package com.example.employeesystem;


import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        try {
            dbManager.connect();
            dbManager.createTables();


            dbManager.insertEmployee(3, "Salma Mohamed", "Engineering", 8500.00, "2022-01-10");


            dbManager.updateEmployeeSalary(1, 9000.00);


            System.out.println("All Employees:");
            dbManager.selectAllEmployees();


            dbManager.deleteEmployee(3);


            dbManager.createAdminUser();
            dbManager.revokeDeletePrivilege();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

