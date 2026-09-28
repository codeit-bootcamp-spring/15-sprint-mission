package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
public class FileUserRepository implements UserRepository {

  private final Path dataFile;
  private final FileLockProvider fileLockProvider;

  public FileUserRepository(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      FileLockProvider fileLockProvider
  ) {
    this.dataFile = Path.of(fileDirectory, "user.ser");
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

  private void saveToFile(Map<UUID, User> data) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile.toFile()))) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, User> loadFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
      return (Map<UUID, User>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public User save(User user) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, User> data = loadFromFile();
      data.put(user.getId(), user);
      saveToFile(data);
      return user;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public User read(UUID userId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().get(userId);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<User> readAll() {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream().toList();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void delete(UUID userId) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      Map<UUID, User> data = loadFromFile();
      data.remove(userId);
      saveToFile(data);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean existsByUserName(String userName) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream().anyMatch(user -> user.getUserName().equals(userName));
    } finally {
      lock.unlock();
    }
  }

  @Override
  public User readByUserName(String userName) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream()
          .filter(user -> user.getUserName().equals(userName))
          .findFirst()
          .orElse(null);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean existsByEmail(String email) {
    ReentrantLock lock = fileLockProvider.getLock(dataFile);
    lock.lock();
    try {
      return loadFromFile().values().stream().anyMatch(user -> user.getEmail().equals(email));
    } finally {
      lock.unlock();
    }
  }
}