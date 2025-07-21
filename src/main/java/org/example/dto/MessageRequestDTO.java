package org.example.dto;

import org.example.model.Emoji;

public class MessageRequestDTO {

    private String content;
    private Emoji emoji;
    private Long chatroomId;
    // MultipartFile handled separately in controller
}