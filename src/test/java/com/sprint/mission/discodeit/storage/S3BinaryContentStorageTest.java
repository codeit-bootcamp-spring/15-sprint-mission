package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileLoadFailedException;
import com.sprint.mission.discodeit.exception.FileSaveFailedException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class S3BinaryContentStorageTest {

    private static final String BUCKET = "discodeit-storage-test";
    private static final Duration EXPIRATION = Duration.ofSeconds(300);

    private final UUID id = UUID.randomUUID();
    private final String objectKey = "26/11/22/" + id;
    private final byte[] content = "attachment contents".getBytes(StandardCharsets.UTF_8);
    private final S3Client client = mock(S3Client.class);
    private final BinaryContentRepository repository = mock(BinaryContentRepository.class);
    private final S3BinaryContentStorage storage =
            new S3BinaryContentStorage(repository, client, mock(S3Presigner.class), BUCKET, EXPIRATION);

    @BeforeEach
    void usePersistedCreationDate() {
        BinaryContent binaryContent = mock(BinaryContent.class);
        when(binaryContent.getCreatedAt()).thenReturn(Instant.parse("2026-11-21T15:00:00Z"));
        when(repository.findById(id)).thenReturn(Optional.of(binaryContent));
    }

    @Test
    void uploadsBytesUsingSeoulDateAndUuidAsKey() throws Exception {
        assertEquals(id, storage.put(id, content));

        ArgumentCaptor<RequestBody> body = ArgumentCaptor.forClass(RequestBody.class);
        verify(client).putObject(eq(PutObjectRequest.builder().bucket(BUCKET).key(objectKey).build()),
                body.capture());
        try (InputStream stream = body.getValue().contentStreamProvider().newStream()) {
            assertArrayEquals(content, stream.readAllBytes());
        }
    }

    @Test
    void readsBytesUsingPersistedDateAndUuidAsKey() throws Exception {
        when(client.getObject(GetObjectRequest.builder().bucket(BUCKET).key(objectKey).build()))
                .thenReturn(new ResponseInputStream<>(GetObjectResponse.builder().build(),
                        new ByteArrayInputStream(content)));

        try (InputStream stream = storage.get(id)) {
            assertArrayEquals(content, stream.readAllBytes());
        }
    }

    @Test
    void translatesS3ExceptionsToStorageExceptions() {
        var failure = S3Exception.builder().statusCode(403).message("Access denied").build();
        when(client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenThrow(failure);
        when(client.getObject(any(GetObjectRequest.class))).thenThrow(failure);

        assertEquals(failure, assertThrows(FileSaveFailedException.class, () -> storage.put(id, content)).getCause());
        assertEquals(failure, assertThrows(FileLoadFailedException.class, () -> storage.get(id)).getCause());
    }

    @Test
    void redirectsToPresignedUrlWithDownloadHeadersAndConfiguredExpiration() {
        var credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create("test-key", "test-secret"));
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2).credentialsProvider(credentials).build()) {
            var signingStorage = new S3BinaryContentStorage(repository, client, presigner, BUCKET, EXPIRATION);
            String filename = "첨부 파일.txt";
            var response = signingStorage.download(new BinaryContentDto(id, filename, (long) content.length, "text/plain"));

            assertEquals(HttpStatus.FOUND, response.getStatusCode());
            assertNull(response.getBody());
            var location = response.getHeaders().getLocation();
            assertThat(location).isNotNull();
            assertEquals("https", location.getScheme());
            assertEquals("/" + objectKey, location.getPath());
            assertThat(location.getQuery().split("&")).contains(
                    "X-Amz-Expires=300",
                    "response-content-type=text/plain",
                    "response-content-disposition=" + ContentDisposition.attachment()
                            .filename(filename, StandardCharsets.UTF_8).build());
            assertThat(location.getQuery()).contains("X-Amz-Signature=");
        }
    }

    @Test
    void registersS3StorageOnlyWhenSelected() {
        var contextRunner = new ApplicationContextRunner()
                .withBean(BinaryContentRepository.class, () -> repository)
                .withUserConfiguration(S3BinaryContentStorage.class);

        contextRunner.withPropertyValues("discodeit.storage.type=local")
                .run(context -> assertThat(context).doesNotHaveBean(S3BinaryContentStorage.class));

        contextRunner.withPropertyValues(
                        "discodeit.storage.type=s3",
                        "discodeit.storage.s3.access-key=test-key",
                        "discodeit.storage.s3.secret-key=test-secret",
                        "discodeit.storage.s3.region=ap-northeast-2",
                        "discodeit.storage.s3.bucket=" + BUCKET,
                        "discodeit.storage.s3.presigned-url-expiration=600")
                .run(context -> assertThat(context).hasSingleBean(BinaryContentStorage.class));
    }
}
