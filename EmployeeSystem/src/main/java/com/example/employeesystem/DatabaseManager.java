package com.example.employeesystem;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/Employee_System";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";
    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Create tables
    public void createTables() throws SQLException {
        String employeeTableSQL = "CREATE TABLE IF NOT EXISTS Employee ("
                + "id INT PRIMARY KEY, "
                + "name VARCHAR(100), "
                + "department VARCHAR(50), "
                + "salary DECIMAL(10, 2), "
                + "hireDate DATE"
                + ");";

        String managerTableSQL = "CREATE TABLE IF NOT EXISTS Manager ("
                + "id INT PRIMARY KEY, "
                + "teamMembers TEXT, "
                + "FOREIGN KEY (id) REFERENCES Employee(id)"
                + ");";

        executeUpdate(employeeTableSQL);
        executeUpdate(managerTableSQL);
    }

    // DML Operations
    public void insertEmployee(int id, String name, String department, double salary, String hireDate) throws SQLException {
        String sql = "INSERT INTO Employee (id, name, department, salary, hireDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, department);
            stmt.setDouble(4, salary);
            stmt.setDate(5, Date.valueOf(hireDate));
            stmt.executeUpdate();
        }
    }

    public void updateEmployeeSalary(int id, double salary) throws SQLException {
        String sql = "UPDATE Employee SET salary = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, salary);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM Employee WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void selectAllEmployees() throws SQLException {
        String sql = "SELECT * FROM Employee";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Department: %s, Salary: %.2f, Hire Date: %s%n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("department"),
                        rs.getDouble("salary"), rs.getDate("hireDate"));
            }
        }
    }

    // DCL Commands for Privilege Management
    public void createAdminUser() throws SQLException {
        String createUserSQL = "CREATE USER second_employee_admin WITH PASSWORD '123'";
        String grantPrivilegesSQL = "GRANT ALL PRIVILEGES ON DATABASE EmployeeDB TO second_employee_admin";
        executeUpdate(createUserSQL);
        executeUpdate(grantPrivilegesSQL);
    }


    public void revokeDeletePrivilege() throws SQLException {
        String sql = "REVOKE DELETE ON ALL TABLES IN SCHEMA public FROM second_employee_admin";
        executeUpdate(sql);
    }

    private void executeUpdate(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}

