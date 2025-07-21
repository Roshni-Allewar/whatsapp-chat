package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime timestamp;

    private String mediaType; // "image" or "video"
    private String mediaPath; // local storage path

    @Enumerated(EnumType.STRING)
    private Emoji emoji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;
}
