import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args) {
        final int serverPort = 8080;

        try (
                // Create a server socket that listens on the specified port
                ServerSocket serverSocket = new ServerSocket(serverPort);
        ) {
            System.out.println("Server is listening on port " + serverPort);

            // Wait for incoming connections
            while (true) {
                // Accept the client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                // Create streams for reading from and writing to the client socket
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

                    // Read the message from the client
                while(true) {
                    try {
                        String message = reader.readLine();
                        if (message == null) {
                            reader.close();
                            writer.close();
                            break;
                        }
                        System.out.println("Received from client: " + message);
                        // Send a response back to the client
                        writer.println("Hello from the server!");
                    } catch (IOException e) {
                        reader.close();
                        writer.close();
                    }

                }

            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
