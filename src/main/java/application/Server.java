package application;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Server {
    private final HttpServer server;

    private static DBConnector dbConnector;
    public Server(int port, DBConnector dbConnector) throws IOException {
        Server.dbConnector = dbConnector;

        server = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(httpExchange -> {
            try {
                handleRequest(httpExchange);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void Start() {
        server.start();
    }


    public void Stop() { server.stop(1);}

    private static void handleResponse(HttpExchange httpExchange, ProcessResult result) throws IOException {
        if (result.getResponseCode() == HttpURLConnection.HTTP_OK){
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.getResponseBody().getBytes().length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(result.getResponseBody().getBytes());
            outputStream.close();
        }
        else{
            httpExchange.sendResponseHeaders(result.getResponseCode(), -1);
        }
        httpExchange.close();
    }

    private static void handleRequest(HttpExchange httpExchange) throws IOException, SQLException {
        String requestString = httpExchange.getRequestURI().toString();
        ProcessResult processResult = new ProcessResult(405, "");
        if (requestString.startsWith("/api/people")){
            processResult = new HandlerPerson(httpExchange, dbConnector).process();
        } else if (requestString.startsWith("/api/cars")){
            processResult = new HandlerCar(httpExchange, dbConnector).process();
        } else if (requestString.startsWith("/api/owner")){
            processResult = new HandlerOwner(httpExchange, dbConnector).process();
        } else if (requestString.startsWith("/api/test")) {
            dbConnector.runQuerySet("SELECT * FROM PERSON WHERE id='001';");
        }
        handleResponse(httpExchange, processResult);
    }
}
