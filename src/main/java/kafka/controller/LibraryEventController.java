package kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kafka.domain.LibraryEvent;
import kafka.domain.LibraryEventType;
import kafka.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/libraryevent")
@Slf4j
public class LibraryEventController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @GetMapping()
    public ResponseEntity<String> getHello(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hello");
    }

    @PostMapping()
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent){

        try {
            log.info("Before calling");
            libraryEvent.setLibraryEventType(LibraryEventType.NEW);
            libraryEventProducer.sendLibraryEvent(libraryEvent);
            log.info("After calling the send method");
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
