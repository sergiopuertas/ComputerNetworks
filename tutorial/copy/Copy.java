package es.udc.redes.tutorial.copy;
import java.io.*;

public class Copy {
    public static void main(String[] args) throws IOException {
        FileReader input = null;
        FileWriter output = null;
        try {
            input = new FileReader(args[0]);
            output = new FileWriter(args[1]);

            int c;
            while ((c = input.read()) != -1) {
                output.write(c);
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }
}
