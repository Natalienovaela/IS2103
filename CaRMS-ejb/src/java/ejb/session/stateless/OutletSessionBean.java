/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Stateless;
import util.exception.OutletNotExistException;
import entity.Outlet;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OutsideOutletAvailability;
/**
 *
 * @author Natalienovaela
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void createOutlet(Outlet outlet) {
        em.persist(outlet);
    }

    @Override
    public Outlet retrieveOutletByName(String name) throws OutletNotExistException {
        Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.outletName = :name");
        query.setParameter("name", name);
        Outlet outlet = (Outlet)query.getSingleResult();
        if(outlet != null) {
            outlet.getEmployees().size();
            return outlet;
        } else {
        throw new OutletNotExistException("Outlet with the name " + name + " does not exist\n");
        }

    }
    
    public void checkOutletAvailability(Date date, Long outletId) throws OutsideOutletAvailability{
        Outlet outlet = em.find(Outlet.class, outletId);
        
        if(outlet.getOpeningHours() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if(cal.get(Calendar.HOUR)*60 + cal.get(Calendar.MINUTE) < Integer.parseInt(outlet.getOpeningHours().substring(0, 2)) * 60 + Integer.parseInt(outlet.getOpeningHours().substring(3,5)) || cal.get(Calendar.HOUR)*60 + cal.get(Calendar.MINUTE) > Integer.parseInt(outlet.getClosingHours().substring(0, 2)) * 60 + Integer.parseInt(outlet.getClosingHours().substring(3,5))) {
                throw new OutsideOutletAvailability("Your chosen time to pickup or return car is outside of Outlet's opening hour and closing hour: " + outlet.getOpeningHours() + " and " + outlet.getClosingHours() + " respectively.\n");
            }
        }
    }
}
