package model;

public class Employee {

    private String id;
    private String name;
    private String department;
    private String phone;

    public Employee(String id, String name, String department, String phone) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhone() {
        return phone;
    }
}