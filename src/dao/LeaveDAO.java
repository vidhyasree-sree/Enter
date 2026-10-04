package dao;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {

    // Apply Leave
    public boolean applyLeave(String employeeId, String reason) {

        String sql =
            "INSERT INTO leave_requests " +
            "(employee_id, reason, leave_status) " +
            "VALUES (?, ?, 'Pending')";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, employeeId);
            ps.setString(2, reason);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all leave requests
    public List<String[]> getAllLeaves() {

        List<String[]> leaves = new ArrayList<>();

        String sql =
            "SELECT leave_id, employee_id, reason, leave_status " +
            "FROM leave_requests ORDER BY leave_id DESC";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                String[] leave = {
                    rs.getString("leave_id"),
                    rs.getString("employee_id"),
                    rs.getString("reason"),
                    rs.getString("leave_status")
                };

                leaves.add(leave);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return leaves;
    }

    // Approve / Reject leave
    public boolean updateLeaveStatus(int leaveId, String status) {

        String sql =
            "UPDATE leave_requests " +
            "SET leave_status = ? " +
            "WHERE leave_id = ?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, status);
            ps.setInt(2, leaveId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}