package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.FileLockProvider;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository implements ReadStatusRepository {

  private final Path path;
  private final FileLockProvider fileLockProvider;

  public FileReadStatusRepository(@Value("${discodeit.repository.file-directory}") String directory,
      FileLockProvider fileLockProvider) {
    path = Paths.get(directory, "readStatus");
    this.fileLockProvider = fileLockProvider;
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    Path filePath = path.resolve("readStatus-" + readStatus.getId() + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(filePath.toFile()))) {
      oos.writeObject(readStatus);
      return readStatus;
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
    return null;
  }

  @Override
  public ReadStatus isAlreadyExist(UUID userId, UUID channelId) {
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
        ReadStatus temp = (ReadStatus) obj;
        if (temp.getUserId().equals(userId) && temp.getChannelId().equals(channelId)) {
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
  public ReadStatus find(UUID id) {
    Path filePath = path.resolve("readStatus-" + id + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
      return (ReadStatus) ois.readObject();
    } catch (NoSuchFileException e) {
      throw new IllegalArgumentException("존재하지 않습니다.", e);
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    List<ReadStatus> result = new ArrayList<>();

    File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
    if (files == null || files.length == 0) {
      return null;
    }

    for (File file : files) {
      ReentrantLock lock = fileLockProvider.getLock(path);
      lock.lock();

      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        Object obj = ois.readObject();
        ReadStatus temp = (ReadStatus) obj;
        if (temp.getUserId().equals(userId)) {
          result.add(temp);
        }
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
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    List<ReadStatus> result = new ArrayList<>();
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
        ReadStatus temp = (ReadStatus) obj;

        if (temp.getChannelId().equals(channelId)) {
          result.add(temp);
        }
      } catch (IOException | ClassNotFoundException e) {
        System.err.println("특정 파일 역직렬화 실패: " + file.getName());
        return result;
      } finally {
        lock.unlock();
      }
    }

    return result;
  }

  @Override
  public void delete(UUID id) {
    Path filePath = path.resolve("readStatus-" + id + ".ser");
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
