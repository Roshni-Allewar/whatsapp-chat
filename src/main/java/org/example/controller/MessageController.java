package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.dto.MessageResponseDTO;
import org.example.model.Emoji;
import org.example.model.Message;
import org.example.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    @Operation
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @RequestParam("content") String content,
            @RequestPart(value = "attachment", required = false) MultipartFile file,
            @RequestParam Emoji emoji,
            @RequestParam Long chatroomId) {

        try {
            Message message = messageService.sendMessage(content, file, emoji, chatroomId);
            return ResponseEntity.ok(new MessageResponseDTO(message));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get paginated messages in a chatroom")
    @GetMapping("/{chatroomId}")
    public ResponseEntity<Page<MessageResponseDTO>> getMessages(
            @PathVariable Long chatroomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<MessageResponseDTO> messages = messageService.getMessagesByChatroom(chatroomId, page, size);
        return ResponseEntity.ok(messages);
    }
    @Operation(summary = "React to a message with an emoji")
    @PostMapping("/{messageId}/react")
    public ResponseEntity<MessageResponseDTO> reactToMessage(
            @PathVariable Long messageId,
            @RequestParam Emoji emoji) {
        try {
            Message updated = messageService.reactToMessage(messageId, emoji);
            return ResponseEntity.ok(new MessageResponseDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}