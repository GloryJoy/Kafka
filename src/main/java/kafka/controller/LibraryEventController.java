package kafka.controller;

import kafka.domain.LibraryEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/libraryevent")
public class LibraryEventController {

    @PostMapping()
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent){
        System.out.println("testing...");


        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
