package application;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class HandlerCar implements Handler{
    private final HttpExchange httpExchange;
    private final DBConnector dbConnector;

    public HandlerCar(HttpExchange httpExchange, DBConnector dbConnector) {
        this.httpExchange = httpExchange;
        this.dbConnector = dbConnector;
    }

    private ProcessResult processGET(String[] request) throws SQLException {
        if (request.length > 1) {
            return new ProcessResult(200, dbConnector.runQuerySet("SELECT * FROM CAR WHERE vin='" + request[1] + "';").toString());
        } else {
            return new ProcessResult(200, dbConnector.runQuerySet("SELECT * FROM CAR;").toString());
        }
    }

    private ProcessResult processPOST(String body){
        JSONObject jsonObject = new JSONObject(body);
        dbConnector.runQuery(new Car(jsonObject.getString("vin"), "", jsonObject.getString("brand"), jsonObject.getString("model"), jsonObject.getInt("year"), jsonObject.getString("color")).toInsert());

        return new ProcessResult(200, "OK");
    }

    private ProcessResult processPUT(String body){
        JSONObject jsonObject = new JSONObject(body);
        dbConnector.runQuery(new Car(jsonObject.getString("vin"), "", jsonObject.getString("brand"), jsonObject.getString("model"), jsonObject.getInt("year"), jsonObject.getString("color")).toUpdate(false));
        return new ProcessResult(200, "OK");
    }

    private ProcessResult processDELETE(String[] request){
        if (request.length > 1) {
            dbConnector.runQuery("DELETE FROM CAR WHERE vin='" + request[1] + "';");
            return new ProcessResult(200, "OK");
        }
        return new ProcessResult(204, "");
    }

    public ProcessResult process() throws IOException, SQLException {
        String requestString = httpExchange.getRequestURI().toString();
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = httpExchange.getRequestBody();

        int character;
        while ((character = inputStream.read()) != -1) {
            stringBuilder.append((char) character);
        }

        return switch (httpExchange.getRequestMethod()) {
            case "GET" -> processGET(requestString.split(":"));
            case "POST" -> processPOST(stringBuilder.toString());
            case "PUT" -> processPUT(stringBuilder.toString());
            case "DELETE" -> processDELETE(requestString.split(":"));
            default -> new ProcessResult(405, "");
        };
    }

}
