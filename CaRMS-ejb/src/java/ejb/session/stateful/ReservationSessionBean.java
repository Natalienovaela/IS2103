/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBean;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Car;
import entity.Category;
import entity.Model;
import entity.RentalRates;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
import util.exception.CategoryNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotAvailableException;
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

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;
    
    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;
    
    

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
    public List<Reservation> retrieveAllReservationsWithThisRentalRate(Long RentalRateId) {
        List<Reservation> reservations = em.createQuery("Select r FROM Reservation r").getResultList();
        
        RentalRates rentalRate = em.find(RentalRates.class, RentalRateId);
        
        for(Reservation r : reservations) {
            List<RentalRates> rentalRates = r.getRentalRates();
            for(RentalRates re: rentalRates) {
                if(!re.getName().equals(rentalRate.getName())) {
                    reservations.remove(r);
                    break;
                }
            }
        } 
        return reservations;
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
    
    public BigDecimal cancelReservation(long reservationId) throws ReservationNotExistException {
        Reservation cancelReservation = retrieveReservationById(reservationId);
        if(cancelReservation == null) {
            throw new ReservationNotExistException("Reservation with Reservation ID " + reservationId + " does not exist");
        }
        
        
            
        
        Calendar cancelCalendar = Calendar.getInstance();
        Calendar pickupCalendar = Calendar.getInstance();
        pickupCalendar.setTime(cancelReservation.getPickUpDate());
        cancelCalendar.add(Calendar.DATE, 14);
        BigDecimal totalAmount = cancelReservation.getTotalAmount();
        BigDecimal penalty = new BigDecimal("0");
        
        cancelReservation.getCustomer().getReservations().remove(cancelReservation);
        em.remove(cancelReservation);

        if(cancelCalendar.compareTo(pickupCalendar) == 0 || cancelCalendar.compareTo(pickupCalendar) == 1) {
            return penalty;
        } else {
            cancelCalendar.add(Calendar.DATE, -7);
            if(cancelCalendar.compareTo(pickupCalendar) == 0 || cancelCalendar.compareTo(pickupCalendar) == 1) {
                penalty = totalAmount.multiply(BigDecimal.valueOf(20/100));
                return penalty;
            } else {
                cancelCalendar.add(Calendar.DATE, -4);
                if(cancelCalendar.compareTo(pickupCalendar) == 0 || cancelCalendar.compareTo(pickupCalendar) == 1) {
                penalty = totalAmount.multiply(BigDecimal.valueOf(50/100));
                return penalty;
                } else {
                    penalty = totalAmount.multiply(BigDecimal.valueOf(70/100));
                    return penalty;
                }
            }
        }
        
        
        
        
    }
    
    @Override
    public void createReservation(Reservation reservation) throws ReservationExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(reservation);
       if(constraintViolations.isEmpty()) {
           
        try {
                em.persist(reservation);
                em.flush();
        } catch(PersistenceException ex) {
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
    
    @Override
    public BigDecimal totalAmount(Reservation reservation) {
        Category category = reservation.getCategory();
        
        List<RentalRates> rentals = category.getRentalRates();
        RentalRates finalRent = rentals.get(0);
        Calendar cal = Calendar.getInstance();
        Calendar pic = Calendar.getInstance();
        pic.setTime(reservation.getPickUpDate());
        Calendar ret = Calendar.getInstance();
        ret.setTime(reservation.getReturnDate());
        BigDecimal amount = BigDecimal.valueOf(0);
        
        while(pic.before(ret)) {
            for(int i = 0; i <= rentals.size(); i++) {
                cal.setTime(rentals.get(i).getStartDateTime());
                if(cal == null || pic.after(cal) || pic.equals(cal)) {
                    if(rentals.get(i).getRentalRateType().equals("Promotion") && (finalRent.getRentalRateType().equals("Default") || finalRent.getRentalRateType().equals("Peak"))) {
                        finalRent = rentals.get(i);
                    }  
                    else if(rentals.get(i).getRentalRateType().equals("Promotion") && finalRent.getRentalRateType().equals("Promotion") && rentals.get(i).getRatePerDay().compareTo(finalRent.getRatePerDay()) < 0) {
                        finalRent = rentals.get(i);
                    }
                    else if(rentals.get(i).getRentalRateType().equals("Peak") && (rentals.get(i).getRentalRateType().equals("Default"))) {
                        finalRent = rentals.get(i);
                    } else if(rentals.get(i).getRentalRateType().equals("Peak") && finalRent.getRentalRateType().equals("Peak") && rentals.get(i).getRatePerDay().compareTo(finalRent.getRatePerDay()) < 0) {
                        finalRent = rentals.get(i);
                    }
                    else if(rentals.get(i).getRentalRateType().equals("Default") && finalRent.getRentalRateType().equals("Default") && rentals.get(i).getRatePerDay().compareTo(finalRent.getRatePerDay()) < 0) {
                        finalRent = rentals.get(i);
                    }
                    else {
                        finalRent = rentals.get(i);
                    }
                }
            }
            
            amount.add(finalRent.getRatePerDay());
            pic.add(Calendar.DATE, 1);
        }
        return amount;
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
