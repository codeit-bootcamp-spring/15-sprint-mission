package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentCreateRequest binaryContentCreateRequest) {
        BinaryContent binaryContent = new BinaryContent(binaryContentCreateRequest.fileName(), binaryContentCreateRequest.file());
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("바이너리컨텐츠 id 없음 : " + id));
    }

    @Override
    public List<BinaryContent> findAllByIds(List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContentList = new ArrayList<>();
        for(UUID id : binaryContentIds){
            binaryContentList.add(binaryContentRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("바이너리컨텐츠 id 없음 : " + id)));
        }
        return binaryContentList;
    }

    @Override
    public void delete(UUID id) {
        if(!binaryContentRepository.existsById(id)){
            throw new NoSuchElementException("바이너리컨텐츠 id 없음 : " + id);
        }
        binaryContentRepository.deleteById(id);

    }
}
