import java.io.*;
import java.net.*;

public class Consumer{
    public static void main(String[] args) {
        String serverHost = "localhost";
        int port = 5050;

        try (Socket socket = new Socket(serverHost, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected as Consumer to " + serverHost + ":" + port);

            while (true) {
                writer.println("RECEIVE");
                String response = reader.readLine();

                if (response != null && response.startsWith("MESSAGE")) {
                    String message = response.substring(9); // after "MESSAGE: "
                    System.out.println("Received: " + message);

                    // Simulate processing and send ACK
                    Thread.sleep(2000); // simulate work
                    writer.println("ACK");
                    System.out.println("ACK sent for: " + message);
                }

                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
