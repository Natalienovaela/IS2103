/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Employee;
import entity.Outlet;
import entity.TransitDriverDispatch;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatus;
import util.exception.EmployeeNotExistException;
import util.exception.TransitDriverDispatchNotExistException;

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
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatch t WHERE t.outlet = :=outlet AND t.completed = :false");
        query.setParameter("outlet", outlet);
        query.setParameter("false", Boolean.FALSE);
        
        List<TransitDriverDispatch> transits = query.getResultList();
        
        return transits;
    }
    
    @Override
    public void assignTransitDriver(Long transitId, Long employeeId) throws EmployeeNotExistException, TransitDriverDispatchNotExistException {
        TransitDriverDispatch transit = em.find(TransitDriverDispatch.class, transitId);
        
        if(transit != null) {
            Employee employee = em.find(Employee.class, employeeId);
            if(employee != null) {
                transit.setTransitDriver(employee);
                transit.getCar().setStatus(CarStatus.INTRANSIT);
                employee.setTransit(transit);
            } 
            else {
                throw new EmployeeNotExistException("Employee with the id " + employeeId + " does not exist");
            }
        } else {
            throw new TransitDriverDispatchNotExistException("Transit driver dispatch record with the id " + transitId + " does not exist");
        }
        
               
    }
    
    @Override
    public void updateTransitAsCompleted(Long transitId) throws TransitDriverDispatchNotExistException {
        TransitDriverDispatch transit = em.find(TransitDriverDispatch.class, transitId);
        
        if(transit != null) {
            transit.setCompleted(Boolean.TRUE);
            transit.getTransitDriver().setTransit(null);
            transit.getCar().setTransit(null);
            transit.getOutlet().getTransits().remove(transit);
            em.remove(transit);
        } else {
            throw new TransitDriverDispatchNotExistException("Transit driver dispatch record with the id " + transitId + " does not exist");
        }
    }
}
