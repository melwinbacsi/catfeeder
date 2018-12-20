package db;

import org.bytedeco.javacpp.presets.opencv_core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    final String URL = "jdbc:derby:measurementDB;create=true";
    final String USERNAME = "";
    final String PASSWORD = "";
    Connection conn;
    Statement createStatement;
    DatabaseMetaData dbmd;
    ResultSet rs;

    public DB() {

        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet rs = dbmd.getTables(null, "MAIL", "MEASUREMENTS", null);
            if (!rs.next()) {
                createStatement.execute("create table measurements(id INT not null primary key generated always as identity (start with 1, increment by 1), meastime varchar (14), origotime varchar (14),actualweight int(4), origoweight int (4))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addMeasurement(Measurement measurement) {
        try {
            String sql = "insert into measurements (meastime, origotime, actualweight, origoweight) values (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, measurement.getMeasurementTime());
            preparedStatement.setString(2, measurement.getOrigoTime());
            preparedStatement.setInt(2, measurement.getActualWeight());
            preparedStatement.setInt(3, measurement.getOrigoWeight());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Measurement getMeasurement(int id) {
        Measurement measurement = null;
        String sql = "select * from measurements where id = ?";
        if (id == -1) {
            sql = "select origoweight from measurements order by meastime desc limit 1";
        } else {
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                rs = createStatement.executeQuery(sql);
                if (!rs.next()) {
                    measurement = new Measurement(rs.getInt("id"),rs.getString("meastime"), rs.getString("origotime"), rs.getInt("actualweight"), rs.getInt("origoweight"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return measurement;
    }
}
