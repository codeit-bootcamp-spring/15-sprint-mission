package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.BinaryContentRequest.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.ChannelFindRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.UserDto.LoginRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserFindRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto.UserStatusUpdateRequest;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context =
				SpringApplication.run(
						DiscodeitApplication.class,
						args
				);



			// Spring Bean 가져오기
			UserService userService =
					context.getBean(UserService.class);

			AuthService authService =
					context.getBean(AuthService.class);

			ChannelService channelService =
					context.getBean(ChannelService.class);

			MessageService messageService =
					context.getBean(MessageService.class);

			ReadStatusService readStatusService =
					context.getBean(ReadStatusService.class);

			UserStatusService userStatusService =
					context.getBean(UserStatusService.class);

			BinaryContentService binaryContentService =
					context.getBean(BinaryContentService.class);


			System.out.println();
			System.out.println("======================================");
			System.out.println(" Discodeit Sprint Mission 통합 테스트");
			System.out.println(" File Repository 사용");
			System.out.println("======================================");
			System.out.println();


			// 매 실행마다 다른 이름 사용
			// File Repository에 기존 데이터가 남아 있어도
			// 중복 사용자 오류가 발생하지 않도록 함
			String runId =
					UUID.randomUUID()
							.toString()
							.substring(0, 8);


			/*
			 * ==========================================
			 * 1. BinaryContentService 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 1. BinaryContentService =========="
			);

			byte[] fileData1 =
					"binary-test-file-1"
							.getBytes(StandardCharsets.UTF_8);

			byte[] fileData2 =
					"binary-test-file-2"
							.getBytes(StandardCharsets.UTF_8);


			BinaryContent binary1 =
					binaryContentService.create(
							new BinaryContentCreateRequest(
									"test1.txt",
									"text/plain",
									fileData1.length,
									fileData1
							)
					);

			BinaryContent binary2 =
					binaryContentService.create(
							new BinaryContentCreateRequest(
									"test2.txt",
									"text/plain",
									fileData2.length,
									fileData2
							)
					);


			check(
					binaryContentService.find(binary1.getId())
							.getId()
							.equals(binary1.getId()),
					"BinaryContent id 조회"
			);


			List<BinaryContent> binaryContents =
					binaryContentService.findAllByIdIn(
							List.of(
									binary1.getId(),
									binary2.getId()
							)
					);

			check(
					binaryContents.size() == 2,
					"BinaryContent id 목록 조회"
			);


			binaryContentService.delete(binary1.getId());

			check(
					!binaryContentExists(
							binaryContentService,
							binary1.getId()
					),
					"BinaryContent 삭제"
			);

			// 두 번째 테스트 파일도 정리
			binaryContentService.delete(binary2.getId());


			/*
			 * ==========================================
			 * 2. UserService create 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 2. UserService create =========="
			);


			byte[] profileData =
					("profile-" + runId)
							.getBytes(StandardCharsets.UTF_8);


			BinaryContentCreateRequest profileRequest =
					new BinaryContentCreateRequest(
							"profile-" + runId + ".png",
							"image/png",
							profileData.length,
							profileData
					);


			User user1 =
					userService.create(
							new UserCreateRequest(
									"user-" + runId,
									"user-" + runId + "@test.com",
									"password123"
							),
							profileRequest
					);


			check(
					user1 != null,
					"프로필 이미지와 함께 User 생성"
			);

			check(
					user1.getProfileId() != null,
					"User에 profileId 등록"
			);

			check(
					binaryContentExists(
							binaryContentService,
							user1.getProfileId()
					),
					"프로필 BinaryContent 저장"
			);


			User user2 =
					userService.create(
							new UserCreateRequest(
									"friend-" + runId,
									"friend-" + runId + "@test.com",
									"friendPassword"
							),
							null
					);


			check(
					user2 != null,
					"프로필 없는 User 생성"
			);


			/*
			 * username / email 중복 테스트
			 */

			expectException(
					"중복 username 차단",
					() ->
							userService.create(
									new UserCreateRequest(
											user1.getName(),
											"different-" + runId + "@test.com",
											"password"
									),
									null
							)
			);


			expectException(
					"중복 email 차단",
					() ->
							userService.create(
									new UserCreateRequest(
											"different-" + runId,
											user1.getEmail(),
											"password"
									),
									null
							)
			);


			/*
			 * ==========================================
			 * 3. UserStatus 자동 생성 / User 조회
			 * ==========================================
			 */

			System.out.println(
					"\n========== 3. User 조회 + UserStatus =========="
			);


			UserFindRequest foundUser =
					userService.find(user1.getId());


			check(
					foundUser != null,
					"User id 조회"
			);

			check(
					foundUser.online(),
					"UserStatus를 이용한 온라인 상태 포함"
			);


			List<UserFindRequest> allUsers =
					userService.findAll();


			check(
					allUsers.stream()
							.anyMatch(user ->
									user.id().equals(user1.getId())),
					"User 전체 조회"
			);


			UserStatus autoStatus =
					userStatusService.findAll()
							.stream()
							.filter(status ->
									status.getUserId()
											.equals(user1.getId()))
							.findFirst()
							.orElseThrow();


			check(
					autoStatus != null,
					"User 생성 시 UserStatus 자동 생성"
			);


			/*
			 * ==========================================
			 * 4. UserStatusService 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 4. UserStatusService =========="
			);


			check(
					userStatusService.find(autoStatus.getId())
							!= null,
					"UserStatus id 조회"
			);


			// 6분 전 → 오프라인
			UserStatus offlineStatus =
					userStatusService.update(
							autoStatus.getId(),
							new UserStatusUpdateRequest(
									Instant.now()
											.minus(
													Duration.ofMinutes(6)
											)
							)
					);


			check(
					!offlineStatus.isOnline(),
					"5분 초과 시 오프라인 판정"
			);


			// 다시 현재 시간으로 갱신
			UserStatus onlineStatus =
					userStatusService.updateByUserId(
							user1.getId(),
							new UserStatusUpdateRequest(
									Instant.now()
							)
					);


			check(
					onlineStatus.isOnline(),
					"5분 이내 온라인 판정"
			);


			/*
			 * UserStatus delete / create 기능까지 테스트
			 */

			UUID oldStatusId =
					onlineStatus.getId();


			userStatusService.delete(oldStatusId);


			check(
					userStatusService.find(oldStatusId) == null,
					"UserStatus id 삭제"
			);


			UserStatus recreatedStatus =
					userStatusService.create(
							new UserStatusCreateRequest(
									user1.getId()
							)
					);


			check(
					recreatedStatus != null,
					"UserStatus DTO 기반 생성"
			);


			/*
			 * ==========================================
			 * 5. UserService update 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 5. UserService update =========="
			);


			UUID oldProfileId =
					user1.getProfileId();


			byte[] newProfileData =
					("new-profile-" + runId)
							.getBytes(StandardCharsets.UTF_8);


			User updatedUser =
					userService.update(
							user1.getId(),
							new UserUpdateRequest(
									"updated-" + runId,
									"updated-" + runId + "@test.com",
									"newPassword123"
							),
							new BinaryContentCreateRequest(
									"new-profile-" + runId + ".png",
									"image/png",
									newProfileData.length,
									newProfileData
							)
					);


			check(
					updatedUser != null,
					"User 수정"
			);

			check(
					updatedUser.getName()
							.equals("updated-" + runId),
					"User 이름 수정"
			);

			check(
					updatedUser.getProfileId() != null
							&& !updatedUser.getProfileId()
							.equals(oldProfileId),
					"프로필 이미지 교체"
			);


			check(
					!binaryContentExists(
							binaryContentService,
							oldProfileId
					),
					"기존 프로필 BinaryContent 삭제"
			);


			check(
					binaryContentExists(
							binaryContentService,
							updatedUser.getProfileId()
					),
					"새 프로필 BinaryContent 저장"
			);


			/*
			 * ==========================================
			 * 6. AuthService 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 6. AuthService =========="
			);


			User loginUser =
					authService.login(
							new LoginRequest(
									updatedUser.getName(),
									"newPassword123"
							)
					);


			check(
					loginUser.getId()
							.equals(updatedUser.getId()),
					"username + password 로그인"
			);


			expectException(
					"잘못된 비밀번호 로그인 차단",
					() ->
							authService.login(
									new LoginRequest(
											updatedUser.getName(),
											"wrong-password"
									)
							)
			);


			/*
			 * ==========================================
			 * 7. PUBLIC Channel 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 7. PUBLIC Channel =========="
			);


			Channel publicChannel =
					channelService.createPublic(
							new PublicChannelCreateRequest(
									updatedUser.getId(),
									"공지-" + runId,
									"공개 채널 테스트"
							)
					);


			check(
					publicChannel != null,
					"PUBLIC Channel 생성"
			);


			ChannelFindRequest publicChannelDto =
					channelService.find(
							publicChannel.getId()
					);


			check(
					publicChannelDto != null,
					"Channel id 조회"
			);


			Channel updatedChannel =
					channelService.update(
							publicChannel.getId(),
							new ChannelUpdateRequest(
									"수정된-공지-" + runId,
									null
							)
					);


			check(
					updatedChannel.getChannelName()
							.equals("수정된-공지-" + runId),
					"PUBLIC Channel 수정"
			);


			check(
					updatedChannel.getChannelDescription()
							.equals("공개 채널 테스트"),
					"null 값은 기존 Channel description 유지"
			);


			/*
			 * ==========================================
			 * 8. PRIVATE Channel 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 8. PRIVATE Channel =========="
			);


			Channel privateChannel =
					channelService.createPrivate(
							new PrivateChannelCreateRequest(
									updatedUser.getId(),
									List.of(
											updatedUser.getId(),
											user2.getId()
									)
							)
					);


			check(
					privateChannel != null,
					"PRIVATE Channel 생성"
			);


			check(
					privateChannel.getChannelName() == null
							&& privateChannel
							.getChannelDescription() == null,
					"PRIVATE Channel name/description 생략"
			);


			ChannelFindRequest privateChannelDto =
					channelService.find(
							privateChannel.getId()
					);


			check(
					privateChannelDto.userIds()
							.contains(updatedUser.getId())
							&& privateChannelDto.userIds()
							.contains(user2.getId()),
					"PRIVATE Channel 참여 User id 포함"
			);


			List<ChannelFindRequest> user2Channels =
					channelService.findAllByUserId(
							user2.getId()
					);


			check(
					user2Channels.stream()
							.anyMatch(channel ->
									channel.id()
											.equals(privateChannel.getId())),
					"참여 User가 PRIVATE Channel 조회 가능"
			);


			expectException(
					"PRIVATE Channel 수정 차단",
					() ->
							channelService.update(
									privateChannel.getId(),
									new ChannelUpdateRequest(
											"수정 시도",
											"수정 시도"
									)
							)
			);


			/*
			 * ==========================================
			 * 9. ReadStatusService 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 9. ReadStatusService =========="
			);


			// PRIVATE 채널은 생성될 때 ReadStatus가 자동 생성되므로
			// PUBLIC 채널 + user2 조합으로 별도 생성 테스트
			ReadStatus readStatus =
					readStatusService.create(
							new ReadStatusCreateRequest(
									publicChannel.getId(),
									user2.getId()
							)
					);


			check(
					readStatus != null,
					"ReadStatus 생성"
			);


			check(
					readStatusService.find(
							readStatus.getId()
					) != null,
					"ReadStatus id 조회"
			);


			List<ReadStatus> userReadStatuses =
					readStatusService.findAllByUserId(
							user2.getId()
					);


			check(
					userReadStatuses.stream()
							.anyMatch(status ->
									status.getId()
											.equals(readStatus.getId())),
					"userId 조건 ReadStatus 조회"
			);


			Instant lastReadAt =
					Instant.now();


			ReadStatus updatedReadStatus =
					readStatusService.update(
							readStatus.getId(),
							new ReadStatusUpdateRequest(
									lastReadAt
							)
					);


			check(
					updatedReadStatus.getLastReadAt() != null,
					"ReadStatus 마지막 읽은 시간 수정"
			);


			expectException(
					"동일 Channel + User ReadStatus 중복 생성 차단",
					() ->
							readStatusService.create(
									new ReadStatusCreateRequest(
											publicChannel.getId(),
											user2.getId()
									)
							)
			);


			readStatusService.delete(
					readStatus.getId()
			);


			check(
					readStatusService.find(
							readStatus.getId()
					) == null,
					"ReadStatus 삭제"
			);


			/*
			 * ==========================================
			 * 10. MessageService + 첨부파일 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 10. MessageService =========="
			);


			byte[] attachmentData1 =
					("attachment-1-" + runId)
							.getBytes(StandardCharsets.UTF_8);

			byte[] attachmentData2 =
					("attachment-2-" + runId)
							.getBytes(StandardCharsets.UTF_8);


			List<BinaryContentCreateRequest> attachments =
					List.of(
							new BinaryContentCreateRequest(
									"attachment1-" + runId + ".txt",
									"text/plain",
									attachmentData1.length,
									attachmentData1
							),
							new BinaryContentCreateRequest(
									"attachment2-" + runId + ".txt",
									"text/plain",
									attachmentData2.length,
									attachmentData2
							)
					);


			Message message =
					messageService.create(
							new MessageCreateRequest(
									publicChannel.getId(),
									updatedUser.getId(),
									"첨부파일이 있는 메시지입니다."
							),
							attachments
					);


			check(
					message != null,
					"Message 생성"
			);


			check(
					message.getAttachmentIds().size() == 2,
					"Message 여러 첨부파일 등록"
			);


			List<BinaryContent> savedAttachments =
					binaryContentService.findAllByIdIn(
							message.getAttachmentIds()
					);


			check(
					savedAttachments.size() == 2,
					"Message 첨부 BinaryContent 저장"
			);


			List<Message> channelMessages =
					messageService.findAllByChannelId(
							publicChannel.getId()
					);


			check(
					channelMessages.stream()
							.anyMatch(savedMessage ->
									savedMessage.getId()
											.equals(message.getId())),
					"Channel id 조건 Message 조회"
			);


			/*
			 * 최근 메시지 시간 포함 확인
			 */

			ChannelFindRequest channelAfterMessage =
					channelService.find(
							publicChannel.getId()
					);


			check(
					channelAfterMessage.lastMessageAt() != null,
					"Channel DTO에 최근 Message 시간 포함"
			);


			Message updatedMessage =
					messageService.update(
							message.getId(),
							new MessageUpdateRequest(
									"수정된 메시지입니다."
							)
					);


			check(
					updatedMessage.getContent()
							.equals("수정된 메시지입니다."),
					"Message DTO 기반 수정"
			);


			// 삭제 후 첨부파일도 같이 삭제되는지 확인하기 위해
			// 첨부파일 id를 미리 복사
			List<UUID> attachmentIds =
					List.copyOf(
							message.getAttachmentIds()
					);


			messageService.delete(
					message.getId()
			);


			check(
					messageService.read(
							message.getId()
					) == null,
					"Message 삭제"
			);


			check(
					attachmentIds.stream()
							.noneMatch(id ->
									binaryContentExists(
											binaryContentService,
											id
									)),
					"Message 삭제 시 첨부 BinaryContent 함께 삭제"
			);


			/*
			 * ==========================================
			 * 11. Channel 삭제 연관 데이터 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 11. Channel delete cascade =========="
			);


			Channel deleteChannel =
					channelService.createPrivate(
							new PrivateChannelCreateRequest(
									updatedUser.getId(),
									List.of(
											updatedUser.getId(),
											user2.getId()
									)
							)
					);


			Message deleteChannelMessage =
					messageService.create(
							new MessageCreateRequest(
									deleteChannel.getId(),
									updatedUser.getId(),
									"채널 삭제 테스트 메시지"
							),
							List.of()
					);


			check(
					deleteChannelMessage != null,
					"삭제 테스트 Channel에 Message 생성"
			);


			channelService.delete(
					deleteChannel.getId()
			);


			check(
					messageService
							.findAllByChannelId(
									deleteChannel.getId()
							)
							.isEmpty(),
					"Channel 삭제 시 관련 Message 삭제"
			);


			boolean readStatusStillExists =
					readStatusService
							.findAllByUserId(
									updatedUser.getId()
							)
							.stream()
							.anyMatch(status ->
									status.getChannelId()
											.equals(deleteChannel.getId()));


			check(
					!readStatusStillExists,
					"Channel 삭제 시 관련 ReadStatus 삭제"
			);


			/*
			 * ==========================================
			 * 12. User 삭제 연관 데이터 테스트
			 * ==========================================
			 */

			System.out.println(
					"\n========== 12. User delete cascade =========="
			);


			byte[] deleteProfileData =
					("delete-profile-" + runId)
							.getBytes(StandardCharsets.UTF_8);


			User deleteUser =
					userService.create(
							new UserCreateRequest(
									"delete-user-" + runId,
									"delete-user-" + runId + "@test.com",
									"deletePassword"
							),
							new BinaryContentCreateRequest(
									"delete-profile-" + runId + ".png",
									"image/png",
									deleteProfileData.length,
									deleteProfileData
							)
					);


			UUID deleteProfileId =
					deleteUser.getProfileId();


			UserStatus deleteUserStatus =
					userStatusService
							.findAll()
							.stream()
							.filter(status ->
									status.getUserId()
											.equals(deleteUser.getId()))
							.findFirst()
							.orElseThrow();


			UUID deleteUserStatusId =
					deleteUserStatus.getId();


			userService.delete(
					deleteUser.getId()
			);


			check(
					userService.find(
							deleteUser.getId()
					) == null,
					"User 삭제"
			);


			check(
					!binaryContentExists(
							binaryContentService,
							deleteProfileId
					),
					"User 삭제 시 프로필 BinaryContent 삭제"
			);


			check(
					userStatusService.find(
							deleteUserStatusId
					) == null,
					"User 삭제 시 UserStatus 삭제"
			);


			/*
			 * ==========================================
			 * 테스트 결과
			 * ==========================================
			 */

			System.out.println();
			System.out.println("======================================");
			System.out.println(" 모든 통합 테스트가 정상 통과했습니다.");
			System.out.println(" File Repository에 데이터가 저장되었습니다.");
			System.out.println("======================================");
			System.out.println();



	}


	/*
	 * ==========================================
	 * 테스트 결과 확인용 메서드
	 * ==========================================
	 */

	private static void check(
			boolean condition,
			String testName
	) {

		if (!condition) {
			throw new IllegalStateException(
					"[FAIL] " + testName
			);
		}

		System.out.println(
				"[PASS] " + testName
		);
	}


	/*
	 * 특정 코드에서 예외가 발생해야
	 * 정상인 테스트
	 */
	private static void expectException(
			String testName,
			Runnable test
	) {

		try {

			test.run();

		} catch (IllegalArgumentException e) {

			System.out.println(
					"[PASS] "
							+ testName
							+ " → "
							+ e.getMessage()
			);

			return;
		}

		throw new IllegalStateException(
				"[FAIL] "
						+ testName
						+ " → 예외가 발생하지 않았습니다."
		);
	}


	/*
	 * BinaryContentService.find()는
	 * 데이터가 없으면 예외를 발생시키므로
	 * 존재 여부 확인용 메서드
	 */
	private static boolean binaryContentExists(
			BinaryContentService binaryContentService,
			UUID id
	) {

		try {

			binaryContentService.find(id);

			return true;

		} catch (IllegalArgumentException e) {

			return false;
		}
	}
}