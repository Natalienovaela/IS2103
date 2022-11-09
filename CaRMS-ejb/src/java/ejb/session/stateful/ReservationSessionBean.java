/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.Car;
import entity.Model;
import entity.RentalRates;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarNotExistException;
import util.exception.ModelNotExistException;
import util.exception.RentalRatesNotExistException;

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
    public List<Reservation> retrieveReservationByRentalRateId(Long rentalRateId) throws RentalRatesNotExistException {
        RentalRates rentalRate = em.find(RentalRates.class, rentalRateId);
        if(rentalRate == null) {
            throw new RentalRatesNotExistException("Rental Rate with Rental Rate ID " + rentalRateId + " does not exist");
        } else {
            Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.rentalRates = :rentalRate");
            query.setParameter("rentalRate", rentalRate);
            return query.getResultList();
        }
    }
    
    @Override
    public List<Reservation> retrieveReservationByCarId(Long carId) throws CarNotExistException {
        Car car = em.find(Car.class, carId);
        if(car == null) {
            throw new CarNotExistException("Car with car ID " + carId + " does not exist");
        } else {
            Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.model.car = :car");
            query.setParameter("car", car);
            return query.getResultList();
        }
    }
}
