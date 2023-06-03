package es.udc.redes.tutorial.tcp.server;
import java.net.*;
import java.io.*;

/** Thread that processes an echo server connection. */

public class ServerThread extends Thread {

  private Socket socket;

  public ServerThread(Socket s) {
   this.socket = s;
  }

  public void run() {
    try {
      BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      // Set the output channel
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      // Receive the client message
      String line;
      line = is.readLine();
      System.out.println("SERVER: Received "+line+"from "+socket.getInetAddress());
      // Send response to the client
      System.out.println("SERVER: Sending "+line+"to "+socket.getInetAddress());
      out.println(line);
      // Close the streams
      is.close();
      out.close();
    // Uncomment next catch clause after implementing the logic
    } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      } finally {
      try {
        socket.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    }
  }

