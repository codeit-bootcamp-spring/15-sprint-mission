package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.exception.BinaryNotFoundException;
import com.sprint.mission.discodeit.exception.FileLoadFailedException;
import com.sprint.mission.discodeit.exception.FileSaveFailedException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PreDestroy;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private static final DateTimeFormatter DATE_PREFIX =
            DateTimeFormatter.ofPattern("uu/MM/dd", Locale.ROOT).withZone(ZoneId.of("Asia/Seoul"));

    private final BinaryContentRepository binaryContentRepository;
    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final String bucket;
    private final Duration presignedUrlExpiration;

    @Autowired
    public S3BinaryContentStorage(
            BinaryContentRepository binaryContentRepository,
            @Value("${discodeit.storage.s3.access-key}") String accessKey,
            @Value("${discodeit.storage.s3.secret-key}") String secretKey,
            @Value("${discodeit.storage.s3.region}") String region,
            @Value("${discodeit.storage.s3.bucket}") String bucket,
            @Value("${discodeit.storage.s3.presigned-url-expiration}") long expirationSeconds) {
        Assert.hasText(bucket, "AWS_S3_BUCKET is required");
        Assert.isTrue(expirationSeconds > 0 && expirationSeconds <= Duration.ofDays(7).toSeconds(),
                "AWS_S3_PRESIGNED_URL_EXPIRATION must be between 1 and 604800 seconds");
        StaticCredentialsProvider credentials =
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        Region s3Region = Region.of(region);
        this.binaryContentRepository = binaryContentRepository;
        this.bucket = bucket;
        this.presignedUrlExpiration = Duration.ofSeconds(expirationSeconds);
        this.s3Client = S3Client.builder().region(s3Region).credentialsProvider(credentials).build();
        this.presigner = S3Presigner.builder().region(s3Region).credentialsProvider(credentials).build();
    }

    S3BinaryContentStorage(BinaryContentRepository binaryContentRepository, S3Client s3Client,
                           S3Presigner presigner, String bucket, Duration expiration) {
        this.binaryContentRepository = binaryContentRepository;
        this.s3Client = s3Client;
        this.presigner = presigner;
        this.bucket = bucket;
        this.presignedUrlExpiration = expiration;
    }

    @Override
    public UUID put(UUID uuid, byte[] bytes) {
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(objectKey(uuid)).build();
        try {
            s3Client.putObject(request, RequestBody.fromBytes(bytes));
            return uuid;
        } catch (SdkException e) {
            throw new FileSaveFailedException(e, uuid);
        }
    }

    @Override
    public InputStream get(UUID uuid) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucket).key(objectKey(uuid)).build();
        try {
            return s3Client.getObject(request);
        } catch (SdkException e) {
            throw new FileLoadFailedException(e, uuid);
        }
    }

    @Override
    public ResponseEntity<Void> download(BinaryContentDto binaryContent) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey(binaryContent.id()))
                .responseContentType(binaryContent.contentType())
                .responseContentDisposition(ContentDisposition.attachment()
                        .filename(binaryContent.fileName(), StandardCharsets.UTF_8)
                        .build().toString())
                .build();
        try {
            URI location = URI.create(presigner.presignGetObject(presign -> presign
                    .signatureDuration(presignedUrlExpiration)
                    .getObjectRequest(request)).url().toExternalForm());
            return ResponseEntity.status(HttpStatus.FOUND).location(location).build();
        } catch (SdkException e) {
            throw new FileLoadFailedException(e, binaryContent.id());
        }
    }

    private String objectKey(UUID id) {
        var binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new BinaryNotFoundException(id));
        return DATE_PREFIX.format(binaryContent.getCreatedAt()) + "/" + id;
    }

    @PreDestroy
    void close() {
        try (S3Client client = s3Client; S3Presigner urlPresigner = presigner) {
            // The SDK clients are shared for the lifetime of this bean.
        }
    }
}
