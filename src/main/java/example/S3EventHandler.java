package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class S3EventHandler implements RequestHandler<S3Event, Object> {

  @Override
  public Object handleRequest(S3Event s3event, Context context) {
    log.info("Running S3EventHandler::handleRequest");
    S3EventNotification.S3EventNotificationRecord record = s3event.getRecords().get(0);
    String bucketName = record.getS3().getBucket().getName();
    String fileKey = record.getS3().getObject().getUrlDecodedKey();

    AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    try (final S3Object s3Object = s3Client.getObject(bucketName, fileKey);
         final InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8);
         final BufferedReader reader = new BufferedReader(streamReader)) {
      log.info("File metadata: {}", new ObjectMapper().writeValueAsString(record.getS3().getObject()));
      log.info("File content: ");
      reader.lines()
          .forEach(line -> System.out.println("Line: " + line));
    } catch (final IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }

    System.out.println("Finished processing file");
    return "Ok";
  }
}