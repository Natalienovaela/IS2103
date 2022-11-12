/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import entity.Customer;
import entity.Reservation;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author PERSONAL
 */
public class ReservationClientModule {
    private final Customer customer;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;

    public ReservationClientModule(Customer customer, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this.customer = customer;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }
    
    public void menuReservationClient() {
    Scanner sc = new Scanner(System.in);
        Integer number;
        
        while(true) {
            System.out.println("*** Welcome Member ***\n");
            System.out.println("1. Create Reservation");
            System.out.println("2. View Reservation Details");
            System.out.println("3. View All My Reservation");
            System.out.println("4. LogOut");
            
            number = sc.nextInt();
            
            
            if(number >= 1 && number <= 3) {
                doReservation(number);
            }
            else if(number == 4) {
                doLogOut(customer);
            }
            else{
                System.out.println("Invalid option! Please try again!\n");
            }
        }
    }
    
    public void doReservation(Integer number) {
        Scanner sc = new Scanner(System.in);
        if(number == 1) {
            
        } else if(number == 2) {
            
            
        } else if(number == 3) {
            List<Reservation> reservations = reservationSessionBeanRemote.retrieveMyReservations(customer.getCustomerId());
            for(Reservation reservation: reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId() + "period: " + reservation.getPickUpDate() + "-" + reservation.getReturnDate() + " total Amount: " + reservation.getTotalAmount());
            }
            
        }
    }
    
    public void doLogOut(Customer customer) {
        customer = null;
    }
    
    
}
