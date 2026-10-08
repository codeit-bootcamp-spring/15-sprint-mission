package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
public class FileUserStatusRepository implements UserStatusRepository {

  private final Path dataFile;
  private final FileLockProvider fileLockProvider;

  public FileUserStatusRepository(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      FileLockProvider fileLockProvider
  ) {
    this.dataFile = Path.of(fileDirectory, "user_status.ser");
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

  private void saveToFile(Map<UUID, UserStatus> data) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile.toFile()))) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, UserStatus> loadFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
      return (Map<UUID, UserStatus>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, UserStatus> data = loadFromFile();
      data.put(userStatus.getId(), userStatus);
      saveToFile(data);
      return userStatus;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public UserStatus read(UUID id) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().get(id);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<UserStatus> readAll() {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream().toList();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public UserStatus readByUserId(UUID userId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream()
          .filter(userStatus -> userStatus.getUserId().equals(userId))
          .findFirst()
          .orElse(null);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void delete(UUID id) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, UserStatus> data = loadFromFile();
      data.remove(id);
      saveToFile(data);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void deleteByUserId(UUID userId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, UserStatus> data = loadFromFile();
      data.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
      saveToFile(data);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean existsByUserId(UUID userId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream()
          .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    } finally {
      lock.unlock();
    }
  }
}