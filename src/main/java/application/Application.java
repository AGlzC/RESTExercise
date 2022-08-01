package application;


import java.io.IOException;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws IOException, SQLException {

        Server server = new Server(8080, new PostgresConnector().getInstance());
        server.Start();
  }
}
