package es.udc.redes.webserver;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ServerThread extends Thread {
    private final Socket socket;

    public ServerThread(Socket s) {
        this.socket = s;
    }
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            String ifmodTime = "";
            String request = in.readLine();
            String header = in.readLine();
            boolean ifmodifiedsince = false;

            if(request!=null) {//to avoid nullpointer exception

                while (header != null && !header.equals("")) {
                    if (header.startsWith("If-Modified-Since: ")) {
                        ifmodTime = header.split(": ")[1];
                        ifmodifiedsince = true;
                    }
                    header = in.readLine();
                }

                boolean get = request.startsWith("GET");
                boolean head = request.startsWith("HEAD");
                boolean ifmod = true;
                String response;

                if (get || head) {
                    String[] parts = request.split(" ");
                    String fullUrl = "p1-files".concat(File.separator).concat(parts[1]);
                    File file = new File(fullUrl);
                    if (file.exists() && !file.isDirectory()) {
                        if (ifmodifiedsince && !wasModified(ifmodTime, file)) {
                            ifmod = false;
                            response = "HTTP/1.0 304 OK\r\n";
                        } else {
                            response = "HTTP/1.0 200 OK\r\n";
                        }
                        processAndWrite(file, response, out);
                        if (!head && ifmod) {
                            printBody(file, out);
                        }
                    }
                    else {
                        response = "HTTP/1.0 404 Not Found\r\n";
                        File errorFile = new File("p1-files".concat(File.separator).concat("error404.html"));
                        processAndWrite(errorFile, response, out);
                        if (!head) {
                            printBody(errorFile, out);
                        }
                    }
                }
                else {
                    response = "HTTP/1.0 400 Bad Request\r\n";
                    File errorFile = new File("p1-files".concat(File.separator).concat("error400.html"));
                    processAndWrite(errorFile, response, out);
                    printBody(errorFile, out);
                }
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    static void processAndWrite(File file, String response, OutputStream out) throws IOException, ParseException {
        response = createHeader(response, file);
        out.write(response.getBytes());
    }
    static void printBody(File file, OutputStream out) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }
    static boolean wasModified(String ifmodTime, File file) throws ParseException {
        if (ifmodTime.equals("")) return false;
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Instant instant = Instant.ofEpochMilli(file.lastModified());
        String isoDate = instant.toString();

        Date  ifModifiedSince= formatoFecha.parse(ifmodTime);
        Date  lastMod= formatoFecha.parse(isoDate);
        return ifModifiedSince.after(lastMod);
    }
    static String createHeader(String response, File file) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String datetime = localDateTime.format(formatter);
        Path path = Path.of(file.getAbsolutePath());

        Instant instant = Instant.ofEpochMilli(file.lastModified());
        String isoDate = instant.toString();

        response += "Date: " + datetime + "\r\n";
        response += "Server: Apache/1.3.0 (Unix)\r\n";
        response += "Last-Modified: " + isoDate + "\r\n";
        response += "Content-Length: " + file.length() + "\r\n";
        response += "Content-Type: " + Files.probeContentType(path) + "\r\n";
        response += "\r\n";
        return response;
    }
}