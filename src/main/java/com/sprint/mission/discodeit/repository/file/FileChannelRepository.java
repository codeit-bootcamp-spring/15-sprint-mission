package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository, Serializable {
    private final Path path;

    public FileChannelRepository() {
        path = Paths.get("data", "channels");
        try {
            Files.createDirectories(path);
        }
        catch (NoSuchFileException e) {
            System.out.println("폴더 경로가 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean create(Channel channel) {
        Path filePath = Paths.get("data", "users", "user-" + channel.getId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())
        )) {
            oos.writeObject(channel);
            System.out.println("["+ channel.getName() + "] 채널 저장 완료");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Channel find(UUID id) {
        List<Channel> channels = this.findAll();
        for (Channel channel: channels) {
            if (channel.getId().equals(id)) return channel;
        }

        return null;
    }

    @Override
    public Channel findByChannelName(String channelName) {
        List<Channel> channels = this.findAll();
        for (Channel channel: channels) {
            if (channel.getName().equals(channelName)) return channel;
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
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                result.add((Channel) obj);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return new ArrayList<>();
            }
        }

        return result;
    }


    @Override
    public boolean update(Channel channel) {
        Path filePath = Paths.get("data", "users", "user-" + channel.getId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())))
        {
            oos.writeObject(channel);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(UUID id) {
        Path filePath = Paths.get("data", "users", "user-" + id + ".ser");

        File file = new File(filePath.toUri());
        return file.exists() && file.delete();
    }
}
