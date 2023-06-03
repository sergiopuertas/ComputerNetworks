package es.udc.redes.tutorial.info;
import java.io.IOException;

import java.nio.file.*;
import java.text.SimpleDateFormat;

public class Info {

    public static void main(String[] args) {

        try{
            Path path1 = Paths.get(args[0]);
            Path realPath = path1.toRealPath();

            String fileName = realPath.toFile().toString();
            int index = fileName.lastIndexOf('.');
            String extension = fileName.substring(index + 1);

            String fileType = Files.probeContentType(realPath);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            if(!realPath.toFile().exists()){
                System.out.println("File doesn't exist");
                return;
            }
            System.out.println("File name: " + realPath.getFileName());
            System.out.println("Absolute path: " + realPath.toAbsolutePath());
            System.out.println("File size: " + realPath.toFile().length() + "Bytes");
            System.out.println("Last modification time : " + sdf.format(realPath.toFile().lastModified()));
            System.out.println("File type:" + fileType);
            System.out.println("File extension: "+ extension);

        }
        catch(IOException e){
            System.out.println("I/O Error");
        }

    }

}
