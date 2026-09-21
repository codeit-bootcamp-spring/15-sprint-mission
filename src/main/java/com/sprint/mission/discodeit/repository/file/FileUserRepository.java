package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.FileLockProvider;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileUserRepository implements UserRepository, Serializable {

  private final Path path;
  private final FileLockProvider fileLockProvider;

  public FileUserRepository(@Value("${discodeit.repository.file-directory}") String directory,
      FileLockProvider fileLockProvider) {
    path = Paths.get(directory, "userContents");
    this.fileLockProvider = fileLockProvider;
    try {
      Files.createDirectories(path);
    } catch (NoSuchFileException e) {
      System.out.println("폴더 경로가 없음");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean create(User user) {
    Path filePath = path.resolve("user-" + user.getId() + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(filePath.toFile()))) {
      oos.writeObject(user);
      System.out.println("[" + user.getUsername() + "] 유저 저장 완료");
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
    return false;
  }

  @Override
  public User find(UUID userId) {
    Path filePath = path.resolve("user-" + userId + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
      return (User) ois.readObject();
    } catch (NoSuchFileException e) {
      throw new IllegalArgumentException("존재하지 않는 유저입니다.", e);
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public User findByName(String name) {
    Set<User> users = this.findAll();
    for (User user : users) {
      if (user.getUsername().equals(name)) {
        return user;
      }
    }

    return null;
  }

  @Override
  public Set<User> findAll() {
    Set<User> result = new HashSet<>();

    File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
    if (files == null || files.length == 0) {
      System.out.println("읽을 파일이 없습니다.");
      return new HashSet<>();
    }

    for (File file : files) {
      ReentrantLock lock = fileLockProvider.getLock(path);
      lock.lock();

      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        Object obj = ois.readObject();
        result.add((User) obj);

      } catch (IOException | ClassNotFoundException e) {
        System.err.println("파일 역직렬화 실패: " + file.getName());
        return result;
      } finally {
        lock.unlock();
      }
    }

    return result;
  }


  @Override
  public boolean update(User user) {
    Path filePath = path.resolve("user-" + user.getId() + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(filePath.toFile()))) {
      oos.writeObject(user);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
    return false;
  }

  @Override
  public void delete(UUID userId) {
    Path filePath = path.resolve("user-" + userId + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try {
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      System.out.println("삭제 실패");
    } finally {
      lock.unlock();
    }
  }
}
