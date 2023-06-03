package es.udc.redes.webserver;

import java.io.*;
import java.net.*;


public class WebServer
{
    public static void main(String[] argv) throws IOException {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
            System.exit(-1);
        }
        // Create a sesrver socket
        ServerSocket servSocket = new ServerSocket(Integer.parseInt(argv[0]));
        try {
            // Set a timeout of 300 seconds
            servSocket.setSoTimeout(300000);
            while (true) {
                // Wait for connection
                Socket socket = servSocket.accept();
                // Create a ServerThread object, with the new connection as parameter
                ServerThread servThread = new ServerThread(socket);
                // Initiate thread using the start() method
                servThread.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            //Close the socket
            servSocket.close();
        }
    }
}