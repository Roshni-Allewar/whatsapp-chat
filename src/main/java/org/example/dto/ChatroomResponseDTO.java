package org.example.dto;

import lombok.Getter;
import org.example.model.Chatroom;
@Getter
public class ChatroomResponseDTO {
    private Long id;
    private String name;

    public ChatroomResponseDTO(Chatroom room) {
        this.id = room.getId();
        this.name = room.getName();
    }
}