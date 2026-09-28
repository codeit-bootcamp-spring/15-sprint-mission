package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final Path dataFile;
  private final FileLockProvider fileLockProvider;

  public FileBinaryContentRepository(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      FileLockProvider fileLockProvider
  ) {
    this.dataFile = Path.of(fileDirectory, "binary_content.ser");
    this.fileLockProvider = fileLockProvider;

    File file = dataFile.toFile();
    File parentDir = file.getParentFile();
    if (parentDir != null && !parentDir.exists()) {
      parentDir.mkdirs();
    }
    if (!file.exists()) {
      saveToFile(new HashMap<>());
    }
  }

  private void saveToFile(Map<UUID, BinaryContent> data) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile.toFile()))) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, BinaryContent> loadFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
      return (Map<UUID, BinaryContent>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, BinaryContent> data = loadFromFile();
      data.put(binaryContent.getId(), binaryContent);
      saveToFile(data);
      return binaryContent;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public BinaryContent read(UUID id) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().get(id);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<BinaryContent> readAllByIdIn(List<UUID> ids) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream()
          .filter(bc -> ids.contains(bc.getId()))
          .toList();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void delete(UUID id) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, BinaryContent> data = loadFromFile();
      data.remove(id);
      saveToFile(data);
    } finally {
      lock.unlock();
    }
  }
}