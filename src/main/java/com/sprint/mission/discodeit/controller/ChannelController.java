package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.*;
import com.sprint.mission.discodeit.dto.Response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    //공개 채널 생성
    @Operation(
            summary = "Public Channel 생성",
            operationId = "create_3"
    )
    @ApiResponse(
            responseCode = "201",
            description = "공개 채널 등록 성공"
    )
    @PostMapping("/public")
    public ResponseEntity<Channel> createPublicChannel(@RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = channelService.create(publicChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channel);
    }

    //비공개 채널 생성
    @Operation(
            summary = "Private Channel 생성",
            operationId = "create_4"
    )
    @ApiResponse(
            responseCode = "201",
            description = "비공개 채널 등록 성공"
    )
    @PostMapping("/private")
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = channelService.create(privateChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channel);
    }

    //업데이트(공개 채널만 가능)
    @Operation(
            summary = "Channel 정보 수정",
            operationId = "update_3"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "채널  수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "채널을 수정할 수 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "채널을 찾을 수 없음"
            )
    })
    @PatchMapping("/{channel-id}")
    public ResponseEntity<Channel> patchChannel(
            @PathVariable("channel-id") UUID uuid, @RequestBody ChannelUpdateRequest channelUpdateRequest) {



        Channel channel = channelService.update(uuid, channelUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(channel);
    }

    //삭제
    @Operation(
            summary = "Channel 삭제",
            operationId = "delete_2"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "채널 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "채널을 찾을 수 없음"
            )
    })
    @DeleteMapping("/{channel-id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable("channel-id") UUID uuid) {
        channelService.delete(uuid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    //
    @Operation(
            summary = "User가 참여 중인 Channel 목록 조회",
            operationId = "findAll_1"
    )
    @GetMapping
    public ResponseEntity<List<ChannelDto>> getAllByUserId( @RequestParam("userId") UUID userId){
        List<ChannelDto> channels = channelService.findAllByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channels);
    }

}
