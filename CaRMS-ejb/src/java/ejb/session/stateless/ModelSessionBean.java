/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateful.ReservationSessionBeanLocal;
import entity.Category;
import entity.Model;
import entity.Reservation;
import java.util.Date;
import java.util.List;
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
    private ReservationSessionBeanLocal reservationSessionBean;

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
                modelToUpdate.getCategory().getModels().add(modelToUpdate);
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
        List<Reservation> reservations = reservationSessionBean.retrieveReservationByModelId(modelId);
        
        if(reservations.isEmpty())
        {
            modelToRemove.getCategory().getModels().remove(modelToRemove);
            em.remove(modelToRemove);
        }
        else
        {
            modelToRemove.setDisabled(Boolean.TRUE);
            throw new DeleteModelException("Model ID " + modelId + " is in use and cannot be deleted! It will be disabled");
        }
    } 
    @Override
    public List<Model> SearchCar(Date pickupDateTime, Date returnDateTime, String pickupOutlet, String returnOutlet) {  
        Query query = em.createQuery(
                "SELECT m FROM Model m"
                        + "EXCEPT"
                        + "SELECT m FROM Model m JOIN m.cars c JOIN c.reservations r"
                        + "WHERE r.reservations IS NOT EMPTY AND "
                        + "(((:pickUpDate < r.pickUpDate AND :returnDate > r.pickUpDate) OR (:pickUpDate > r.pickUpDate AND :returnDate < r.returnDate) OR(:pickUpDate < r.returnDate AND :returnDate > r.returnDate))"
                        + "OR (r.returnOutlet != :pickUpOutlet"
                        + "AND EXTRACT(YEAR FROM r.returnDate) = EXTRACT(YEAR FROM :pickUpDate) "
                        + "AND EXTRACT(MONTH FROM r.returnDate) = EXTRACT(MONTH FROM :pickUpDate"
                        + "AND EXTRACT(DAY FROM r.returnDate) = EXTRACT(DAY FROM :pickUpDate)"
                        + "AND EXTRACT(HOUR FROM r.returnDate) * 60 + EXTRACT(MINUTE FROM r.returnDate) + 120 > EXTRACT(HOUR FROM :pickUpDate) * 60 + EXTRACT(MINUTE FROM :pickUpDate))))"
                        + "ORDER BY m.category ASC, m ASC");
        query.setParameter("pickUpOutlet", pickupOutlet);
        query.setParameter("returnDate", returnDateTime);
        query.setParameter("pickUpDate", pickupDateTime);
        List<Model> models = query.getResultList();
        
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
