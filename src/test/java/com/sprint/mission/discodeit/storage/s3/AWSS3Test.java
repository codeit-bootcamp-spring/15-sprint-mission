package com.sprint.mission.discodeit.storage.s3;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class AWSS3Test {
    private static final byte[] CONTENT = "S3 integration test".getBytes(StandardCharsets.UTF_8);

    private S3Client s3;
    private StaticCredentialsProvider credentials;
    private Region region;
    private String bucket;
    private String key;

    @BeforeEach
    void setUp() throws IOException {
        Path envFile = findProjectEnv();
        assumeTrue(envFile != null, "Project root .env is required");

        Properties settings = new Properties();
        try (var reader = Files.newBufferedReader(envFile)) {
            settings.load(reader);
        }
        assumeTrue(settings.stringPropertyNames().stream().anyMatch(name -> name.startsWith("AWS_S3_")),
            "Project root .env has no AWS S3 settings");

        String accessKey = requiredProperty(settings, "AWS_S3_ACCESS_KEY");
        String secretKey = requiredProperty(settings, "AWS_S3_SECRET_KEY");
        region = Region.of(requiredProperty(settings, "AWS_S3_REGION"));
        bucket = requiredProperty(settings, "AWS_S3_BUCKET");
        credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        s3 = S3Client.builder().region(region).credentialsProvider(credentials).build();
    }

    @AfterEach
    void tearDown() {
        if (s3 != null) {
            try (S3Client client = s3) {
                if (key != null) {
                    client.deleteObject(request -> request.bucket(bucket).key(key));
                }
            }
        }
    }

    @Test
    void uploadsObject() {
        PutObjectResponse response = uploadTestObject();

        assertTrue(response.sdkHttpResponse().isSuccessful());
        assertEquals((long) CONTENT.length, s3.headObject(request -> request.bucket(bucket).key(key)).contentLength());
    }

    @Test
    void downloadsObject() {
        uploadTestObject();

        assertArrayEquals(CONTENT, s3.getObjectAsBytes(downloadRequest()).asByteArray());
    }

    @Test
    void downloadsObjectUsingPresignedUrl() throws Exception {
        uploadTestObject();

        try (S3Presigner presigner = S3Presigner.builder().region(region).credentialsProvider(credentials).build()) {
            var presigned = presigner.presignGetObject(request -> request
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(downloadRequest()));
            assertTrue(presigned.isBrowserExecutable());

            HttpRequest request = HttpRequest.newBuilder(presigned.url().toURI())
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();
            HttpResponse<byte[]> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());

            assertEquals(200, response.statusCode());
            assertArrayEquals(CONTENT, response.body());
        }
    }

    private PutObjectResponse uploadTestObject() {
        String testKey = "aws-s3-test/" + UUID.randomUUID() + ".txt";
        PutObjectResponse response = s3.putObject(
            request -> request.bucket(bucket).key(testKey), RequestBody.fromBytes(CONTENT));
        key = testKey;
        return response;
    }

    private GetObjectRequest downloadRequest() {
        return GetObjectRequest.builder().bucket(bucket).key(key).build();
    }

    private static Path findProjectEnv() {
        for (Path directory = Path.of("").toAbsolutePath(); directory != null; directory = directory.getParent()) {
            if (Files.isRegularFile(directory.resolve("settings.gradle"))) {
                Path envFile = directory.resolve(".env");
                return Files.isRegularFile(envFile) ? envFile : null;
            }
        }
        return null;
    }

    private static String requiredProperty(Properties settings, String name) {
        String value = settings.getProperty(name, "").trim();
        if (value.isEmpty()) {
            fail("Missing or blank .env setting: " + name);
        }
        return value;
    }
}
