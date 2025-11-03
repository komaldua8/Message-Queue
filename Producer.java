import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Producer {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5050);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected as Producer. Type messages to send:");

            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    writer.println("QUIT");
                    break;
                }

                writer.println("SEND " + input);
                System.out.println(reader.readLine()); // Read ACK
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
