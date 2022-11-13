/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateful.ReservationSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import entity.Car;
import entity.Model;
import entity.Outlet;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Natalienovaela
 */
@WebService(serviceName = "ReservationWebService")
@Stateless()
public class ReservationWebService {

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "retrieveMyReservations")
    public List<Reservation> retrieveMyReservations(long customerId) {
        List<Reservation> reservations = reservationSessionBean.retrieveMyReservations(customerId);
        for(Reservation reservation: reservations) {
           em.detach(reservation); 
           Car car = reservation.getCar();
           em.detach(reservation.getCar());
           car.getReservations().clear();
        }
        return reservations;
    }
    
    @WebMethod(operationName = "searchCar")
    public List<Model> searchCar(Date pickupDateTime, Date returnDateTime, Outlet pickupOutlet, Outlet returnOutlet) {
        List<Model> models = modelSessionBean.searchCar(pickupDateTime, returnDateTime, pickupOutlet, returnOutlet);
        for(Model model : models) {
            em.detach(model);
            model.getCars().clear();
            model.getCategory().getModels().clear();
        }
        return models;
    }
}
