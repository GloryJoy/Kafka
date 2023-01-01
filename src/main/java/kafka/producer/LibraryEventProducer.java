package kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

@Component
@Slf4j
public class LibraryEventProducer {

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        Integer key = libraryEvent.getMessageId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        Header header = new RecordHeader("additional-info", "value of a header".getBytes());

//        Header header = new Header() {
//            @Override
//            public String key() {
//                return "additional-info";
//            }
//
//            @Override
//            public byte[] value() {
//                return "value of a header".getBytes();
//            }
//        }

        List<Header> headerList = List.of(header);

        ProducerRecord<Integer, String> producerRecord = new ProducerRecord("library-events",null, key, value, headerList);

        CompletableFuture<SendResult<Integer, String>> completableFuture =
                kafkaTemplate
//                        .sendDefault(key, value)
                        .send(producerRecord)
                        .handle((res, e) -> {

                            Optional<SendResult<Integer, String>> optionalSendResult = Optional.ofNullable(res);
                            optionalSendResult.ifPresent(sr -> log.info("Succesfully send message {}", sr));

                            Optional<Throwable> optionalThrowable = Optional.ofNullable(e);
                            optionalThrowable.ifPresent(throwable -> log.error("Error sending message {}", throwable.getMessage()));

                            return res;
                        });


//        CompletableFuture<SendResult<Integer, String>> completableFuture =
//                kafkaTemplate
//                        .sendDefault(key, value)
//                        .handle(new BiFunction<SendResult<Integer, String>, Throwable, SendResult<Integer, String>>() {
//                            @Override
//                            public SendResult<Integer, String> apply(SendResult<Integer, String> result, Throwable throwable) {
//                                Optional<SendResult<Integer, String>> optionalSendResult = Optional.ofNullable(result);
//                            optionalSendResult.ifPresent(sr -> log.info("Succesfully send message {}", sr));
//
//                            Optional<Throwable> optionalThrowable = Optional.ofNullable(throwable);
//                            optionalThrowable.ifPresent(t -> log.error("Error sending message {}", t.getMessage()));
//                                return result;
//                            }
//                        });

    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error sending the message and the exception message is {}", ex.getMessage());
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message successfully sent for the key {}, value {}, partition {}", key, value, result.getRecordMetadata().partition());
    }
}
