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
   javac *.java
   ```

2. **Run the broker server**

   ```bash
   java MessageBrokerServer
   ```

   Server starts on port **5050**.

3. **Test**

   * **Producer:**

     ```
     java Producer
     SEND Hello
     ```
   * **Consumer:**

     ```
     java Consumer
     RECEIVE
     ```

---

## 🧰 Tech Stack

* `java.net` (Sockets)
* `ExecutorService`
* `BlockingQueue`

---

