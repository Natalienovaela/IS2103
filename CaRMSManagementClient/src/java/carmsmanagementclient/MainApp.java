/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;

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
    
    
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Car Rental Management System!");
        
        if( employee == null) {
            System.out.println("Please login before continuing..");
            System.out.println("email: ");
            String email = sc.next();
            /*employeeSessionBeanRemote.check(email);*/
            System.out.println("password: ");
            String password = sc.next();

            
        }
    }
}
