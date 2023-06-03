package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String[] argv) throws IOException {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket serverSocket=new ServerSocket(5001);
        try {
            // Set maximum timeout to 300 secs
            serverSocket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                Socket recSocket = serverSocket.accept();
                // Set the input channel
                BufferedReader is = new BufferedReader(new InputStreamReader(recSocket.getInputStream()));
                // Set the output channel
                PrintWriter out = new PrintWriter(recSocket.getOutputStream(), true);
                // Receive the client message
                String line;
                line = is.readLine();
                System.out.println("SERVER: Received "+line+"from "+recSocket.getInetAddress());
                // Send response to the client
                System.out.println("SERVER: Sending "+line+"to "+recSocket.getInetAddress());
                out.println(line);
                // Close the streams
                is.close();
                out.close();
            }
        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
