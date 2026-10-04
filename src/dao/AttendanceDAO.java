package dao;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public boolean markAttendance(String employeeId, String status) {

        String sql =
            "INSERT INTO attendance " +
            "(employee_id, attendance_date, attendance_status) " +
            "VALUES (?, CURDATE(), ?)";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, employeeId);
            ps.setString(2, status);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String[]> getAllAttendance() {

        List<String[]> records = new ArrayList<>();

        String sql =
            "SELECT attendance_date, employee_id, attendance_status " +
            "FROM attendance ORDER BY attendance_date DESC";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                String[] record = {
                    rs.getString("attendance_date"),
                    rs.getString("employee_id"),
                    rs.getString("attendance_status")
                };

                records.add(record);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}