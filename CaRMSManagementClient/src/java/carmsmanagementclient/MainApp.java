/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.exception.CategoryNotExistException;
import util.exception.EmployeeNotExistException;

/**
 *
 * @author Natalienovaela
 */
public class MainApp {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote;
    
    private Employee employee;
    
    private OperationsManagementModule operationsManagement;

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.transitDriverDispatchSessionBeanRemote = transitDriverDispatchSessionBeanRemote;
    }
    
    
    public void run() throws EmployeeNotExistException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Welcome to Car Rental Management System!");

            if( employee == null) {
                doLogin(); 
                System.out.println();
            } else {
                if(employee.getRole().name().equals("SALESMANAGER")) {
                    System.out.println("You are login as Sales Manager\n");
                    
                } else if(employee.getRole().name().equals("OPERATIONSMANAGER")) {
                    System.out.println("You are login as Operations Manager\n");
                    operationsManagement = new OperationsManagementModule(employee, modelSessionBeanRemote, categorySessionBeanRemote, transitDriverDispatchSessionBeanRemote);
                    operationsManagement.menuOperationsManagement();
                    
                } else if(employee.getRole().name().equals("CUSTOMERSALESEXECUTIVE")) {
                    System.out.println("You are login as Customer Sales Executive\n");
                    
                    
                } else if(employee.getRole().name().equals("SYSTEMADMINISTRATOR")) {
                    System.out.println("You are login as System Administrator\n");
                    
                    
                } else if(employee.getRole() == null) {
                    
                }
            }
        }
    }
    
    public void doLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please login before continuing..");
        System.out.println("email: ");
        String email = sc.nextLine();

        try{
            Employee employees = employeeSessionBeanRemote.retrieveEmployeeByEmail(email);
            System.out.println("password: ");
            String password = sc.nextLine();
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

    private TransitDriverDispatchSessionBeanRemote lookupTransitDriverDispatchSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (TransitDriverDispatchSessionBeanRemote) c.lookup("java:comp/env/TransitDriverDispatchSessionBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
