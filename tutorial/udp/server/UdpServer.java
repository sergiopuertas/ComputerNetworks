package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {
    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");

            System.exit(-1);
        }
        DatagramSocket sDatagram=null;
        try {
            // Create a server socket
            sDatagram = new DatagramSocket(5000);
            // Set maximum timeout to 300 secs
            sDatagram.setSoTimeout(300000);
            byte[] buffer = new byte[1024];
            DatagramPacket dataReceived = new DatagramPacket(buffer, 1024);

            String input;
            while (true) {
                // Receive message
                sDatagram.receive(dataReceived);
                input = new String(dataReceived.getData(), 0, dataReceived.getLength());
                System.out.println("SERVER: Received "+input+"from server "+dataReceived.getAddress());
                // Send response
                sDatagram.send(dataReceived);
                System.out.println("SERVER: Sending "+input+"to "+dataReceived.getAddress());
            }
        // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
           System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            assert sDatagram != null;
            sDatagram.close();
        }
    }
}
