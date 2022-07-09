import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Main {

    final static private int port = 8989;
    final static private String path = "pcs-jd-diplom_nd705/pdfs";
    final static GsonBuilder builder = new GsonBuilder( );

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File(path));
        //System.out.println(engine.search("бизнес"));

        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            String input;
            while (true) {
                try (
                        Socket socket = serverSocket.accept( );
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream( )));
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream( ));
                ) {
                    // обработка одного подключения
                    input = bufferedReader.readLine( );
                    if (input == null) {
                        break;
                    }

                    List<PageEntry> list = engine.search(input);
                    Gson gson = builder.create( );
                    Type listType = new TypeToken<List<PageEntry>>( ) {
                        }.getType( );
                    String json = gson.toJson(list, listType);
                    printWriter.print(json);

                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace( );
        }
    }


}
