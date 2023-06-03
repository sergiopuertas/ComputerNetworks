package es.udc.redes.tutorial.tcp.server;
import java.io.IOException;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

  public static void main(String[] argv) throws IOException {
    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
      System.exit(-1);
    }
    // Create a server socket
    ServerSocket servSocket = new ServerSocket(5001);
    try {
      // Set a timeout of 300 secs
      servSocket.setSoTimeout(300000);
      while (true) {
        // Wait for connections
        Socket socket = servSocket.accept();

        // Create a ServerThread object, with the new connection as parameter
        ServerThread servThread = new ServerThread(socket);
        // Initiate thread using the start() method
        servThread.start();
      }
    // Uncomment next catch clause after implementing the logic
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
