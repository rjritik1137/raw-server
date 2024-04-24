package com.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {
    public void call() {
        final String serverAddress = "localhost";
        final int serverPort = 8080;

        try (
                // Create a socket connection to the server
                Socket socket = new Socket(serverAddress, serverPort);
                // Create streams for reading from and writing to the socket
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        ) {
            // Send a message to the server
            while(true) {
                writer.println("Hello from the client!");
                // Read the server's response
                String response = reader.readLine();
                System.out.println("Server response: " + response);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverAddress);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}