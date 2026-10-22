package com.sprint.mission.discodeit.storage;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {

  void put(UUID id, byte[] bytes);

  InputStream get(UUID id);

  void delete(UUID id);
}