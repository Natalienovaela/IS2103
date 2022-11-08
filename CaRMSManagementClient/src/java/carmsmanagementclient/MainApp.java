/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.EmployeeNotExistException;

/**
 *
 * @author Natalienovaela
 */
public class MainApp {
    EmployeeSessionBeanRemote employeeSessionBeanRemote;
    Employee employee;

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
    }
    
    
    public void run() throws EmployeeNotExistException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Welcome to Car Rental Management System!");

            if( employee == null) {
                doLogin(); 
            } else {
                
            }
        }
    }
    
    public void doLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please login before continuing..");
        System.out.println("email: ");
        String email = sc.next();

        try{
            Employee employees = employeeSessionBeanRemote.retrieveEmployeeByEmail(email);
            System.out.println("password: ");
            String password = sc.next();
            if(password.equals(employees.getPassword())) {
                employee = employees;
            } else {
                System.out.println("Wrong password!\n");
            }
        }  
        catch(EmployeeNotExistException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void doLogout() {
        employee = null;
    }
}
