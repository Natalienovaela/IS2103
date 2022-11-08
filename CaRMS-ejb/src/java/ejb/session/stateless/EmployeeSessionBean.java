/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Employee retrieveEmployeeByEmail(String email) throws EmployeeNotExistException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.email = :email");
        query.setParameter("email", email);
        
        try{
            Employee employee = (Employee)query.getSingleResult();
            return employee;
        }
        catch (Exception ex) {
            throw new EmployeeNotExistException("Employee with the email " + email + " does not exist\n");
        }
    }

}
