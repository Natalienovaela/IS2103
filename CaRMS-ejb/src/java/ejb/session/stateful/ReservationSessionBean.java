/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.Car;
import entity.Customer;
import entity.Model;
import entity.RentalRates;
import entity.Reservation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarNotExistException;
import util.exception.ModelNotExistException;
import util.exception.RentalRateNotExistException;
import util.exception.ReservationNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Stateful
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public List<Reservation> retrieveReservationByModelId(Long modelId) throws ModelNotExistException {
        Model model = em.find(Model.class, modelId);
        if(model == null) {
            throw new ModelNotExistException("Model with Model ID " + modelId + " does not exist");
        } else {
            Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.model = :model");
            query.setParameter("model", model);
            return query.getResultList();
        }
    }
    
    @Override
    public List<Reservation> retrieveCurrentDateReservation(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(date);
        
        Calendar pickUpCalendar = Calendar.getInstance();
        
        Query query = em.createQuery("SELECT r From Reservation r");
        List<Reservation> reservations = query.getResultList();
        
        List<Reservation> returnedReservation = new ArrayList<>();
        
        for(Reservation reservation: reservations) {
            pickUpCalendar.setTime(reservation.getPickUpDate());
            if(pickUpCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)
                    && pickUpCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)
                    && pickUpCalendar.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)){
                returnedReservation.add(reservation);
            }
        }
        
        return returnedReservation;
        
    }
    
    @Override
    public List<Reservation> retrieveReservationByCarId(Long carId) throws CarNotExistException {
        Car car = em.find(Car.class, carId);
        if(car == null) {
            throw new CarNotExistException("Car with Car ID " + carId + " does not exist");
        } else {
            Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.car = :car");
            query.setParameter("car", car);
            return query.getResultList();
        }
    }
    
    @Override
    public List<Reservation> retrieveReservationByRentalRateId(Long rentalRateId) throws RentalRateNotExistException {
        RentalRates rentalRate = em.find(RentalRates.class, rentalRateId);
        if(rentalRate == null) {
            throw new RentalRateNotExistException("Rental Rate with Rental Rate ID " + rentalRateId + " does not exist");
        } else {
            Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.rentalRates = :rentalRate");
            query.setParameter("rentalRate", rentalRate);
            return query.getResultList();
        }
    }
    
     @Override
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotExistException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if(reservation == null) {
            throw new ReservationNotExistException("Reservation with Reservation ID " + reservationId + " does not exist");
        } else {
            return reservation;
        }
    }
    
    public List<Reservation> retrieveAllReservation() {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        return query.getResultList();
    }
    
    
    //public Reservation reserveCar(Car car, Customer customer) {
        
    //}
    
    public void CancelReservation(long reservationId) {
        
    }
}
