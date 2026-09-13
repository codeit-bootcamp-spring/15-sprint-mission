package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final String dataFile = "data/binary_content.ser";

    public FileBinaryContentRepository() {
        File file = new File(dataFile);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    private void saveToFile(Map<UUID, BinaryContent> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, BinaryContent> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, BinaryContent>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized BinaryContent save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> data = loadFromFile();
        data.put(binaryContent.getId(), binaryContent);
        saveToFile(data);
        return binaryContent;
    }

    @Override
    public BinaryContent read(UUID id) {
        Map<UUID, BinaryContent> data = loadFromFile();
        return data.get(id);
    }

    @Override
    public List<BinaryContent> readAllByIdIn(List<UUID> ids) {
        Map<UUID, BinaryContent> data = loadFromFile();
        return data.values().stream()
                .filter(bc -> ids.contains(bc.getId()))
                .toList();
    }

    @Override
    public synchronized void delete(UUID id) {
        Map<UUID, BinaryContent> data = loadFromFile();
        data.remove(id);
        saveToFile(data);
    }
}