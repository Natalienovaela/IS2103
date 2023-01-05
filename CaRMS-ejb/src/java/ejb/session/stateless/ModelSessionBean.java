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
import entity.Outlet;
import entity.Reservation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import util.exception.CategoryNotExistException;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.MakeOrModelExistException;
import util.exception.ModelNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Natalienovaela
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;
    
    @EJB
    private CarSessionBeanLocal carSessionBean;
    
    

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ModelSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void createModel(Model model, Long categoryId) throws MakeOrModelExistException, UnknownPersistenceException, InputDataValidationException {
       Set<ConstraintViolation<Model>>constraintViolations = validator.validate(model);
       
       if(constraintViolations.isEmpty()) {
            try {
                Category category = em.find(Category.class, categoryId); 
                em.persist(model);
                model.setCategory(category);
                category.getModels().add(model);
                em.flush();
            }
            catch(PersistenceException ex) {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new MakeOrModelExistException();
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
    public List<Model> retrieveAllModel() {
        Query query = em.createQuery("SELECT m FROM Model m ORDER BY m.category.categoryName, m.make, m.model ASC");
        return query.getResultList();
    }
    
    @Override
    public Model retrieveModelbyId(Long modelId) throws ModelNotExistException{
        Model model = em.find(Model.class, modelId);
        
        if(model == null) {
            throw new ModelNotExistException("Model with Model ID " + modelId + " does not exist");
        }
        else {
            model.getCategory().getModels();
            return model;
        }
    }
    
    @Override
    public Model retrieveModelbyMakeandModel(String make, String model) throws ModelNotExistException{
        Query query = em.createQuery("SELECT m FROM Model m WHERE m.make = :make AND m.model = :model");
        query.setParameter("make", make);
        query.setParameter("model", model);
        
        Model retrieveModel = (Model)query.getSingleResult();
        
        if(retrieveModel == null) {
            throw new ModelNotExistException("Model with the make " + make + "and model" + model + " does not exist");
        }
        else {
            retrieveModel.getCars().size();
            return retrieveModel;
        }
    }
    
    @Override
    public void updateModel(Model model) throws InputDataValidationException, ModelNotExistException {
        if(model != null && model.getModelId()!= null)
        {
            Set<ConstraintViolation<Model>>constraintViolations = validator.validate(model);
        
            if(constraintViolations.isEmpty())
            {
                Model modelToUpdate = retrieveModelbyId(model.getModelId());
                modelToUpdate.setMake(model.getMake());
                modelToUpdate.setModel(model.getModel());
                modelToUpdate.getCategory().getModels().remove(modelToUpdate);
                modelToUpdate.setCategory(model.getCategory());
                Category category;
                try {
                    category = categorySessionBean.retrieveCategoryByName(modelToUpdate.getCategory().getCategoryName());
                    category.getModels().add(modelToUpdate);
                } catch (CategoryNotExistException ex) {
                    Logger.getLogger(ModelSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new ModelNotExistException("Model ID not provided for model to be updated");
        }
    }
    
    @Override
    public void deleteModel(Long modelId) throws ModelNotExistException, DeleteModelException
    {
        Model modelToRemove = retrieveModelbyId(modelId);
        List<Car> cars = carSessionBean.retrieveCarByModelId(modelId);
        
        if(cars.isEmpty())
        {
            Category category;
            try {
                category = categorySessionBean.retrieveCategoryByName(modelToRemove.getCategory().getCategoryName());
                category.getModels().remove(modelToRemove);
                em.remove(modelToRemove);
            } catch (CategoryNotExistException ex) {
                Logger.getLogger(ModelSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            modelToRemove.setDisabled(Boolean.TRUE);
            throw new DeleteModelException("Model ID " + modelId + " is in use and cannot be deleted! It will be disabled");
        }
    } 
    @Override
    public List<Model> searchCar(Date pickupDateTime, Date returnDateTime, Outlet pickupOutlet, Outlet returnOutlet) {  
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.disabled = :false");
        query.setParameter("false", Boolean.FALSE);
        
        /*Query query = em.createQuery("SELECT c FROM Car c EXCEPT SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND (((:pickUpDate < r.pickUpDate AND :returnDate > r.pickUpDate) OR (:pickUpDate > r.pickUpDate AND :returnDate < r.returnDate) OR(:pickUpDate < r.returnDate AND :returnDate > r.returnDate)) OR ((r.returnOutlet != :pickUpOutlet) AND EXTRACT(YEAR FROM r.returnDate) = EXTRACT(YEAR FROM :pickUpDate) AND EXTRACT(MONTH FROM r.returnDate) = EXTRACT(MONTH FROM :pickUpDate) AND EXTRACT(DAY FROM r.returnDate) = EXTRACT(DAY FROM :pickUpDate) AND EXTRACT(HOUR FROM r.returnDate) * 60 + EXTRACT(MINUTE FROM r.returnDate) + 120 > EXTRACT(HOUR FROM :pickUpDate) * 60 + EXTRACT(MINUTE FROM :pickUpDate)))");
        query.setParameter("pickUpOutlet", pickupOutlet);
        query.setParameter("returnDate", returnDateTime);
        query.setParameter("pickUpDate", pickupDateTime);*/
        List<Car> cars = query.getResultList();
        Calendar pickUp = Calendar.getInstance();
        pickUp.setTime(pickupDateTime);
        Calendar returnTime = Calendar.getInstance();
        returnTime.setTime(returnDateTime);
        
        List<Model> models = new ArrayList<>();
        
        for(Car car: cars) {
                if(!car.getReservations().isEmpty()) {
                    List<Reservation> reservations =  car.getReservations();                        

                    for(Reservation reservation: reservations ){
                        Calendar rPickUp = Calendar.getInstance();
                        rPickUp.setTime(reservation.getPickUpDate());
                        Calendar rReturnTime = Calendar.getInstance();
                        rReturnTime.setTime(reservation.getReturnDate());
                        
                        if((pickUp.before(rPickUp) && returnTime.after(rPickUp)) || (pickUp.after(rPickUp) && returnTime.before(rReturnTime)) || (pickUp.before(rReturnTime) && returnTime.after(rReturnTime))) {
                            cars.remove(car);
                            break;
                        }
                        
                        if(reservation.getReturnOutlet() != returnOutlet) {
                            if(pickUp.get(Calendar.YEAR) == rReturnTime.get(Calendar.YEAR) && pickUp.get(Calendar.MONTH) == rReturnTime.get(Calendar.MONTH) && pickUp.get(Calendar.DAY_OF_MONTH) == rReturnTime.get(Calendar.DAY_OF_MONTH) && pickUp.get(Calendar.HOUR) * 60 + pickUp.get(Calendar.MINUTE) < 120 + rReturnTime.get(Calendar.HOUR)*60 + rReturnTime.get(Calendar.MINUTE)){
                                cars.remove(car);
                                break;
                            }
                        }
                    }
                }
                   
        }   
        
        for(Car car: cars) {
            if(!models.contains(car.getModel())){
                models.add(car.getModel());
            } 
        }
        
        for(Model model : models) {
            model.getCategory().getRentalRates().size();
        }
        return models;
    }
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Model>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
