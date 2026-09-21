package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.FileLockProvider;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository, Serializable {

  private final Path path;
  private final FileLockProvider fileLockProvider;

  public FileChannelRepository(@Value("${discodeit.repository.file-directory}") String directory,
      FileLockProvider fileLockProvider) {
    path = Paths.get(directory, "channels");
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
  public boolean create(Channel channel) {
    Path filePath = path.resolve("channel-" + channel.getId() + ".ser");

    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
      oos.writeObject(channel);
      System.out.println("[" + channel.getName() + "] 채널 저장 완료");
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
    return false;
  }

  @Override
  public Channel find(UUID id) {
    Path filePath = path.resolve("channel-" + id + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
      return (Channel) ois.readObject();
    } catch (NoSuchFileException e) {
      return null;
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Channel findByChannelName(String channelName) {
    List<Channel> channels = this.findAll();
    for (Channel channel : channels) {
      if (channel.getName().equals(channelName)) {
        return channel;
      }
    }

    return null;
  }

  @Override
  public List<Channel> findAll() {
    File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
    List<Channel> result = new ArrayList<>();

    if (files == null || files.length == 0) {
      System.out.println("읽을 파일이 없습니다.");
      return new ArrayList<>();
    }

    for (File file : files) {
      ReentrantLock lock = fileLockProvider.getLock(path);
      lock.lock();

      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        Object obj = ois.readObject();
        result.add((Channel) obj);

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
  public boolean update(Channel channel) {
    Path filePath = path.resolve("channel-" + channel.getId() + ".ser");
    ReentrantLock lock = fileLockProvider.getLock(path);
    lock.lock();

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
      oos.writeObject(channel);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
    return false;
  }

  @Override
  public void delete(UUID id) {
    Path filePath = path.resolve("channel-" + id + ".ser");
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
