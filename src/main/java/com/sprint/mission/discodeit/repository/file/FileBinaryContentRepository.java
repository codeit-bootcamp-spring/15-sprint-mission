package com.sprint.mission.discodeit.repository.file;
import org.springframework.stereotype.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path directory;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String root) {
        this.directory = Path.of(root).resolve("binarycontent");
        try {
            Files.createDirectories(directory);
        }
        catch (IOException e) {
            throw new UncheckedIOException("저장 폴더 생성 실패", e);
        }
    }
    private Path path(UUID id) {
        return directory.resolve(id + ".ser");
    }

    // 같은 ID의 파일이 있으면 기존 내용을 수정된 객체로 덮어씁니다.
    @Override
    public BinaryContent save(BinaryContent entity) {
        Path file = path(entity.getId());
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(entity);
        }
        catch (IOException e) {
            throw new UncheckedIOException("저장 실패: " + entity.getId(), e);
        }
        return entity;
    }
    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Path path = path(id);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            return Optional.of((BinaryContent) in.readObject());
        }
        catch (IOException e) {
            throw new UncheckedIOException("조회 실패: " + id, e);
        }
        catch (ClassNotFoundException | ClassCastException e) {
            throw new IllegalStateException("잘못된 저장 데이터: " + path, e);
        }
    }
    @Override
    public List<BinaryContent> findAll() {
        List<BinaryContent> result = new ArrayList<>();
        // Files.list가 연 자원은 try-with-resources로 닫습니다.
        try (Stream<Path> files = Files.list(directory)) {
            for (Path file : files.toList()) {
                String name = file.getFileName().toString();
                if (name.endsWith(".ser")) {
                    UUID id = UUID.fromString(name.substring(0, name.length() - 4));
                    Optional<BinaryContent> entity = findById(id);
                    if (entity.isPresent()) {
                        result.add(entity.get());
                    }
                }
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }
    @Override
    public void deleteById(UUID id) {
        try {
            Files.deleteIfExists(path(id));
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    @Override
    public boolean existsById(UUID id) {
        return Files.exists(path(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : ids) {
            Optional<BinaryContent> content = findById(id);
            if (content.isPresent() && !result.contains(content.get())) {
                result.add(content.get());
            }
        }
        return result;
    }
}
