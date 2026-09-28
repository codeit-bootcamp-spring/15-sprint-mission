package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
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
public class FileMessageRepository implements MessageRepository {

  private final Path dataFile;
  private final FileLockProvider fileLockProvider;

  public FileMessageRepository(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      FileLockProvider fileLockProvider
  ) {
    this.dataFile = Path.of(fileDirectory, "message.ser");
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

  private void saveToFile(Map<UUID, Message> data) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile.toFile()))) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, Message> loadFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
      return (Map<UUID, Message>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Message save(Message message) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, Message> data = loadFromFile();
      data.put(message.getId(), message);
      saveToFile(data);
      return message;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Message read(UUID messageId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().get(messageId);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<Message> readAllByChannelId(UUID channelId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream()
          .filter(message -> message.getChannelId().equals(channelId))
          .toList();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void delete(UUID messageId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, Message> data = loadFromFile();
      data.remove(messageId);
      saveToFile(data);
    } finally {
      lock.unlock();
    }
  }
}