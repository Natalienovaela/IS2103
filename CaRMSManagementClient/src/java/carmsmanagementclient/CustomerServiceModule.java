/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;
import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import entity.Employee;
import entity.Reservation;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ReservationNotExistException;
/**
 *
 * @author Natalienovaela
 */
public class CustomerServiceModule {
    private Employee employee; 
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    
    public CustomerServiceModule(Employee employee, ReservationSessionBeanRemote reservationSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote) {
        this.employee = employee;
        this.reservationSessionBeanRemote= reservationSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public void menuCustomerService() {
        Scanner sc = new Scanner(System.in);
        Integer number;
        
        while(true) {
         System.out.println("*** CaRMS Customer Service Module ***\n");
            System.out.println("1. Pickup Car");
            System.out.println("2. Return Car");
            System.out.println("3. Back");
            
            number = sc.nextInt();
            if(number == 1) {
                doPickUpCar();
            } else if(number == 2) {
                doReturnCar();
            }
            else if(number == 3) {
                break;
            }
            else{
                System.out.println("Invalid option! Please try again!\n");
            }
        }
    }
    
    public void doPickUpCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Reservation ID: ");
        Long reservationId = sc.nextLong();
        
        try{
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);
            if(!reservation.getPaid()){
                System.out.println("Customer has not paid for the car rental");
                System.out.println("The total fee is " + reservation.getTotalAmount());
            }
            carSessionBeanRemote.pickUpCar(reservation.getCar().getCarId(), reservation.getReservationId());
            System.out.println("Car with car ID " + reservation.getCar().getCarId() + " has already been picked up by customer with ID " + reservation.getCustomer());
        }
        catch(ReservationNotExistException ex) {
            System.out.println(ex.getMessage());
        }    
    }
    
    public void doReturnCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Reservation ID: ");
        Long reservationId = sc.nextLong();
        
        try{
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);
            carSessionBeanRemote.returnCar(reservation.getCar().getCarId(), reservation.getReservationId());
            System.out.println("Car with car ID " + reservation.getCar().getCarId() + "has already been returned by customer with ID " + reservation.getCustomer());
        }
        catch(ReservationNotExistException ex) {
            System.out.println(ex.getMessage());
        }    
    }
}
