import java.io.*;
import java.net.*;

public class Consumer {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5050);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected as Consumer. Waiting for messages...");

            while (true) {
                writer.println("RECEIVE");
                String response = reader.readLine();
                if (response != null && response.startsWith("MESSAGE")) {
                    System.out.println("Received: " + response.substring(9)); 
                }
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
