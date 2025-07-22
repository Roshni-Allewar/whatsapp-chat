package org.example.service;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dto.MessageResponseDTO;
import org.example.exception.ChatroomNotFoundException;
import org.example.exception.MessageNotFoundException;
import org.example.kafka.MessageProducer;
import org.example.model.Chatroom;
import org.example.model.Emoji;
import org.example.model.Message;
import org.example.repository.ChatroomRepository;
import org.example.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import org.springframework.mock.web.MockMultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatroomRepository chatroomRepository;

    @Mock
    private MessageProducer messageProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageService.imageDir = "test-images";
        messageService.videoDir = "test-videos";
    }

    @Test
    void testSendMessageWithoutFile() throws Exception {
        Chatroom chatroom = new Chatroom();
        chatroom.setId(1L);

        Message saved = new Message();
        saved.setId(1L);
        saved.setContent("Hello");

        when(chatroomRepository.findById(1L)).thenReturn(Optional.of(chatroom));
        when(messageRepository.save(any(Message.class))).thenReturn(saved);

        Message result = messageService.sendMessage("Hello", null, Emoji.THUMBUP, 1L);

        assertEquals("Hello", result.getContent());
        verify(messageProducer).sendMessage(anyString());
    }



    @Test
    void testSendMessage_FileTooLarge_ThrowsException() {
        Chatroom chatroom = new Chatroom();
        chatroom.setId(1L);

        byte[] largeContent = new byte[11 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile("file", "big.mp4", "video/mp4", largeContent);

        when(chatroomRepository.findById(1L)).thenReturn(Optional.of(chatroom));

        assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage("Big File", file, Emoji.LOVE, 1L));
    }

    @Test
    void testSendMessage_ChatroomNotFound() {
        when(chatroomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ChatroomNotFoundException.class, () ->
                messageService.sendMessage("X", null, Emoji.LOVE, 99L));
    }

    @Test
    void testReactToMessageSuccess() {
        Message message = new Message();
        message.setId(10L);

        when(messageRepository.findById(10L)).thenReturn(Optional.of(message));
        when(messageRepository.save(message)).thenReturn(message);

        Message updated = messageService.reactToMessage(10L, Emoji.CRYING);

        assertEquals(Emoji.CRYING, updated.getEmoji());
    }

    @Test
    void testReactToMessage_MessageNotFound() {
        when(messageRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () ->
                messageService.reactToMessage(100L, Emoji.THUMBUP));
    }

    @Test
    void testGetMessagesByChatroom() {
        Message msg = new Message();
        msg.setId(1L);
        msg.setContent("Hi");

        Page<Message> page = new PageImpl<>(Arrays.asList(msg));

        when(messageRepository.findByChatroomId(1L, PageRequest.of(0, 10))).thenReturn(page);

        Page<MessageResponseDTO> result = messageService.getMessagesByChatroom(1L, 0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals("Hi", result.getContent().get(0).getContent());
    }
}
