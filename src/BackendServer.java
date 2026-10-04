import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import dao.AttendanceDAO;
import dao.EmployeeDAO;
import dao.LeaveDAO;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BackendServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8081), 0);

        // =========================
        // EMPLOYEE API
        // =========================

        server.createContext("/api/employees", (exchange) -> {

            addCors(exchange);

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

                EmployeeDAO dao = new EmployeeDAO();

                List<String[]> employees = dao.getAllEmployees();

                StringBuilder json = new StringBuilder("[");

                for (int i = 0; i < employees.size(); i++) {

                    String[] e = employees.get(i);

                    json.append("{")
                        .append("\"employee_id\":\"").append(e[0]).append("\",")
                        .append("\"name\":\"").append(e[1]).append("\",")
                        .append("\"department\":\"").append(e[2]).append("\",")
                        .append("\"phone\":\"").append(e[3]).append("\",")
                        .append("\"status\":\"").append(e[4]).append("\"")
                        .append("}");

                    if (i < employees.size() - 1) {
                        json.append(",");
                    }
                }

                json.append("]");

                sendResponse(exchange, 200, json.toString());

            } else {

                sendResponse(exchange, 405,
                        "{\"message\":\"Method Not Allowed\"}");
            }
        });


        // =========================
        // ADD EMPLOYEE API
        // =========================

        server.createContext("/api/addEmployee", (exchange) -> {

            addCors(exchange);

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                String body = readRequestBody(exchange);

                String[] data = body.split("&");

                String employeeId = getValue(data, "employeeId");
                String name = getValue(data, "name");
                String department = getValue(data, "department");
                String phone = getValue(data, "phone");
                String status = getValue(data, "status");

                EmployeeDAO dao = new EmployeeDAO();

                boolean success = dao.addEmployee(
                        employeeId,
                        name,
                        department,
                        phone,
                        status
                );

                if (success) {

                    sendResponse(exchange, 200,
                            "{\"message\":\"Employee added successfully\"}");

                } else {

                    sendResponse(exchange, 500,
                            "{\"message\":\"Failed to add employee\"}");
                }

            } else {

                sendResponse(exchange, 405,
                        "{\"message\":\"Method Not Allowed\"}");
            }
        });


        // =========================
        // ATTENDANCE API
        // =========================

        server.createContext("/api/attendance", (exchange) -> {

            addCors(exchange);

            // POST - Mark attendance
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                String body = readRequestBody(exchange);

                String[] data = body.split("&");

                String employeeId = getValue(data, "employeeId");
                String status = getValue(data, "status");

                AttendanceDAO dao = new AttendanceDAO();

                boolean success =
                        dao.markAttendance(employeeId, status);

                if (success) {

                    sendResponse(exchange, 200,
                            "{\"message\":\"Attendance marked successfully\"}");

                } else {

                    sendResponse(exchange, 500,
                            "{\"message\":\"Failed to mark attendance\"}");
                }
            }

            // GET - Load attendance
            else if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

                AttendanceDAO dao = new AttendanceDAO();

                List<String[]> records =
                        dao.getAllAttendance();

                StringBuilder json =
                        new StringBuilder("[");

                for (int i = 0; i < records.size(); i++) {

                    String[] r = records.get(i);

                    json.append("{")
                        .append("\"attendance_date\":\"")
                        .append(r[0])
                        .append("\",")

                        .append("\"employee_id\":\"")
                        .append(r[1])
                        .append("\",")

                        .append("\"attendance_status\":\"")
                        .append(r[2])
                        .append("\"")
                        .append("}");

                    if (i < records.size() - 1) {
                        json.append(",");
                    }
                }

                json.append("]");

                sendResponse(
                        exchange,
                        200,
                        json.toString()
                );
            }

            else {

                sendResponse(
                        exchange,
                        405,
                        "{\"message\":\"Method Not Allowed\"}"
                );
            }
        });


        // =========================
        // LEAVE API
        // =========================

        server.createContext("/api/leave", (exchange) -> {

            addCors(exchange);

            // GET - Load leave requests
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

                LeaveDAO dao = new LeaveDAO();

                List<String[]> leaves =
                        dao.getAllLeaves();

                StringBuilder json =
                        new StringBuilder("[");

                for (int i = 0; i < leaves.size(); i++) {

                    String[] leave = leaves.get(i);

                    json.append("{")
                        .append("\"leave_id\":\"")
                        .append(leave[0])
                        .append("\",")

                        .append("\"employee_id\":\"")
                        .append(leave[1])
                        .append("\",")

                        .append("\"reason\":\"")
                        .append(leave[2])
                        .append("\",")

                        .append("\"leave_status\":\"")
                        .append(leave[3])
                        .append("\"")
                        .append("}");

                    if (i < leaves.size() - 1) {
                        json.append(",");
                    }
                }

                json.append("]");

                sendResponse(
                        exchange,
                        200,
                        json.toString()
                );
            }

            // POST - Apply leave
            else if ("POST".equalsIgnoreCase(
                    exchange.getRequestMethod())) {

                String body =
                        readRequestBody(exchange);

                String[] data =
                        body.split("&");

                String employeeId =
                        getValue(data, "employeeId");

                String reason =
                        getValue(data, "reason");

                LeaveDAO dao =
                        new LeaveDAO();

                boolean success =
                        dao.applyLeave(
                                employeeId,
                                reason
                        );

                if (success) {

                    sendResponse(
                            exchange,
                            200,
                            "{\"message\":\"Leave request submitted successfully\"}"
                    );

                } else {

                    sendResponse(
                            exchange,
                            500,
                            "{\"message\":\"Failed to submit leave request\"}"
                    );
                }
            }

            else {

                sendResponse(
                        exchange,
                        405,
                        "{\"message\":\"Method Not Allowed\"}"
                );
            }
        });


        // =========================
        // LEAVE STATUS API
        // =========================

        server.createContext("/api/leave/status", (exchange) -> {

            addCors(exchange);

            if ("POST".equalsIgnoreCase(
                    exchange.getRequestMethod())) {

                String body =
                        readRequestBody(exchange);

                String[] data =
                        body.split("&");

                String leaveId =
                        getValue(data, "leaveId");

                String status =
                        getValue(data, "status");

                LeaveDAO dao =
                        new LeaveDAO();

                boolean success =
                        dao.updateLeaveStatus(
                                Integer.parseInt(leaveId),
                                status
                        );

                if (success) {

                    sendResponse(
                            exchange,
                            200,
                            "{\"message\":\"Leave status updated successfully\"}"
                    );

                } else {

                    sendResponse(
                            exchange,
                            500,
                            "{\"message\":\"Failed to update leave status\"}"
                    );
                }

            } else {

                sendResponse(
                        exchange,
                        405,
                        "{\"message\":\"Method Not Allowed\"}"
                );
            }
        });


        // =========================
        // START SERVER
        // =========================

        server.start();

        System.out.println("--------------------------------------");
        System.out.println("Employee Attendance Backend Started");
        System.out.println("Server running on port 8081");
        System.out.println("--------------------------------------");
    }


    // =========================
    // CORS
    // =========================

    private static void addCors(
            HttpExchange exchange) {

        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Origin",
                "*");

        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Methods",
                "GET, POST, OPTIONS");

        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Headers",
                "Content-Type");
    }


    // =========================
    // READ REQUEST BODY
    // =========================

    private static String readRequestBody(
            HttpExchange exchange)
            throws IOException {

        InputStream inputStream =
                exchange.getRequestBody();

        return new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8);
    }


    // =========================
    // GET FORM VALUE
    // =========================

    private static String getValue(
            String[] data,
            String key) {

        for (String item : data) {

            String[] pair =
                    item.split("=", 2);

            if (pair.length == 2 &&
                pair[0].equals(key)) {

                try {

                    return URLDecoder.decode(
                            pair[1],
                            StandardCharsets.UTF_8
                    );

                } catch (Exception e) {

                    return pair[1];
                }
            }
        }

        return "";
    }


    // =========================
    // SEND RESPONSE
    // =========================

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response)
            throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json");

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length);

        OutputStream outputStream =
                exchange.getResponseBody();

        outputStream.write(bytes);

        outputStream.close();
    }
}