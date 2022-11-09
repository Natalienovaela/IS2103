/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.TransitDriverDispatch;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Natalienovaela
 */
@Stateless
public class TransitDriverDispatchSessionBean implements TransitDriverDispatchSessionBeanRemote, TransitDriverDispatchSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public void createDispatch(TransitDriverDispatch dispatch, Long outletId, Long carId) {
        em.persist(dispatch);
        Outlet outlet = em.find(Outlet.class, outletId);
        dispatch.setOutlet(outlet);
        outlet.getTransits().add(dispatch);
        Car car = em.find(Car.class, carId);
        dispatch.setTransitId(carId);
        car.setTransit(dispatch);
    }
}
