package db;

import java.util.ArrayList;
import model.Employee;

public class DataStore {

    // Employee details
    public static ArrayList<Employee> employees =
            new ArrayList<Employee>();

    // Attendance:
    // Date, Employee ID, Status
    public static ArrayList<String[]> attendance =
            new ArrayList<String[]>();

    // Leave:
    // Employee ID, Reason, Status
    public static ArrayList<String[]> leaves =
            new ArrayList<String[]>();

    // Sample employee
    static {
        employees.add(
            new Employee(
                "E001",
                "Vidhya",
                "HR",
                "9876543210"
            )
        );
    }
}