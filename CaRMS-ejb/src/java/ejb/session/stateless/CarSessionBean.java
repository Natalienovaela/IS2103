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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.enumeration.CarStatus;
import util.exception.CarExistException;
import util.exception.CarNotExistException;
import util.exception.CategoryNotExistException;
import util.exception.DeleteCarException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotExistException;
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
    public Car retrieveCarByPlate(String carPlate) {
        return (Car) em.createQuery("SELECT c FROM Car c WHERE c.licensePlateNumber = :carPlate").setParameter("carPlate", carPlate).getSingleResult();
    }
    
    @Override
    public List<Car> retrieveAllCar() {
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.model.category.categoryName, c.model.make, c.model.model, c.licensePlateNumber ASC");
        return query.getResultList();
    }
    
    @Override
    public List<Car> retrieveAllSearchCarByCategory(List<Model> models, String categoryName) throws CategoryNotExistException {
        Category category = categorySessionBean.retrieveCategoryByName(categoryName);
        if(category == null) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.model.category =:category");
        query.setParameter("category", category);
        return query.getResultList();
        }
        else {
            throw new CategoryNotExistException("Category with the name " + categoryName + " does not exist");
        }
            
    }
    
    @Override
    public List<Car> retrieveAllSearchCarByModelandMake(List<Model> models, String model, String make) throws ModelNotExistException {
        Model chosenModel = modelSessionBean.retrieveModelbyMakeandModel(make, model);
        if(chosenModel == null) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.model =:model");
        query.setParameter("model", chosenModel);
        return query.getResultList();
        }
        else {
            throw new ModelNotExistException("Model with the model " + model + "and with the make" + make + "does not exist");
        }
            
    }
    
    @Override 
    public List<Car> retrieveCarByModelId(Long modelId) throws ModelNotExistException{
        return em.createQuery("SELECT c FROM Car c WHERE c.model.modelId = :modelId").setParameter("modelId", modelId).getResultList();
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
                
                try {
                    Model model = modelSessionBean.retrieveModelbyMakeandModel(updateCar.getModel().getMake(), updateCar.getModel().getModel());
                    model.getCars().add(car);
                } catch (ModelNotExistException ex) {
                    Logger.getLogger(CarSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                
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
     
    @Override
    public void pickUpCar(Long carId, Long reservationId){
        Reservation reservation = em.find(Reservation.class, reservationId);
        Car car = em.find(Car.class, carId);
        reservation.setPickedUp(Boolean.TRUE);
        car.setStatus(CarStatus.ONRENTAL);
        car.setCurrOutlet(null);
    }
    
    public void returnCar(Long carId, Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        Car car = em.find(Car.class, carId);
        reservation.setReturned(Boolean.TRUE);
        car.setStatus(CarStatus.INOUTLET);
        car.setCurrOutlet(reservation.getReturnOutlet());
        reservation.getCustomer().getReservations().remove(reservation);
        reservation.setCustomer(null);
        em.remove(reservation);
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
