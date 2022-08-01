package application;

import org.json.JSONArray;

import java.sql.SQLException;
import java.util.List;

public interface DBConnector {
    boolean runQuery(String query);

    //List<List<String>> runQuerySet(String query);

    JSONArray runQuerySet(String set) throws SQLException;
}
