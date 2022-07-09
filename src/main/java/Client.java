import com.itextpdf.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
        final static private String word = "бизнес";

        public static void main(String[] args) throws IOException {
            try (
                    Socket socket = new Socket("localhost", 8989);
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream( )));
                    PrintWriter out = new PrintWriter(socket.getOutputStream( ), true)) {
                out.println(word);
                String string = input.readLine( );
                System.out.println(string);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

