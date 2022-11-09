/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.Model;
import entity.Reservation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ModelNotExistException;

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
}
