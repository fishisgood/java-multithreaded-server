
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;

public class MultiThreadedServer {
    public static void main(String[] args) throws IOException {

        List<Client> clientState = Collections.synchronizedList(new ArrayList<>());
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(4444)) {
                System.out.println("Jokes Server listening on port 4444");
                while (true) {
                    Socket clientSocket = serverSocket.accept(); 
                    System.out.println("New jokes client: " + clientSocket.getPort());
                    ClientHandler clientHandler = new ClientHandler(clientSocket, "KKP", clientState);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) { e.printStackTrace(); }
        }).start();

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(4445)) {
                System.out.println("Ruppin Server listening on port 4445");
                while (true) {
                    Socket clientSocket = serverSocket.accept(); 
                    System.out.println("New Ruppin client: " + clientSocket.getPort());
                    ClientHandler clientHandler = new ClientHandler(clientSocket, "RUPPIN", clientState);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
        

    }
}
