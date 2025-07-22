package org.example.service;

import org.example.dto.ChatroomResponseDTO;
import org.example.model.Chatroom;
import org.example.repository.ChatroomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatroomServiceTest {

    private ChatroomRepository chatroomRepository;
    private ChatroomService chatroomService;

    @BeforeEach
    void setUp() {
        chatroomRepository = mock(ChatroomRepository.class);
        chatroomService = new ChatroomService(chatroomRepository);
    }

    @Test
    void testListChatrooms_withPagination() {
        // Given
        int page = 0;
        int size = 2;

        Chatroom room1 = new Chatroom();
        room1.setId(1L);
        room1.setName("General");

        Chatroom room2 = new Chatroom();
        room2.setId(2L);
        room2.setName("Random");

        Page<Chatroom> pagedResult = new PageImpl<>(Arrays.asList(room1, room2));

        when(chatroomRepository.findAll(PageRequest.of(page, size))).thenReturn(pagedResult);

        // When
        List<ChatroomResponseDTO> result = chatroomService.listChatrooms(page, size);

        // Then
        assertEquals(2, result.size());
        assertEquals("General", result.get(0).getName());
        assertEquals("Random", result.get(1).getName());

        verify(chatroomRepository, times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    void testCreateChatroom() {
        // Given
        String chatroomName = "Tech";
        Chatroom savedRoom = new Chatroom();
        savedRoom.setId(10L);
        savedRoom.setName(chatroomName);

        when(chatroomRepository.save(any(Chatroom.class))).thenReturn(savedRoom);

        // When
        ChatroomResponseDTO response = chatroomService.createChatroom(chatroomName);

        // Then
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Tech", response.getName());

        ArgumentCaptor<Chatroom> captor = ArgumentCaptor.forClass(Chatroom.class);
        verify(chatroomRepository).save(captor.capture());
        assertEquals(chatroomName, captor.getValue().getName());
    }
}
