package org.example.service;

import org.example.dto.MessageResponseDTO;
import org.example.kafka.MessageProducer;
import org.example.model.Chatroom;
import org.example.model.Emoji;
import org.example.model.Message;
import org.example.repository.ChatroomRepository;
import org.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageService {

    @Value("${storage.image-dir}")
    private String imageDir;

    @Value("${storage.video-dir}")
    private String videoDir;

    private final MessageRepository messageRepository;
    private final ChatroomRepository chatroomRepository;
    private final MessageProducer messageProducer;

    public MessageService(MessageRepository msgRepo, ChatroomRepository roomRepo, MessageProducer producer) {
        this.messageRepository = msgRepo;
        this.chatroomRepository = roomRepo;
        this.messageProducer = producer;
    }

    public Message sendMessage(String content, MultipartFile file, Emoji emoji, Long chatroomId) throws IOException {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found"));

        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setEmoji(emoji);
        message.setChatroom(chatroom);

        // Handle file upload if present
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("File too large");
            }

            String mediaType = file.getContentType().contains("image") ? "image" : "video";
            String storageDir = mediaType.equals("image") ? imageDir : videoDir;

            String baseDir = new File("").getAbsolutePath();
            Path targetDir = Paths.get(baseDir, storageDir);

            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            String generatedFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = targetDir.resolve(generatedFilename);

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            message.setMediaType(mediaType);
            message.setMediaPath(targetPath.toString());
        }

        Message saved = messageRepository.save(message);
        messageProducer.sendMessage("Message ID " + saved.getId() + " stored with content: " + saved.getContent());

        return saved;
    }

    public Page<MessageResponseDTO> getMessagesByChatroom(Long chatroomId, int page, int size) {
        Page<Message> messages = messageRepository.findByChatroomId(chatroomId, PageRequest.of(page, size));
        return messages.map(MessageResponseDTO::new);
    }
}