import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageBrokerServer {
    private static final int PORT = 5050;
    private static final int MAX_CLIENTS = 10;
    private final MessageQueue messageQueue = new MessageQueue();
    private final ExecutorService clientPool = Executors.newFixedThreadPool(MAX_CLIENTS);

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Message Broker Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientPool.execute(new ClientHandler(clientSocket, messageQueue));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class for handling each connected client
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private MessageQueue messageQueue;

        public ClientHandler(Socket socket, MessageQueue messageQueue) {
            this.socket = socket;
            this.messageQueue = messageQueue;
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
                        String payload = line.substring(5); // Extract message after "SEND "
                        Message message = new Message();
                        message.setPayload(payload);
                        messageQueue.enqueue(message);
                        writer.println("ACK: Message received");
                        System.out.println("Received from producer: " + payload);
                    } 
                    else if (line.equals("RECEIVE")) {
                        Message message = messageQueue.dequeue();
                        writer.println("MESSAGE: " + message.getPayload());
                        System.out.println("Delivered to consumer: " + message.getPayload());
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
                System.out.println("Client disconnected.");
            }
        }
    }

    public static void main(String[] args) {
        new MessageBrokerServer().start();
    }
}
