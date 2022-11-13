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
import java.util.Set;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarExistException;
import util.exception.CarNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotExistException;
import util.exception.RentalRateNotExistException;
import util.exception.ReservationExistException;
import util.exception.ReservationNotExistException;
import util.exception.UnknownPersistenceException;

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
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
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
    
    @Override
    public List<Reservation> retrieveMyReservations(long customerId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.customer.customerId :customer");
        query.setParameter("customer", customerId);
        return query.getResultList();
    }
    
    
    //public Reservation reserveCar(Car car, Customer customer) {
        
    //}
    
    /*public void cancelReservation(long reservationId, Date cancelDate) throws ReservationNotExistException {
        Calendar calendar = Calendar.getInstance();
        Reservation cancelReservation = retrieveReservationById(reservationId);
        if(cancelReservation == null) {
            throw new ReservationNotExistException("Reservation with Reservation ID " + reservationId + " does not exist");
        }
        
        if(calendar.add(Calendar.DATE, 14) <= cancelReservation.getPickUpDate().get(Calendar.YEAR))
        
        
        
    }*/
    
    public void createReservation(Reservation reservation, long carId) throws ReservationExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(reservation);
       
       if(constraintViolations.isEmpty()) {
            try {
                Car car = em.find(Car.class, carId);
                em.persist(reservation);
                reservation.setCar(car);
                car.getReservations().add(reservation);
                em.flush();
            }
            catch(PersistenceException ex) {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new ReservationExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
       } else {
           throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
       }
        
        
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
