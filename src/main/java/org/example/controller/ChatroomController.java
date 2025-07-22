package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.dto.ChatroomResponseDTO;
import org.example.model.Chatroom;
import org.example.repository.ChatroomRepository;
import org.example.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatroomController {
@Autowired
    ChatroomRepository chatroomRepository;
    private final ChatroomService chatroomService;

    public ChatroomController(ChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }
    @Operation(summary = "Create a new chatroom")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatroomResponseDTO> createChatroom(@RequestParam String name) {
        ChatroomResponseDTO newRoom = chatroomService.createChatroom(name);
        return ResponseEntity.ok(newRoom);
    }

    @Operation(summary = "List all chatrooms")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatroomResponseDTO>> listChatrooms(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        List<ChatroomResponseDTO> chatrooms = chatroomService.listChatrooms(page,size);
        return ResponseEntity.ok(chatrooms);
    }

}