# 💬 Chat Application API
# Check the master branch
This is a Spring Boot RESTful API for a chat application. It supports sending messages, attaching media files, emoji reactions, and Kafka-based message streaming. The app is documented with Swagger UI.

---

## 🚀 Features

- Send messages with optional emoji and media attachment (image/video)
- Emoji reactions to existing messages
- Paginated message retrieval per chatroom
- In-memory H2 database for development
- File upload support (with size limits)
- Kafka producer integration
- Swagger UI for API testing

---

## 🔧 Tech Stack

- Spring Boot 3
- Spring Data JPA
- H2 Database (in-memory)
- Kafka (for async messaging)
- Swagger (via Springdoc OpenAPI)
- JUnit 5 & Mockito (for testing)

---

## 🛠️ Running Locally

### 📦 Prerequisites

- Java 17+
- Kafka (optional but needed for full functionality)
- Maven or Gradle

### ▶️ Steps

```bash
# Clone the repo
git clone https://github.com/your-username/chat-app-api.git
cd chat-app-api

# Build and run
./mvnw spring-boot:run
