package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {

    private final Path directory;
    private final FileChannelService fileChannelService;
    private final FileUserService fileUserService;

    public FileMessageService(FileChannelService fileChannelService, FileUserService fileUserService) {
        this.directory = Paths.get(System.getProperty("user.dir"), "data", "message");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException("메세지 데이터 디렉토리를 생성할 수 없습니다.", e);
        }
        this.fileChannelService = fileChannelService;
        this.fileUserService = fileUserService;
    }

    @Override
    public Message createMessage(UUID channelId, UUID authorId, String content) {
        Channel channel = fileChannelService.getChannelInfo(channelId);
        User user = fileUserService.getUser(authorId);
        Message message = new Message(channelId, authorId, content);
        saveToFile(message);
        return message;
    }

    @Override
    public List<Message> getMessagesByChannel(UUID channelId) {
        return getAllMessages().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessagesByUser(UUID userId) {
        return getAllMessages().stream()
                .filter(m -> m.getAuthorId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessageContents(UUID uuid, String newContents) {
        Message message = findMessage(uuid);
        message.update(newContents);
        saveToFile(message);
    }

    @Override
    public void deleteMessage(UUID id) {
        Path path = resolvePath(id);
        try {
            if (!Files.deleteIfExists(path)) {
                throw new IllegalArgumentException("존재하지 않는 메세지 입니다. id = " + id);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("메세지 삭제에 실패했습니다. id = " + id, e);
        }
    }

    private List<Message> getAllMessages() {
        try {
            return Files.list(directory)
                    .filter(p -> p.toString().endsWith(".ser"))
                    .map(this::readFromFile)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("메세지 목록을 불러오지 못했습니다.", e);
        }
    }

    private Path resolvePath(UUID id) {
        return directory.resolve(id.toString() + ".ser");
    }

    private void saveToFile(Message message) {
        Path path = resolvePath(message.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new UncheckedIOException("메세지 저장에 실패했습니다. id = " + message.getId(), e);
        }
    }

    private Message findMessage(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("존재하지 않는 메세지 입니다. id = " + id);
        }
        return readFromFile(path);
    }

    private Message readFromFile(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메세지 역직렬화에 실패했습니다. path = " + path, e);
        }
    }
}
