/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.TransitDriverDispatch;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    @Override
    public void createDispatch(TransitDriverDispatch dispatch, Long outletId, Long carId) {
        em.persist(dispatch);
        Outlet outlet = em.find(Outlet.class, outletId);
        dispatch.setOutlet(outlet);
        outlet.getTransits().add(dispatch);
        Car car = em.find(Car.class, carId);
        dispatch.setTransitId(carId);
        car.setTransit(dispatch);
    }
    
    @Override
    public List<TransitDriverDispatch> retrieveAllDispatch(Long outletId) {
        Outlet outlet = em.find(Outlet.class, outletId);
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatch t WHERE t.outlet = :=outlet");
        query.setParameter("outlet", outlet);
        
        List<TransitDriverDispatch> transit = query.getResultList();
        
        return transit;
    }
}
