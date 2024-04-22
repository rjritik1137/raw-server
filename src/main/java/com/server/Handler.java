package com.server;

import java.io.*;
import java.net.Socket;

public class Handler {
    BufferedReader reader = null;
    PrintWriter writer = null;
    Socket clientSocket;
    Handler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
         reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
         writer.println("Client connected!" + clientSocket.getInetAddress().getHostAddress() + " " + clientSocket.getPort());
    }
    public boolean serveRequest() {
        writer.println("Client connected!" + clientSocket.getInetAddress().getHostAddress() + " " + clientSocket.getPort());
        try {
            String message = reader.readLine();
            if (message == null) {
                reader.close();
                writer.close();
                return false;
            }
            System.out.println("Received from client: " + message);
            writer.println("com.server.Hello from the server!");
        } catch (IOException e) {
            //
            return false;
        }
        return true;
    }

    public boolean rejectRequest() {
        writer.println("request rejected");
        return true;
    }
}
