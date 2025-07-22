package org.example.dto;

import lombok.Getter;
import org.example.model.Emoji;
@Getter
public class MessageRequestDTO {

    private String content;
    private Emoji emoji;
    private Long chatroomId;
    // MultipartFile handled separately in controller
}