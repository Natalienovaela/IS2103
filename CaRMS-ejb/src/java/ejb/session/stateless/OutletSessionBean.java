/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Natalienovaela
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void createOutlet(Outlet outlet) {
        em.persist(outlet);
    }
    
    @Override
    public Outlet retrieveOutletByName(String name) {
        Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.name = :name");
        query.setParameter("name", name);
        Outlet outlet = (Outlet)query.getSingleResult();
        return outlet;
    }
}
