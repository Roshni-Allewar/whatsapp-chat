package org.example.dto;

import lombok.Getter;
import org.example.model.Emoji;
import org.example.model.Message;

import java.time.LocalDateTime;
@Getter
public class MessageResponseDTO {

    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private String mediaType;
    private String mediaPath;
    private Emoji emoji;

    public MessageResponseDTO(Message msg) {
        this.id = msg.getId();
        this.content = msg.getContent();
        this.timestamp = msg.getTimestamp();
        this.mediaType = msg.getMediaType();
        this.mediaPath = msg.getMediaPath();
        this.emoji = msg.getEmoji();
    }
}