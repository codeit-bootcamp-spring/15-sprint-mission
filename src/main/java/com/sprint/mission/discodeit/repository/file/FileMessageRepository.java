package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

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

public class FileMessageRepository implements MessageRepository {

    private final Path directory;

    public FileMessageRepository() {
        this.directory = Paths.get(System.getProperty("user.dir"), "data", "message");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException("메세지 데이터 디렉토리를 생성할 수 없습니다.", e);
        }
    }

    private Path resolvePath(UUID id) {
        return directory.resolve(id.toString() + ".ser");
    }

    @Override
    public Message createMessage(Message message) {
        Path path = resolvePath(message.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new UncheckedIOException("메세지 저장에 실패했습니다. id = " + message.getId(), e);
        }
        return message;
    }

    @Override
    public Optional<Message> getMessage(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        return Optional.of(readMessage(path));
    }

    @Override
    public List<Message> getMessageAll() {
        try (Stream<Path> paths = Files.list(directory)) {
            return paths
                    .filter(path -> path.toString().endsWith(".ser"))
                    .map(this::readMessage)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("메시지 목록을 불러오지 못했습니다.",e);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        try {
            Files.deleteIfExists(resolvePath(id));
        } catch (IOException e) {
            throw new UncheckedIOException("메세지 삭제에 실패했습니다. id = " + id, e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    private Message readMessage(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메세지 역직렬화에 실패했습니다. path = " + path, e);
        }
    }
}
