/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.CarNotExistException;
import util.exception.CategoryNotExistException;
import util.exception.EmployeeNotExistException;
import util.exception.ModelNotExistException;
import util.exception.RentalRateNotExistException;

/**
 *
 * @author Natalienovaela
 */
public class MainApp {
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    private Employee employee;
    
    private OperationsManagementModule operationsManagement;
    
    private SalesManagementModule salesManagement;

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
    }
    
    public void run() throws EmployeeNotExistException, ModelNotExistException, CarNotExistException, RentalRateNotExistException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Welcome to Car Rental Management System!");

            if( employee == null) {
                doLogin(); 
                System.out.println();
            } else {
                if(employee.getRole().name().equals("SALESMANAGER")) {
                    System.out.println("You are login as Sales Manager\n");
                    salesManagement = new SalesManagementModule(rentalRateSessionBeanRemote, categorySessionBeanRemote);
                    salesManagement.menuSalesManagement();
                    
                } else if(employee.getRole().name().equals("OPERATIONSMANAGER")) {
                    System.out.println("You are login as Operations Manager\n");
                    operationsManagement = new OperationsManagementModule(modelSessionBeanRemote, categorySessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote);
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
}
