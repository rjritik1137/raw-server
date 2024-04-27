package com.client;

public class TcpClientTest {
    public static void main(String[] args) {
        for(int i=0;i<10;i++) {
            new Thread(()-> {
                TcpClient client = new TcpClient();
                client.call();
            }).start();
        }
    }
}
