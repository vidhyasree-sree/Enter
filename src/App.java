import dao.AttendanceDAO;
import java.util.List;

public class App {

    public static void main(String[] args) {

        System.out.println("Employee Attendance & Leave Management System");
        System.out.println("Java Backend Started Successfully!");

        AttendanceDAO dao = new AttendanceDAO();

        List<String[]> records = dao.getAllAttendance();

        System.out.println("\n===== ATTENDANCE RECORDS =====");

        if (records.isEmpty()) {
            System.out.println("No attendance records found.");
        } else {
            for (String[] record : records) {
                System.out.println(
                    "Employee ID: " + record[0] +
                    " | Date: " + record[1] +
                    " | Status: " + record[2]
                );
            }
        }
    }
}