package dao;

import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Add Employee
    public boolean addEmployee(String employeeId, String name,
                               String department, String phone,
                               String status) {

        String sql = "INSERT INTO employees " +
                     "(employee_id, name, department, phone, status) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ps.setString(2, name);
            ps.setString(3, department);
            ps.setString(4, phone);
            ps.setString(5, status);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Get All Employees
    public List<String[]> getAllEmployees() {

        List<String[]> employees = new ArrayList<>();

        String sql = "SELECT employee_id, name, department, phone, status FROM employees";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String[] employee = {
                    rs.getString("employee_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("phone"),
                    rs.getString("status")
                };

                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }


    // Delete Employee
    public boolean deleteEmployee(String employeeId) {

        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, employeeId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}