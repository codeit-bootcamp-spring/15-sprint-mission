package com.sprint.mission.discodeit.storage;


import com.sprint.mission.discodeit.dto.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @PostConstruct
  public void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectories(root);
      }
    } catch (IOException e) {
      throw new RuntimeException("스토리지 루트 디렉터리를 생성할 수 없습니다: " + root, e);
    }
  }

  protected Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path targetPath = resolvePath(id);
    try {
      Files.write(targetPath, bytes, StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING);
      return id;
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패: " + id, e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path targetPath = resolvePath(id);
    try {
      return Files.newInputStream(targetPath, StandardOpenOption.READ);
    } catch (IOException e) {
      throw new RuntimeException("파일 조회 실패: " + id, e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    // dto.id() 호출
    InputStream inputStream = get(dto.id());
    Resource resource = new InputStreamResource(inputStream);

    // dto.fileName() 호출
    ContentDisposition contentDisposition = ContentDisposition.attachment()
        .filename(dto.fileName(), StandardCharsets.UTF_8)
        .build();

    MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
    // dto.contentType() 호출
    if (dto.contentType() != null && !dto.contentType().isBlank()) {
      try {
        mediaType = MediaType.parseMediaType(dto.contentType());
      } catch (Exception ignored) {
      }
    }

    return ResponseEntity.ok()
        .contentType(mediaType)
        .contentLength(dto.size()) // dto.size() 호출
        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
        .body(resource);
  }

}
