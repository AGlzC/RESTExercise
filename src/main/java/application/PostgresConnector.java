package application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PostgresConnector implements DBConnector{

    private static PostgresConnector postgresConnectorInstance = null;
    private Connection connection;

    PostgresConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "53cr3t");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    public PostgresConnector getInstance(){
        if (postgresConnectorInstance == null){
            synchronized (PostgresConnector.class){
                if (postgresConnectorInstance == null){
                    postgresConnectorInstance = new PostgresConnector();
                }
            }
        }
        return postgresConnectorInstance;
    }

    public void configureDefault() throws SQLException {
        Statement statement;

        try {
            statement = connection.createStatement();
            statement.addBatch("CREATE TABLE PERSON (ID VARCHAR(40) PRIMARY KEY, FIRSTNAME VARCHAR(30), LASTNAME VARCHAR(40), EMAIL VARCHAR(40), GENDER VARCHAR(15))");
            statement.addBatch("CREATE TABLE CAR (VIN VARCHAR(30) PRIMARY KEY, PERSONID VARCHAR(40), BRAND VARCHAR(30), MODEL VARCHAR(40), YEAR NUMERIC, COLOR VARCHAR(15))");
            statement.executeBatch();
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
/*
    public ArrayList<Person> runQueryPerson(String query){
        Statement statement;
        ResultSet resultSet;
        ArrayList <Person> list = new ArrayList<Person>();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Person(resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getString(5)));
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return list;
    }

    public ArrayList<Car> runQueryCar(String query){
        Statement statement;
        ResultSet resultSet;
        ArrayList <Car> list = new ArrayList<Car>();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Car(resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getInt(5),
                                    resultSet.getString(6)));
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return list;
    }
*/
    public boolean runQuery(String query){
        Statement statement;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return false;
        }
        return true;
    }
/*

    public List<List<String>> runQuerySetx(String query) {
        List<List<String>> rowList = new LinkedList<List<String>>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()){
                List<String> columnList = new LinkedList<String>();
                rowList.add(columnList);
                for (int column = 1; column <= columnCount; ++column)
                {
                    final Object value = resultSet.getObject(column);
                    columnList.add(String.valueOf(value));
                }
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return rowList;
    }

 */
    @Override
    public JSONArray runQuerySet(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int numCols = resultSetMetaData.getColumnCount();

        List<String> colNames = IntStream.range(0, numCols)
                .mapToObj(i -> {
                                try {
                                    return resultSetMetaData.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    return "?";
                                }}).collect(Collectors.toList());

        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            JSONObject jsonObjectRow = new JSONObject();
            colNames.forEach(columnName -> {
                try {
                    jsonObjectRow.put(columnName, resultSet.getObject(columnName));
                } catch (JSONException | SQLException e) {
                    e.printStackTrace();
                }
            });
            jsonArray.put(jsonObjectRow);
        }
        return jsonArray;
    }
}
