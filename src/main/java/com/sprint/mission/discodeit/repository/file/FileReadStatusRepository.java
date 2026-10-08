package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository implements ReadStatusRepository {

  private final Path dataFile;
  private final FileLockProvider fileLockProvider;

  public FileReadStatusRepository(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      FileLockProvider fileLockProvider
  ) {
    this.dataFile = Path.of(fileDirectory, "read_status.ser");
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

  private void saveToFile(Map<UUID, ReadStatus> data) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile.toFile()))) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, ReadStatus> loadFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
      return (Map<UUID, ReadStatus>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, ReadStatus> data = loadFromFile();
      data.put(readStatus.getId(), readStatus);
      saveToFile(data);
      return readStatus;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public ReadStatus read(UUID id) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().get(id);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<ReadStatus> readAllByUserId(UUID userId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream()
          .filter(readStatus -> readStatus.getUserId().equals(userId))
          .toList();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<ReadStatus> readAllByChannelId(UUID channelId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream()
          .filter(readStatus -> readStatus.getChannelId().equals(channelId))
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
      Map<UUID, ReadStatus> data = loadFromFile();
      data.remove(id);
      saveToFile(data);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, ReadStatus> data = loadFromFile();
      data.values().removeIf(readStatus -> readStatus.getChannelId().equals(channelId));
      saveToFile(data);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream()
          .anyMatch(readStatus -> readStatus.getUserId().equals(userId)
              && readStatus.getChannelId().equals(channelId));
    } finally {
      lock.unlock();
    }
  }
}