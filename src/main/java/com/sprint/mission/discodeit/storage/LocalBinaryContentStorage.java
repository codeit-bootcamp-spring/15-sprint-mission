package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.Response.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@ConditionalOnProperty(
        prefix = "discodeit.storage",
        name = "type",
        havingValue = "local"
)
public class LocalBinaryContentStorage implements BinaryContentStorage{

    private final Path root;


    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
        this.root = Paths.get(rootPath);


    }
    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }


    @PostConstruct
    private void init() {
        if (Files.notExists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public UUID put(UUID uuid, byte[] bytes) {
        Path path = resolvePath(uuid);

        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return uuid;
    }

    @Override
    public InputStream get(UUID uuid) {
        Path path = resolvePath(uuid);

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
        UUID id = binaryContentDto.id();

        InputStream inputStream = get(id);
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
                .contentLength(binaryContentDto.size())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(binaryContentDto.fileName(), StandardCharsets.UTF_8)
                                .build()
                                .toString()
                )
                .body(resource);
    }
}
