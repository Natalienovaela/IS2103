/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateful.ReservationSessionBeanLocal;
import entity.Car;
import entity.Category;
import entity.Model;
import entity.Reservation;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import util.exception.DeleteCarException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;
    
    

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CarSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void createCar(Car car, Long modelId) throws CarExistException, UnknownPersistenceException, InputDataValidationException {
       Set<ConstraintViolation<Car>>constraintViolations = validator.validate(car);
       
       if(constraintViolations.isEmpty()) {
            try {
                Model model = em.find(Model.class, modelId);
                em.persist(car);
                car.setModel(model);
                model.getCars().add(car);
                em.flush();
            }
            catch(PersistenceException ex) {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CarExistException();
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
    public List<Car> retrieveAllCar() {
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.model.category.categoryName, c.model.make, c.model.model, c.licensePlateNumber ASC");
        return query.getResultList();
    }
    
    public List<Car> retrieveAllCarByOutletandDate(String outlet, Date pickupDate) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE (c.reservations IS EMPTY AND c.currOutlet :outlet) OR (c.reservations <= :pickupDate AND c.currOutlet.outletName :outlet)");
        query.setParameter("outlet", outlet);
        query.setParameter("pickupDate", pickupDate);
        return query.getResultList();
    }
    
    @Override
    public Car retrieveCarById(Long carId) throws CarNotExistException{
        Car car = em.find(Car.class, carId);
        
        if(car == null) {

            throw new CarNotExistException("Car with Car ID " + carId + " does not exist");
        }
        else {
            return car;
        }
    }
    
    @Override
    public void updateCar(Car car) throws InputDataValidationException, CarNotExistException {
        if(car != null && car.getCarId()!= null)
        {
            Set<ConstraintViolation<Car>>constraintViolations = validator.validate(car);
        
            if(constraintViolations.isEmpty())
            {
                Car updateCar = retrieveCarById(car.getCarId());
                updateCar.setLicensePlateNumber(car.getLicensePlateNumber());
                updateCar.setAvailStatus(car.getAvailStatus());
                updateCar.getModel().getCars().remove(updateCar);
                updateCar.setModel(car.getModel());
                updateCar.getModel().getCars().add(car);
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new CarNotExistException("Car ID not provided for car to be updated");
        }
    }
    
    @Override
    public void deleteCar(Long carId) throws CarNotExistException, DeleteCarException
    {
        Car removeCar = retrieveCarById(carId);
        List<Reservation> reservations = reservationSessionBean.retrieveReservationByCarId(carId);
        
        if(reservations.isEmpty())
        {
            removeCar.getModel().getCars().remove(removeCar);
            em.remove(removeCar);
        }
        else
        {
            removeCar.setDisabled(Boolean.TRUE);
            throw new DeleteCarException("Car ID " + carId + " is in use and cannot be deleted! It will be disabled");
        }
    } 
    
    public Car SearchCar(Date pickupDateTime, Date returnDateTime, String pickupOutlet, String returnOutlet) {  
        Query query = em.createQuery("SELECT c FROM Car c WHERE (c.reservations.pickUpDate >= :retunDate AND c.reservations.returnDate >= :pickupDate AND c.reservations.returnOutlet :pickUpOutlet) OR c.reservations IS EMPTY OR (c.reservations.returnDate >= :pickupDate AND c.reservations.returnDate.currentTime() >= :pikcupDate.currentTime())");
         query.setParameter("returnDate", returnDateTime);
        query.setParameter("pickUpOutlet", pickupOutlet);
        query.setParameter("pickUpDate", pickupDateTime);
        return (Car)query.getSingleResult();
        }
            
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
