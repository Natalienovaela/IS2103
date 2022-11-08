/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.Category;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
import javax.persistence.Query;
import util.enumeration.EmployeeRole;


/**
 *
 * @author Natalienovaela
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct(){
        
        
        if(em.find(Outlet.class, 1l) == null) {
            initialiseOutlet();
        }
        
        if(em.find(Employee.class, 1l) == null) {  
            initialiseEmployee();
        }
        
        if(em.find(Partner.class, 1l) == null) {
            initialisePartner();
        }
        
        if(em.find(Category.class, 1l) == null) {
            initialiseCategory();
        }
    }
    
    public void initialiseOutlet() {
        Outlet outlet = new Outlet("Outlet A", "address A", null, null);
        em.persist(outlet);
        em.flush();
        Outlet outlet2 = new Outlet("Outlet B", "address B", null, null);
        em.persist(outlet2);
        em.flush();
        Outlet outlet3 = new Outlet("Outlet C","address C", "10:00", "22:00");
        em.persist(outlet3); 
        em.flush();
    }
    
    public void initialiseEmployee() {
        Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.outletName = :name");
        query.setParameter("name", "Outlet A");
        Outlet outletA = (Outlet)query.getSingleResult();

        Query query2 = em.createQuery("SELECT o FROM Outlet o WHERE o.outletName = :name");
        query2.setParameter("name", "Outlet B");
        Outlet outletB = (Outlet)query2.getSingleResult();

        Query query3 = em.createQuery("SELECT o FROM Outlet o WHERE o.outletName = :name");
        query3.setParameter("name", "Outlet C");
        Outlet outletC = (Outlet)query3.getSingleResult();

        Employee employeeA1 = new Employee("Employee A1", "a1@gmail.com", "a1", EmployeeRole.SALESMANAGER);
        em.persist(employeeA1);
        employeeA1.setOutlet(outletA);
        outletA.getEmployees().add(employeeA1);
        em.flush();

        Employee employeeA2 = new Employee("Employee A2", "a2@gmail.com", "a2", EmployeeRole.OPERATIONSMANAGER);
        em.persist(employeeA2);
        employeeA2.setOutlet(outletA);
        outletA.getEmployees().add(employeeA2);
        em.flush();

        Employee employeeA3 = new Employee("Employee A3", "a3@gmail.com", "a3", EmployeeRole.CUSTOMERSERVICEEXECUTIVE);
        em.persist(employeeA3);
        employeeA3.setOutlet(outletA);
        outletA.getEmployees().add(employeeA3);
        em.flush();

        Employee employeeA4 = new Employee("Employee A4", "a4@gmail.com", "a4", null);
        em.persist(employeeA4);
        employeeA4.setOutlet(outletA);
        outletA.getEmployees().add(employeeA4);
        em.flush();

        Employee employeeA5 = new Employee("Employee A5", "a5@gmail.com", "a5", null);
        em.persist(employeeA5);
        employeeA5.setOutlet(outletA);
        outletA.getEmployees().add(employeeA5);
        em.flush();

        Employee employeeB1 = new Employee("Employee B1", "b1@gmail.com", "b1", EmployeeRole.SALESMANAGER);
        em.persist(employeeB1);
        employeeB1.setOutlet(outletB);
        outletB.getEmployees().add(employeeB1);
        em.flush();

        Employee employeeB2 = new Employee("Employee B2", "b2@gmail.com", "b2", EmployeeRole.OPERATIONSMANAGER);
        em.persist(employeeB2);
        employeeB2.setOutlet(outletB);
        outletB.getEmployees().add(employeeB2);
        em.flush();

        Employee employeeB3 = new Employee("Employee B3", "b3@gmail.com", "b3", EmployeeRole.CUSTOMERSERVICEEXECUTIVE);
        em.persist(employeeB3);
        employeeB3.setOutlet(outletB);
        outletB.getEmployees().add(employeeB3);
        em.flush();

        Employee employeeC1 = new Employee("Employee C1", "c1@gmail.com", "c1", EmployeeRole.SALESMANAGER);
        em.persist(employeeC1);
        employeeC1.setOutlet(outletC);
        outletC.getEmployees().add(employeeC1);
        em.flush();

        Employee employeeC2 = new Employee("Employee C2", "c2@gmail.com", "c2", EmployeeRole.OPERATIONSMANAGER);
        em.persist(employeeC2);
        employeeC2.setOutlet(outletC);
        outletC.getEmployees().add(employeeC2);
        em.flush();

        Employee employeeC3 = new Employee("Employee C3", "c3@gmail.com", "c3", EmployeeRole.CUSTOMERSERVICEEXECUTIVE);
        em.persist(employeeC3);
        employeeC3.setOutlet(outletC);
        outletC.getEmployees().add(employeeC3);
        em.flush();
        
        /*Employee systemAdmin = new Employee("SystemAdmin", "systemAdmin@gmail.com", "admin", EmployeeRole.SYSTEMADMINISTRATOR);
        em.persist(systemAdmin);
        systemAdmin.setOutlet(outletA);
        em.flush();*/
    }
    
    public void initialisePartner() {
        Partner partner = new Partner("Holiday.com", "holiday@gmail.com", "holiday");
        em.persist(partner);
        em.flush();
    }
    
    public void initialiseCategory() {
        Category category = new Category("Standard Sedan");
        em.persist(category);
        em.flush();
        category = new Category("Family Sedan");
        em.persist(category);
        category = new Category("Luxury Sedan");
        em.persist(category);
        em.flush();
        category = new Category("SUV and Minivan");
        em.persist(category);
        em.flush();
    }

}
