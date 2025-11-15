package com.messagebroker.server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import com.messagebroker.queue.MessageQueue;
import com.messagebroker.model.Message;

public class MessageBrokerServer {
    private static final int PORT = 5050;
    private static final int MAX_CLIENTS = 10;
    private final MessageQueue messageQueue = new MessageQueue();
    private final ExecutorService clientPool = Executors.newFixedThreadPool(MAX_CLIENTS);
    private final Map<Socket, PendingMessage> pendingAcks = new ConcurrentHashMap<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Message Broker Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientPool.execute(new ClientHandler(clientSocket, messageQueue, pendingAcks));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class PendingMessage {
        Message message;
        long timestamp;
        PendingMessage(Message msg, long time) {
            this.message = msg;
            this.timestamp = time;
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final MessageQueue messageQueue;
        private final Map<Socket, PendingMessage> pendingAcks;

        public ClientHandler(Socket socket, MessageQueue queue, Map<Socket, PendingMessage> pendingAcks) {
            this.socket = socket;
            this.messageQueue = queue;
            this.pendingAcks = pendingAcks;
        }

        @Override
        public void run() {
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("SEND")) {
                        String payload = line.substring(5);
                        Message message = new Message();
                        message.setPayload(payload);
                        messageQueue.enqueue(message);
                        writer.println("ACK: Message received by broker");
                        System.out.println("Received from producer: " + payload);
                    } 
                    else if (line.equals("RECEIVE")) {
                        Message message = messageQueue.dequeue();
                        writer.println("MESSAGE: " + message.getPayload());

                        pendingAcks.put(socket, new PendingMessage(message, System.currentTimeMillis()));
                        new Thread(() -> {
                            try {
                                Thread.sleep(5000); // 5 seconds timeout
                                PendingMessage pending = pendingAcks.get(socket);
                                if (pending != null && System.currentTimeMillis() - pending.timestamp >= 5000) {
                                    System.out.println("ACK timeout. Requeuing message: " + pending.message.getPayload());
                                    messageQueue.enqueue(pending.message);
                                    pendingAcks.remove(socket);
                                }
                            } catch (InterruptedException ignored) {}
                        }).start();
                    } 
                    else if (line.equals("ACK")) {
                        PendingMessage pending = pendingAcks.remove(socket);
                        if (pending != null) {
                            System.out.println("ACK received for message: " + pending.message.getPayload());
                        }
                    } 
                    else if (line.equalsIgnoreCase("QUIT")) {
                        writer.println("Goodbye!");
                        break;
                    } 
                    else {
                        writer.println("ERROR: Unknown command");
                    }
                }
            } catch (Exception e) {
                System.out.println("Client disconnected unexpectedly.");
                PendingMessage pending = pendingAcks.remove(socket);
                if (pending != null) {
                    System.out.println("Requeuing due to disconnect: " + pending.message.getPayload());
                    messageQueue.enqueue(pending.message);
                }
            }
        }
    }

    public static void main(String[] args) {
        new MessageBrokerServer().start();
    }
}
