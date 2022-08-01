package application;

import com.sun.net.httpserver.HttpExchange;

import java.sql.SQLException;

public class HandlerOwner implements Handler{
    private final HttpExchange httpExchange;
    private final DBConnector dbConnector;

    public HandlerOwner(HttpExchange httpExchange, DBConnector dbConnector) {
        this.httpExchange = httpExchange;
        this.dbConnector = dbConnector;
    }

    private ProcessResult processGET(String[] request) throws SQLException {
        if (request.length == 2) {
            return new ProcessResult(200, dbConnector.runQuerySet("SELECT * FROM CAR WHERE personid='" + request[1] + "';").toString());
        }
        return new ProcessResult(204, "");
    }

    private ProcessResult processPOST(String[] request){
        if (request.length == 3) {
            dbConnector.runQuery("UPDATE CAR SET personid='" + request[2] + "' WHERE vin='" + request[1] + "' AND EXISTS (SELECT 1 FROM person WHERE id='" + request[2] + "') AND EXISTS (SELECT 1 FROM CAR WHERE vin='" + request[1] + "' AND personid='');");
            return new ProcessResult(200, "OK");
        }
        return new ProcessResult(204, "");
    }

    private ProcessResult processPUT(String[] request){
        if (request.length == 3) {
            dbConnector.runQuery("UPDATE CAR SET personid='" + request[2] + "' WHERE vin='" + request[1] + "' AND EXISTS (SELECT 1 FROM person WHERE id='" + request[2] + "');");
            return new ProcessResult(200, "OK");
        }
        return new ProcessResult(204, "");
    }

    private ProcessResult processDELETE(String[] request){
        if (request.length == 2) {
            dbConnector.runQuery("UPDATE CAR SET personid='' WHERE vin='" + request[1] + "';");
            return new ProcessResult(200, "OK");
        }
        return new ProcessResult(204, "");
    }

    public ProcessResult process() throws SQLException {
        String[] requests = httpExchange.getRequestURI().toString().split(":");

        return switch (httpExchange.getRequestMethod()) {
            case "GET" -> processGET(requests);
            case "POST" -> processPOST(requests);
            case "PUT" -> processPUT(requests);
            case "DELETE" -> processDELETE(requests);
            default -> new ProcessResult(405, "");
        };
    }

}
