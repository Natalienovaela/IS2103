/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateful.ReservationSessionBeanLocal;
import entity.Reservation;
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
        /*for(Reservation reservation: reservations) {
            
        }*/
        return reservations;
    }
}
