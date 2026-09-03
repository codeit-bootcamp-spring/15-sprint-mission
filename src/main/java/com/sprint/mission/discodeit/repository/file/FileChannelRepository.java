package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileChannelRepository implements ChannelRepository {

    private final Path directory;

    public FileChannelRepository() {
        this.directory = Paths.get(System.getProperty("user.dir"), "data", "channel");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException("채널 데이터 디렉토리를 생성할 수 없습니다.", e);
        }
    }

    private Path resolvePath(UUID id) {
        return directory.resolve(id.toString() + ".ser");
    }

    @Override
    public Channel createChannel(Channel channel) {
        Path path = resolvePath(channel.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new UncheckedIOException("채널 저장에 실패했습니다. id = " + channel.getId(), e);
        }
        return channel;
    }

    @Override
    public Optional<Channel> getChannel(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        return Optional.of(readChannel(path));
    }

    @Override
    public List<Channel> getChannelAll() {
        try (Stream<Path> paths = Files.list(directory)) {
            return paths
                    .filter(path -> path.toString().endsWith(".ser"))
                    .map(this::readChannel)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("채널 목록을 불러오지 못했습니다.", e);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        try {
            Files.deleteIfExists(resolvePath(id));
        } catch (IOException e) {
            throw new UncheckedIOException("채널 삭제에 실패했습니다. id = " + id, e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    private Channel readChannel(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 역직렬화에 실패했습니다. path = " + path, e);
        }
    }
}
