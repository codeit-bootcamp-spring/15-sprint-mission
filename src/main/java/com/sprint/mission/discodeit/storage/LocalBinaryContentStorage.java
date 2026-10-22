package com.sprint.mission.discodeit.storage;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root = Path.of("storage", "binaryContents");

  public LocalBinaryContentStorage() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new UncheckedIOException("파일 저장 폴더 생성 실패", e);
    }
  }

  @Override
  public void put(UUID id, byte[] bytes) {
    try {
      Files.write(root.resolve(id.toString()), bytes);
    } catch (IOException e) {
      throw new UncheckedIOException("파일 저장 실패", e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    try {
      return Files.newInputStream(root.resolve(id.toString()));
    } catch (IOException e) {
      throw new UncheckedIOException("파일 읽기 실패", e);
    }
  }


  @Override
  public void delete(UUID id) {
    try {
      Files.deleteIfExists(root.resolve(id.toString()));
    } catch (IOException e) {
      throw new UncheckedIOException("파일 삭제 실패", e);
    }
  }
}