/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.CarNotExistException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
    private TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote;
    private EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    private Employee employee;
    
    private OperationsManagementModule operationsManagement;
    private SalesManagementModule salesManagement;
    private CustomerServiceModule customerService;

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote, EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.transitDriverDispatchSessionBeanRemote = transitDriverDispatchSessionBeanRemote;
        this.ejbTimerSessionBeanRemote = ejbTimerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
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
                    employee = null;                           
                } else if(employee.getRole().name().equals("OPERATIONSMANAGER")) {
                    System.out.println("You are login as Operations Manager\n");
                    operationsManagement = new OperationsManagementModule(employee, modelSessionBeanRemote, categorySessionBeanRemote, transitDriverDispatchSessionBeanRemote, ejbTimerSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote);
                    operationsManagement.menuOperationsManagement();
                    employee = null;
                    
                } else if(employee.getRole().name().equals("CUSTOMERSALESEXECUTIVE")) {
                    System.out.println("You are login as Customer Sales Executive\n");
                    customerService = new CustomerServiceModule(employee, reservationSessionBeanRemote, carSessionBeanRemote);
                    customerService.menuCustomerService();
                    employee = null;
                    
                } else if(employee.getRole().name().equals("SYSTEMADMINISTRATOR")) {
                    System.out.println("You are login as System Administrator\n");
                    System.out.println("*** CaRMS Management Module ***\n");
                    System.out.println("1. Sales Management Module\n");
                    System.out.println("2. Operations Management Module\n");
                    System.out.println("3. Customer Service Management Module\n");
                    int number = sc.nextInt();
                    if(number == 1) {
                        System.out.println("You are login as Sales Manager\n");
                        salesManagement = new SalesManagementModule(rentalRateSessionBeanRemote, categorySessionBeanRemote);
                        salesManagement.menuSalesManagement();
                    } else if(number == 2) {
                        System.out.println("You are login as Operations Manager\n");
                        operationsManagement = new OperationsManagementModule(employee, modelSessionBeanRemote, categorySessionBeanRemote, transitDriverDispatchSessionBeanRemote, ejbTimerSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote);
                        operationsManagement.menuOperationsManagement();
                    } else if(number == 3) {
                        System.out.println("You are login as Customer Sales Executive\n");
                        customerService = new CustomerServiceModule(employee, reservationSessionBeanRemote, carSessionBeanRemote);
                        customerService.menuCustomerService();
                    } else {
                        System.out.println("Invalid input option! Try again!\n");
                    }
                    
                } else {
                    System.out.println("No such role! Try again!\n");
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
