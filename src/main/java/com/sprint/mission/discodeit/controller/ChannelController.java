package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "Channel API")
@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final BasicChannelService channelService;

    public ChannelController(BasicChannelService channelService) {
        this.channelService = channelService;
    }

    // 등록
    @Operation(summary = "Public Channel 생성", operationId = "create_3")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨")
    })
    @RequestMapping(value="/public", method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<ChannelResponse>> createChannel(
            @Valid @RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = channelService.create(publicChannelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ChannelResponse.from(channel)));
    }
    @Operation(summary = "Private Channel 생성", operationId = "create_4")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨")
    })
    @RequestMapping(value="/private", method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<ChannelResponse>> createChannel(
            @Valid @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = channelService.create(privateChannelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ChannelResponse.from(channel)));
    }

    // 조회
    @Operation(summary = "Channel 조회")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Channel 조회 성공"),
    })
    @RequestMapping(value="/{channel-id}", method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<ChannelResponse>> getChannel(
            @PathVariable("channel-id") UUID channelId
            ){
        ChannelDto channelDto = channelService.find(channelId);
        return ResponseEntity.ok(ApiResponse.success(ChannelResponse.from(channelDto)));
    }

    @Operation(summary = "User가 참여 중인 Channel 목록 조회", operationId = "findAll_1")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공"),
    })
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ChannelResponse>>> getChannels(
            @RequestParam("user-id") UUID userId
    ){
        List<ChannelResponse> channels = channelService.findAllByUserId(userId)
                .stream().map(ChannelResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(channels));
    }

    // 수정
    @Operation(summary = "Channel 정보 수정", operationId = "update_3")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음"),
    })
    @RequestMapping(value="/{channel-id}", method=RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<ChannelResponse>> updateChannel(
            @PathVariable ("channel-id") UUID channelId,
            @Valid @RequestBody PublicChannelUpdateRequest request
            ) {
        Channel update = channelService.update(channelId, request);
        return ResponseEntity.ok(ApiResponse.success(ChannelResponse.from(update)));
    }
    // 삭제
    @Operation(summary = "Channel 삭제", operationId = "delete_2")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨"),
    })
    @RequestMapping(value="/{channel-id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(
            @PathVariable ("channel-id") UUID channelId
    ) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

}
