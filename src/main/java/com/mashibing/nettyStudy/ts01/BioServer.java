package com.mashibing.nettyStudy.ts01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

    public static void main(String[] args) {
        try {
            ServerSocket ss=new ServerSocket();
            ss.bind(new InetSocketAddress(8888));

            Socket s=ss.accept();
            System.out.println("a client connect");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
