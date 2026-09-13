package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path filePath;

    public FileBinaryContentRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}")
            String fileDirectory
    ) {
        this.filePath = Path.of(
                fileDirectory,
                "binaryContents.ser"
        );
    }


    // 저장
    @Override
    public void save(BinaryContent binaryContent) {

        Map<UUID, BinaryContent> binaryContents = load();

        binaryContents.put(
                binaryContent.getId(),
                binaryContent
        );

        saveAll(binaryContents);
    }


    // id로 하나 조회
    @Override
    public Optional<BinaryContent> findById(UUID id) {

        Map<UUID, BinaryContent> binaryContents = load();

        return Optional.ofNullable(
                binaryContents.get(id)
        );
    }


    // 여러 id로 조회
    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {

        Map<UUID, BinaryContent> binaryContents = load();

        List<BinaryContent> result = new ArrayList<>();

        for (UUID id : ids) {

            BinaryContent binaryContent = binaryContents.get(id);

            if (binaryContent != null) {
                result.add(binaryContent);
            }
        }

        return result;
    }


    // id로 삭제
    @Override
    public void deleteById(UUID id) {

        Map<UUID, BinaryContent> binaryContents = load();

        binaryContents.remove(id);

        saveAll(binaryContents);
    }


    // 전체 데이터를 파일에 저장
    private void saveAll(Map<UUID, BinaryContent> binaryContents) {

        try {

            // application.yaml에서 지정한 폴더가 없으면 생성
            Files.createDirectories(filePath.getParent());

            try (
                    ObjectOutputStream outputStream =
                            new ObjectOutputStream(
                                    new FileOutputStream(filePath.toFile())
                            )
            ) {

                outputStream.writeObject(binaryContents);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "BinaryContent 저장 중 오류가 발생했습니다.",
                    e
            );
        }
    }


    // 파일에서 전체 데이터 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, BinaryContent> load() {

        File file = filePath.toFile();

        // 아직 파일이 없으면 빈 Map 반환
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (
                ObjectInputStream inputStream =
                        new ObjectInputStream(
                                new FileInputStream(file)
                        )
        ) {

            return (Map<UUID, BinaryContent>)
                    inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {

            throw new RuntimeException(
                    "BinaryContent 조회 중 오류가 발생했습니다.",
                    e
            );
        }
    }
}