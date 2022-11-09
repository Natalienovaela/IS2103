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
import entity.RentalRates;
import entity.Reservation;
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
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public RentalRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void createRentalRate(RentalRates rentalRate, Long categoryId) throws RentalRateExistException, UnknownPersistenceException, InputDataValidationException {
       Set<ConstraintViolation<RentalRates>>constraintViolations = validator.validate(rentalRate);
       
       if(constraintViolations.isEmpty()) {
            try {
                Category category = em.find(Category.class, categoryId);
                em.persist(rentalRate);
                rentalRate.setCategory(category);
                category.getRentalRates().add(rentalRate);
                em.flush();
            }
            catch(PersistenceException ex) {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new RentalRateExistException();
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
    public List<RentalRates> retrieveAllRentalRate() {
        Query query = em.createQuery("SELECT r FROM RentalRates r ORDER BY r.category.categoryName, r.StartDateTime, r.EndDateTime ASC");
        return query.getResultList();
    }
    
    @Override
    public RentalRates retrieveRentalRateById(Long rentalRateId) throws RentalRateNotExistException{
        RentalRates rentalRate = em.find(RentalRates.class, rentalRateId);
        
        if(rentalRate == null) {
            throw new RentalRateNotExistException("Rental Rate with Rental Rate ID " + rentalRateId + " does not exist");
        }
        else {
            return rentalRate;
        }
    }
    
    @Override
    public void updateRentalRate(RentalRates rentalRate) throws InputDataValidationException, RentalRateNotExistException {
        if(rentalRate != null && rentalRate.getRentalRateId()!= null)
        {
            Set<ConstraintViolation<RentalRates>>constraintViolations = validator.validate(rentalRate);
        
            if(constraintViolations.isEmpty())
            {
                RentalRates updateRentalRate = retrieveRentalRateById(rentalRate.getRentalRateId());
                updateRentalRate.setRentalRateType(rentalRate.getRentalRateType());
                updateRentalRate.setName(rentalRate.getName());
                updateRentalRate.setRatePerDay(rentalRate.getRatePerDay());
                updateRentalRate.setStartDateTime(rentalRate.getStartDateTime());
                updateRentalRate.setEndDateTime(rentalRate.getEndDateTime());
                updateRentalRate.getCategory().getRentalRates().remove(updateRentalRate);
                updateRentalRate.setCategory(rentalRate.getCategory());
                updateRentalRate.getCategory().getRentalRates().add(rentalRate);
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new RentalRateNotExistException("Rental Rate ID not provided for rental rate to be updated");
        }
    }
    
    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotExistException, DeleteRentalRateException
    {
        RentalRates removeRentalRate = retrieveRentalRateById(rentalRateId);
        List<Reservation> reservations = reservationSessionBean.retrieveReservationByRentalRateId(rentalRateId);
        
        if(reservations.isEmpty())
        {
            removeRentalRate.getCategory().getRentalRates().remove(removeRentalRate);
            em.remove(removeRentalRate);
        }
        else
        {
            removeRentalRate.setDisabled(Boolean.TRUE);
            throw new DeleteRentalRateException("Rental Rate ID " + rentalRateId + " is in use and cannot be deleted! It will be disabled");
        }
    } 
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRates>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
}
