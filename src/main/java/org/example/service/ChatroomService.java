package org.example.service;

import org.example.dto.ChatroomResponseDTO;
import org.example.model.Chatroom;
import org.example.repository.ChatroomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    public ChatroomService(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    public List<ChatroomResponseDTO> listChatrooms() {
        return chatroomRepository.findAll()
                .stream()
                .map(ChatroomResponseDTO::new)
                .collect(Collectors.toList());
    }
    public ChatroomResponseDTO createChatroom(String name) {
        Chatroom room = new Chatroom();
        room.setName(name);
        Chatroom saved = chatroomRepository.save(room);
        return new ChatroomResponseDTO(saved);
    }
}