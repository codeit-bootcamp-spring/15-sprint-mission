package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final Path directory;
    private final FileUserService fileUserService;

    public FileChannelService(FileUserService fileUserService) {
        this.directory = Paths.get(System.getProperty("user.dir"), "data", "channel");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException("채널 데이터 디렉토리를 생성할 수 없습니다.", e);
        }
        this.fileUserService = fileUserService;
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        saveToFile(channel);
        return channel;
    }

    @Override
    public void updateChannelName(UUID id, String channelName) {
        Channel channel = findChannel(id);
        channel.update(channelName);
        saveToFile(channel);
    }

    @Override
    public Channel getChannelInfo(UUID id) {
        return findChannel(id);
    }

    @Override
    public List<Channel> getAllChannel() {
        try {
            return Files.list(directory)
                    .filter(p -> p.toString().endsWith(".ser"))
                    .map(this::readFromFile)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("채널 목록을 불러오지 못했습니다.", e);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        Path path = resolvePath(id);
        try {
            if (!Files.deleteIfExists(path)) {
                throw new IllegalArgumentException("존재하지 않는 채널입니다. id = " + id);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("채널 삭제에 실패했습니다. id = " + id, e);
        }
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = findChannel(channelId);
        User user = fileUserService.getUser(userId);
        channel.addUserToChannel(userId);
        saveToFile(channel);
    }

    @Override
    public List<UUID> getUserInChannel(UUID id) {
        Channel channel = findChannel(id);
        return channel.getUserInChannel();
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        Channel channel = findChannel(channelId);
        User user = fileUserService.getUser(userId);
        channel.deleteUserToChannel(userId);
        saveToFile(channel);
    }

    private Path resolvePath(UUID id) {
        return directory.resolve(id.toString() + ".ser");
    }

    private void saveToFile(Channel channel) {
        Path path = resolvePath(channel.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new UncheckedIOException("채널 저장에 실패했습니다. id = " + channel.getId(), e);
        }
    }

    private Channel findChannel(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다. id = " + id);
        }
        return readFromFile(path);
    }

    private Channel readFromFile(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 역직렬화에 실패했습니다. path = " + path, e);
        }
    }
}
