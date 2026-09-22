package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.FileLockProvider;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {

  private final Path path;
  private final FileLockProvider fileLockProvider;

  public FileUserStatusRepository(@Value("${discodeit.repository.file-directory}") String directory,
      FileLockProvider fileLockProvider) {
    path = Paths.get(directory, "userStatus");
    this.fileLockProvider = fileLockProvider;
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public boolean save(UserStatus userStatus) {
    Path filePath = path.resolve("userStatus-" + userStatus.getId() + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(filePath.toFile()))) {
      oos.writeObject(userStatus);
      return true;
    } catch (IOException e) {
      throw new IllegalStateException("못읽었서");
    } finally {
      lock.unlock();
    }
  }

  @Override
  public UserStatus find(UUID id) {
    Path filePath = path.resolve("userStatus-" + id + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
      return (UserStatus) ois.readObject();
    } catch (NoSuchFileException e) {
      return null;
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
    if (files == null || files.length == 0) {
      System.out.println("읽을 파일이 없습니다.");
      return null;
    }

    for (File file : files) {
      ReentrantLock lock = fileLockProvider.getLock(path);
      lock.lock();

      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        Object obj = ois.readObject();
        UserStatus temp = (UserStatus) obj;
        if (temp.getUserId().equals(userId)) {
          return temp;
        }

      } catch (IOException | ClassNotFoundException e) {
        System.err.println("파일 역직렬화 실패: " + file.getName());
      } finally {
        lock.unlock();
      }
    }

    return null;
  }

  @Override
  public List<UserStatus> findAll() {
    List<UserStatus> result = new ArrayList<>();

    File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
    if (files == null || files.length == 0) {
      System.out.println("읽을 파일이 없습니다.");
      return new ArrayList<>();
    }

    for (File file : files) {
      ReentrantLock lock = fileLockProvider.getLock(path);
      lock.lock();

      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        Object obj = ois.readObject();
        result.add((UserStatus) obj);

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
  public void delete(UUID id) {
    Path filePath = path.resolve("userStatus-" + id + ".ser");
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
