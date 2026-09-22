package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.UUID;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@AllArgsConstructor
public class BinaryContentController {

  private final BinaryContentService contentService;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContent>> getContents(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    return ResponseEntity.ok().body(contentService.findAllByIdIn(binaryContentIds));
  }

  @RequestMapping(path = "/{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContent> getContent(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    return ResponseEntity.ok().body(contentService.find(binaryContentId));
  }
}
