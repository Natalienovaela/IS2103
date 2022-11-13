/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Car;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerExistException;
import util.exception.CustomerNotExistException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotExistException;
import util.exception.OutsideOutletAvailability;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
public class MainApp {
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Customer customer;
    private ReservationClientModule reservationClient;
    
    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote,ReservationSessionBeanRemote reservationSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
    this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    this.customerSessionBeanRemote = customerSessionBeanRemote;
    this.modelSessionBeanRemote = modelSessionBeanRemote;
    this.outletSessionBeanRemote = outletSessionBeanRemote;
    }
    
    
    
    public void run() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Welcome to Car Rental Reservation System!");

            if(customer == null) {
                while(true) {
                System.out.println("1. Login");
                System.out.println("2. Sign Up");
                System.out.println("3. Search Car");
                Integer number = sc.nextInt();
                
                if(number == 1) {
                    doLogin();
                    System.out.println();
                    System.out.println("You are login as a member\n");
                    reservationClient = new ReservationClientModule(customer, reservationSessionBeanRemote);
                    reservationClient.menuReservationClient();
                } else if(number == 2) {
                    doSignUp();
                        System.out.println();
                    System.out.println("You are login as a member\n");
                    reservationClient = new ReservationClientModule(customer, reservationSessionBeanRemote);
                    reservationClient.menuReservationClient();
                }else if (number == 3) {
                    searchCar();
                } else {
                    System.out.println("invalid option please try again! /n");
                }
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
            Customer customers = customerSessionBeanRemote.retrieveCustomerByEmail(email);
            System.out.println("password: ");
            String password = sc.nextLine();
            if(password.equals(customers.getPassword())) {
                customer = customers;
            } else {
                System.out.println("Wrong password!\n");
            }
        }  
        catch(CustomerNotExistException ex) {
            System.out.println(ex.getMessage());
            doSignUp();
        }
    }
    
    public void doSignUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Sign up Page");
        System.out.println("Enter Name: ");
        String name = sc.nextLine();
        System.out.println("Enter Email: ");
        String email = sc.nextLine();
        System.out.println("Enter mobile phone number: ");
        String phoneNumber= sc.nextLine();
        System.out.println("Enter passportNumber: ");
        String passportNumber= sc.nextLine();

            Customer customers = new Customer(name, email, phoneNumber, passportNumber);
            Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(customers);
            if(constraintViolations.isEmpty()) {
            try {
            customerSessionBeanRemote.registerCustomer(customers);
            System.out.println("Account is created successfully!\n");
            }catch(UnknownPersistenceException ex){
                System.out.println("An unknown error has occurred while creating the new account!: " + ex.getMessage() + "\n");
            }catch(InputDataValidationException ex){
                System.out.println(ex.getMessage() + "\n");
            } catch(CustomerExistException ex) {
                System.out.println("An error has occurred while creating the account!: The account already exist\n");
            }
                
            }
    }
    
    public void searchCar() {
        try {
            Scanner sc = new Scanner(System.in);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            System.out.println("Enter pick up Date: ");
            Date pickUpDate = date.parse(sc.nextLine());
            System.out.println("Enter return Date: ");
            Date returnDate = date.parse(sc.nextLine());
            System.out.println("Enter pick up location: ");
            String pickUpLocation = sc.nextLine();
            System.out.println("Enter return location: ");
            String returnLocation = sc.nextLine();
         
            try{
                Outlet pickUpLoc = outletSessionBeanRemote.retrieveOutletByName(pickUpLocation);
                try {
                    Outlet returnLoc = outletSessionBeanRemote.retrieveOutletByName(returnLocation);
                    try{
                        outletSessionBeanRemote.checkOutletAvailability(pickUpDate, pickUpLoc.getOutletId());
                        outletSessionBeanRemote.checkOutletAvailability(returnDate, returnLoc.getOutletId());
                        List<Model> models = modelSessionBeanRemote.searchCar(pickUpDate, returnDate, pickUpLoc, returnLoc);
                        for(Model model : models)
                           {
                               System.out.println("Car model: " + model.getModel() + ", Car make: " + model.getMake() + ", Car Category: " + model.getCategory().getCategoryName());
                           }
                        }
                    catch(OutsideOutletAvailability ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                catch(OutletNotExistException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            catch(OutletNotExistException ex) {
                System.out.println(ex.getMessage());
            }
        }
         catch(ParseException ex)
        {
            System.out.println("Invalid date input!\n");
        }   
    }
}
