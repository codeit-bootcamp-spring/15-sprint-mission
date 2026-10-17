package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;



    @Override
    @Transactional(readOnly = true)
    public BinaryContentDto find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("바이너리컨텐츠 id 없음 : " + id));
        return BinaryContentDto.from(binaryContent);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentDto> findAllByIds(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds)
                .stream()
                .map(BinaryContentDto::from)
                .toList();
    }// todo Ids에서 하나라도 노서치가 발생하면 익셉션을 던질지 고민중


    @Override
    public ResponseEntity<?> download(UUID id){
        return binaryContentStorage.download(find(id));
    }
}
