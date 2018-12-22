package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DB {
    final static String URL = "jdbc:derby:measurementDB;create=true";
    final static String USERNAME = "";
    final static String PASSWORD = "";

    public static void createNewTable() {
        Connection conn = null;
        try {
            conn = getDBConnection();
            Statement createStatement = conn.createStatement();
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, "measurements", null);
            if (!rs.next()) {
                createStatement.execute("create table measurements(id INT not null primary key generated always as identity (start with 1, increment by 1), meastime varchar (14), origotime varchar (14),actualweight int, origoweight int )");
                System.out.println("tábla kész");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addMeasurement(Measurement measurement) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        String sql = "";
        try {
            conn = getDBConnection();
            sql = "insert into measurements (meastime, origotime, actualweight, origoweight) values (?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, measurement.getMeasurementTime());
            preparedStatement.setString(2, measurement.getOrigoTime());
            preparedStatement.setInt(3, measurement.getActualWeight());
            preparedStatement.setInt(4, measurement.getOrigoWeight());
            preparedStatement.execute();

            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Measurement getMeasurement(int id) {
        Connection conn = null;
        Measurement measurement = null;
        ResultSet rs = null;

        PreparedStatement preparedStatement = null;
        Statement statement = null;
        String sql = "";
        try {
            conn = getDBConnection();
            if (id == -1) {
                sql = "select * from measurements order by meastime desc fetch first row only";
                statement = conn.createStatement();
                rs = statement.executeQuery(sql);
            } else {
                sql = "select * from measurements where id = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                rs = preparedStatement.executeQuery(sql);
            }
            if (rs.next()) {
                measurement = new Measurement(rs.getInt("id"), rs.getString("meastime"), rs.getString("origotime"), rs.getInt("actualweight"), rs.getInt("origoweight"));
            }
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return measurement;
    }

    private static Connection getDBConnection() {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;

    }

}
