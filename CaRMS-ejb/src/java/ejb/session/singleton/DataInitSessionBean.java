/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.Car;
import entity.Category;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import entity.Partner;
import entity.RentalRates;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.persistence.Query;
import util.enumeration.EmployeeRole;
import util.enumeration.CarAvailabilityStatus;


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
        
        if(em.find(Model.class, 1l) == null ) {
            initialiseModel();
        }
        
        if(em.find(Car.class, 1l) == null) {
            initialiseCar();
        }
        
        
    }
    
    
    public void initialiseOutlet() {
        Outlet outlet = new Outlet("Outlet A", "address A", null, null);
        em.persist(outlet);
        em.flush();
        Outlet outlet2 = new Outlet("Outlet B", "address B", null, null);
        em.persist(outlet2);
        em.flush();
        Outlet outlet3 = new Outlet("Outlet C","address C", "08:00", "22:00");
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
        
        Employee systemAdmin = new Employee("SystemAdmin", "systemAdmin@gmail.com", "admin", EmployeeRole.SYSTEMADMINISTRATOR);
        em.persist(systemAdmin);
        systemAdmin.setOutlet(outletA);
        em.flush();
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
    
    public void initialiseModel() {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :categoryName");
        query.setParameter("categoryName", "Standard Sedan");
        Category category = (Category) query.getSingleResult();
        
        Model model = new Model("Toyota", "Corolla", category);
        em.persist(model);
        model.setCategory(category);
        category.getModels().add(model);
        em.flush();
        
        model = new Model("Honda", "Civic", category);
        em.persist(model);
        model.setCategory(category);
        category.getModels().add(model);
        em.flush();
        
        model = new Model("Nissan", "Sunny", category);
        em.persist(model);
        model.setCategory(category);
        category.getModels().add(model);
        em.flush();
        
        query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :categoryName");
        query.setParameter("categoryName", "Luxury Sedan");
        category = (Category) query.getSingleResult();
        
        model = new Model("Mercedes", "E class", category);
        em.persist(model);
        model.setCategory(category);
        category.getModels().add(model);
        em.flush();
        
        model = new Model("BMW", "5 Series", category);
        em.persist(model);
        model.setCategory(category);
        category.getModels().add(model);
        em.flush();
        
        model = new Model("Audi", "A6", category);
        em.persist(model);
        model.setCategory(category);
        category.getModels().add(model);
        em.flush();
    }
    
    public void initialiseCar() {
        Query query = em.createQuery("SELECT m FROM Model m WHERE m.make = :make AND m.model = :model");
        query.setParameter("make", "Toyota");
        query.setParameter("model", "Corolla");
        Model model = (Model)query.getSingleResult();
        
        Query query2 = em.createQuery("SELECT o FROM Outlet o WHERE o.outletName = :name");
        query2.setParameter("name", "Outlet A");
        Outlet outletA = (Outlet)query2.getSingleResult();
        
        Car car = new Car("SS00A1TC", model, outletA);
        car.setAvailStatus(CarAvailabilityStatus.AVAILABLE);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        car = new Car("SS00A2TC", model, outletA);
        car.setAvailStatus(CarAvailabilityStatus.AVAILABLE);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        car = new Car("SS00A3TC", model, outletA);
        car.setAvailStatus(CarAvailabilityStatus.AVAILABLE);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        query = em.createQuery("SELECT m FROM Model m WHERE m.make = :make AND m.model = :model");
        query.setParameter("make", "Honda");
        query.setParameter("model", "Civic");
        model = (Model)query.getSingleResult();
        
        query2 = em.createQuery("SELECT o FROM Outlet o WHERE o.outletName = :name");
        query2.setParameter("name", "Outlet B");
        Outlet outletB = (Outlet)query2.getSingleResult();
        
        car = new Car("SS00B1HC", model, outletB);
        car.setAvailStatus(CarAvailabilityStatus.AVAILABLE);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        car = new Car("SS00B2HC", model, outletB);
        car.setAvailStatus(CarAvailabilityStatus.AVAILABLE);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        car = new Car("SS00B3HC", model, outletB);
        car.setAvailStatus(CarAvailabilityStatus.AVAILABLE);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        query = em.createQuery("SELECT m FROM Model m WHERE m.make = :make AND m.model = :model");
        query.setParameter("make", "Nissan");
        query.setParameter("model", "Sunny");
        model = (Model)query.getSingleResult();
        
        query2 = em.createQuery("SELECT o FROM Outlet o WHERE o.outletName = :name");
        query2.setParameter("name", "Outlet C");
        Outlet outletC = (Outlet)query2.getSingleResult();
        
        car = new Car("SS00C1NS", model, outletC);
        car.setAvailStatus(CarAvailabilityStatus.AVAILABLE);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        car = new Car("SS00C2NS", model, outletC);
        car.setAvailStatus(CarAvailabilityStatus.AVAILABLE);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        car = new Car("SS00C3NS", model, outletC);
        car.setAvailStatus(CarAvailabilityStatus.REPAIR);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        query = em.createQuery("SELECT m FROM Model m WHERE m.make = :make AND m.model = :model");
        query.setParameter("make", "Mercedes");
        query.setParameter("model", "E Class");
        model = (Model)query.getSingleResult();
        
        car = new Car("LS00A4ME", model, outletA);
        car.setAvailStatus(CarAvailabilityStatus.REPAIR);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        query = em.createQuery("SELECT m FROM Model m WHERE m.make = :make AND m.model = :model");
        query.setParameter("make", "BMW");
        query.setParameter("model", "5 Series");
        model = (Model)query.getSingleResult();
        
        car = new Car("LS00B4B5", model, outletB);
        car.setAvailStatus(CarAvailabilityStatus.REPAIR);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
        
        query = em.createQuery("SELECT m FROM Model m WHERE m.make = :make AND m.model = :model");
        query.setParameter("make", "Audi");
        query.setParameter("model", "A6");
        model = (Model)query.getSingleResult();
        
        car = new Car("LS00C4A6", model, outletC);
        car.setAvailStatus(CarAvailabilityStatus.REPAIR);
        model.getCars().add(car);
        em.persist(car);
        em.flush();
    }
    
    public void initialiseRentalRates() throws ParseException {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :categoryName");
        query.setParameter("categoryName", "Standard Sedan");
        Category category = (Category) query.getSingleResult();
        
        RentalRates rental = new RentalRates("Standard Sedan - Default", "Default", category, BigDecimal.valueOf(100), null, null);
        em.persist(rental);
        em.flush();
        SimpleDateFormat date = new SimpleDateFormat("dd/mm/yyyy HH:mm");
        
        rental = new RentalRates("Standard Sedan - Weekend Promo", "Promotion", category, BigDecimal.valueOf(80), date.parse("09/12/2022 12:00"), date.parse("11/12/2022 00:00"));
        em.persist(rental);
        em.flush();
        
        query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :categoryName");
        query.setParameter("categoryName", "Family Sedan");
        category = (Category) query.getSingleResult();
        
        rental = new RentalRates("Family Sedan - Default", "Default", category, BigDecimal.valueOf(200), null, null);
        em.persist(rental);
        em.flush();
        
        query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :categoryName");
        query.setParameter("categoryName", "Luxury Sedan");
        category = (Category) query.getSingleResult();
        
        rental = new RentalRates("Luxury Sedan - Default", "Default", category, BigDecimal.valueOf(300), null, null);
        em.persist(rental);
        em.flush();
        
        rental = new RentalRates("Luxury Sedan - Monday", "Peak",category, BigDecimal.valueOf(310), date.parse("05/12/2022 00:00"), date.parse("05/12/2022 23:59"));
        em.persist(rental);
        em.flush();
        
        rental = new RentalRates("Luxury Sedan - Tuesday", "Peak",category, BigDecimal.valueOf(320), date.parse("06/12/2022 00:00"), date.parse("06/12/2022 23:59"));
        em.persist(rental);
        em.flush();
        
        rental = new RentalRates("Luxury Sedan - Wednesday", "Peak",category, BigDecimal.valueOf(330), date.parse("07/12/2022 00:00"), date.parse("07/12/2022 23:59"));
        em.persist(rental);
        em.flush();
        
        rental = new RentalRates("Luxury Sedan - WeekdayPromo", "Peak" ,category, BigDecimal.valueOf(320), date.parse("07/12/2022 12:00"), date.parse("08/12/2022 12:00"));
        em.persist(rental);
        em.flush();
       
        query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :categoryName");
        query.setParameter("categoryName", "SUV and Minivan");
        category = (Category) query.getSingleResult();
        
        rental = new RentalRates("Luxury Sedan - Default", "Default" ,category, BigDecimal.valueOf(400), null, null);
        em.persist(rental);
        em.flush();
    }
}

