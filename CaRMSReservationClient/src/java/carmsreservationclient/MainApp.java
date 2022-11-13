/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Car;
import entity.Category;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import entity.Reservation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotExistException;
import util.exception.CustomerExistException;
import util.exception.CustomerNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotAvailableException;
import util.exception.ModelNotExistException;
import util.exception.ModelNotInTheSearchListException;
import util.exception.OutletNotExistException;
import util.exception.OutsideOutletAvailability;
import util.exception.ReservationExistException;
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
    private CategorySessionBeanRemote categorySessionBeanRemote;
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Customer customer;
    
    public MainApp(CategorySessionBeanRemote categorySessionBeanrRemote, CustomerSessionBeanRemote customerSessionBeanRemote,ReservationSessionBeanRemote reservationSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
    this.categorySessionBeanRemote = categorySessionBeanRemote;
    this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    this.customerSessionBeanRemote = customerSessionBeanRemote;
    this.modelSessionBeanRemote = modelSessionBeanRemote;
    this.outletSessionBeanRemote = outletSessionBeanRemote;
    }
    
    
    
    public void run() throws ModelNotInTheSearchListException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Welcome to Car Rental Reservation System!");

                while(true) {
                if(customer == null) {
                    System.out.println("1. Login");
                    System.out.println("2. Sign Up");
                    
                } 
                System.out.println("3. Search Car");
                if(customer != null) {
                    System.out.println("You are login as a member\n");
                    System.out.println("4. Create Reservation");
                    System.out.println("5. View Reservation Details");
                    System.out.println("6. View All My Reservation");
                    System.out.println("7. LogOut");
                }
                Integer number = sc.nextInt();
                
                if(number == 1) {
                    doLogin();
                    System.out.println();
                    
                } else if(number == 2) {
                    doSignUp();
                        System.out.println();
                }else if (number == 3) {
                    searchCar();
                } 
                else if (number >= 4 && number <=6) {
                    doReservation(number);
                } else if (number == 7) {
                    doLogOut();
                } else {
                    System.out.println("invalid option please try again! /n");
                }
            }
        }
    }
    
    public void doLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Login Page");
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
        }
    }
    
    public void doSignUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Sign up Page");
        System.out.println("Enter Email: ");
        String email = sc.nextLine();
        System.out.println("Enter mobile phone number: ");
        String phoneNumber= sc.nextLine();
        System.out.println("Enter passportNumber: ");
        String passportNumber= sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();

            Customer customers = new Customer(email, password, phoneNumber, passportNumber);
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
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
    
    public void doLogOut() {
        customer = null;
    }
    
    public void doReservation(Integer number) throws ModelNotInTheSearchListException {
        while(true) {
        try {
        Scanner sc = new Scanner(System.in);
        if(number == 1) {
            Model chosen1 = null;
            Category chosen = null;
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            System.out.println("Enter pick up Date : ");
            Date pickUpDate = date.parse(sc.nextLine());
            System.out.println("Enter return Date : ");
            Date returnDate = date.parse(sc.nextLine());
            System.out.println("Enter pick up location : ");
            String pickUpLocation = sc.nextLine();
            System.out.println("Enter return location : ");
            String returnLocation = sc.nextLine();
            System.out.println("Enter category of car:(leave blank if no need to specify)");
            String category = sc.nextLine();
            if(category.length() > 0) {
                try {
                chosen = categorySessionBeanRemote.retrieveCategoryByName(category);
                } catch(CategoryNotExistException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            }
            System.out.println("Enter model:(leave blank if no need to specify)");
            String model = sc.nextLine();
            System.out.println("Enter make:(leave blank if no need to specify)");
            String make = sc.nextLine();
            
            if(model.length() > 0 && make.length() > 0) {
                try{
                chosen1 = modelSessionBeanRemote.retrieveModelbyMakeandModel(make, model);
                } catch(ModelNotExistException ex) {
                System.out.println(ex.getMessage() + "\n");
                }
            }
            
            try{
                Outlet pickUp = outletSessionBeanRemote.retrieveOutletByName(pickUpLocation);
                try {
                    Outlet returnLoc = outletSessionBeanRemote.retrieveOutletByName(returnLocation);
                        try{
                                List<Model> models = modelSessionBeanRemote.searchCar(pickUpDate, returnDate, pickUp, returnLoc);
                                Integer i = 0;
                                for(Model modelM: models) 
                                   {
                                       if(!models.get(i).equals(chosen1)) {
                                           throw new ModelNotInTheSearchListException("Model is not Available");
                                       }
                                       i++;
                                           
                                   }
                            Reservation newReservation = new Reservation(pickUpDate, returnDate, pickUp, returnLoc, chosen, chosen1);
                            reservationSessionBeanRemote.createReservation(newReservation);
                            checkOut(newReservation);
                        }catch(ReservationExistException ex) {
                           System.out.println("Reservation with that ID already exist");
                        
                        } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                        
                        }catch(UnknownPersistenceException ex) {
                           System.out.println(ex.getMessage() + "\n");
                        
                        }
                }catch(OutletNotExistException ex) {
                    System.out.println("Outlet does not exist");
                }
            }catch(OutletNotExistException ex) {
                System.out.println("Outlet does not exist");
            }
            
        }  else if(number == 2) {
            System.out.println();
            
        } else if(number == 3) {
                    List<Reservation> reservations = reservationSessionBeanRemote.retrieveMyReservations(customer.getCustomerId());
                    for(Reservation reservation: reservations) {
                    System.out.println("Reservation ID: " + reservation.getReservationId() + " period: " + reservation.getPickUpDate() + "-" + reservation.getReturnDate() + " total Amount: " + reservation.getTotalAmount());
                    }
                 }
            
        }
        catch(ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
        }
    }
    
    public void checkOut(Reservation reservation) {
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("1. Pay Rental Fee Upfront");
            System.out.println("2. Pay Rental Fee Later");
            Integer number = sc.nextInt();

            if(number == 1) {
                reservation.setPaid(Boolean.TRUE);
            } else if ( number == 2) {
                reservation.setPaid(Boolean.FALSE);
                System.out.println("Enter cVV: ");
                customer.setcVV(sc.nextLine());
                System.out.println("Enter NameOnCard: ");
                customer.setNameOnCard(sc.nextLine());
                System.out.println("Enter cardNumber: ");
                customer.setCardNumber(sc.nextLine());
                
            }else {
                System.out.println("invalid input! please try again");
            }
        }
    }
    }
}
