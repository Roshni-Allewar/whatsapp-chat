package org.example.exception;



public class ChatroomNotFoundException extends RuntimeException {
    public ChatroomNotFoundException(Long chatroomId) {
        super("Chatroom not found with ID: " + chatroomId);
    }
}

