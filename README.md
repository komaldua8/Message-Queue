# 📨 Message Broker System (Java)

A multi-stage project implementing a lightweight **Message Queue & Broker System** in Java using sockets, threads, and acknowledgments.

---

## 🧭 Milestones & Deliverables

### ✅ Milestone 1: In-Memory Queue (Local Only)

* **Deliverable:** A standalone Java class implementing a thread-safe in-memory queue.
* **Features:**

  * Multiple producer/consumer threads
  * Uses `BlockingQueue` for synchronization
  * Logs message flow

---

### ✅ Milestone 2: Message Broker Server

* **Deliverable:** A TCP socket server that handles producer and consumer connections.
* **Protocol:**

  * `SEND <message>` → from producer
  * `RECEIVE` → from consumer
* **Features:**

  * Multi-client support via `ExecutorService`
  * Routes queued messages between producers and consumers

---


### ✅ Milestone 3: Message Acknowledgment & Retry

* **Deliverable:** Extended protocol with message acknowledgment.
* **Features:**

  * Broker waits for `ACK` from consumers
  * Messages requeued if ACK not received within 5 seconds
  * Fault-tolerant delivery mechanism

---

## 🚀 How to Run

1. **Compile all Java files**

   ```bash
   mvn clean compile
   mvn clean package
   ```

2. **Run the JAR file**

   ```bash
   java -jar target/message-broker-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

3. **Run the broker server**

   ```bash
   cd message-broker
   mvn -q exec:java -Dexec.mainClass="com.messagebroker.server.Producer"
   SEND hello
   ```

   Server starts on port **5050**.

4. **Test: Open separate Terminals for Producer and Consumer**

   * **Producer:**

     ```
     cd message-broker
     mvn -q exec:java -Dexec.mainClass="com.messagebroker.server.Producer"
     SEND hello
     ```
   * **Consumer:**

     ```
     cd message-broker
     mvn -q exec:java -Dexec.mainClass="com.messagebroker.server.Consumer"
     ```

---

## 🧰 Tech Stack

* Programming Language: Java
* Build & Dependency Management: Apache Maven ->Handles project structure (pom.xml)
* Networking & Communication: TCP Sockets
* Concurrency: Executor Service/ Thread Pool-> Supports async processing & ACK timeout recovery
* Data Structures: Custom Message Queue, Concurrent Hashmap
* Packaging: Maven Assembly Plugin-> builds JAR and adds class manifest

---

