package kafka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Header;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LibraryEvent {
    private String libraryEventId;
    private Integer messageId;
//    private Header header;
    private LibraryEventType libraryEventType;
    private Book book;

}
