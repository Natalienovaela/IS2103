/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Car;
import ejb.session.stateless.TransitDriverDispatchSessionBeanRemote;
import entity.Model;
import entity.Category;
import entity.Outlet;
import java.util.Collections;
import java.util.Comparator;
import entity.Employee;
import entity.TransitDriverDispatch;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarAvailabilityStatus;
import util.exception.CarExistException;
import util.exception.CarNotExistException;
import util.exception.CategoryNotExistException;
import util.exception.DeleteCarException;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.MakeOrModelExistException;
import util.exception.ModelNotExistException;
import util.exception.OutletNotExistException;
import util.exception.UnknownPersistenceException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.EmployeeNotExistException;
import util.exception.TransitDriverDispatchNotExistException;

/**
 *
 * @author Natalienovaela
 */

public class OperationsManagementModule {
    private CarSessionBeanRemote carSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote;
    private EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;   
    private final Employee employee;
    
    public OperationsManagementModule(Employee employee, ModelSessionBeanRemote modelSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote, EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this.employee = employee;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.transitDriverDispatchSessionBeanRemote = transitDriverDispatchSessionBeanRemote;
        this.ejbTimerSessionBeanRemote = ejbTimerSessionBeanRemote;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public void menuOperationsManagement() throws ModelNotExistException, CarNotExistException {
        Scanner sc = new Scanner(System.in);
        Integer number;
        
        while(true) {
            System.out.println("*** CaRMS Operations Management Module ***\n");
            System.out.println("1. Create New Model");
            System.out.println("2. View All Models");
            System.out.println("3. Update Model");
            System.out.println("4. Delete Model");
            System.out.println("5. Create New Car");
            System.out.println("6. View All Cars");
            System.out.println("7. View Car Details");
            System.out.println("8. View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("9. Assign Transit Driver");
            System.out.println("10. Update Transit As Completed");
            System.out.println("11. Allocate Cars Manually");
            System.out.println("12. Back");
            
            number = sc.nextInt();
            
            if(number<=4 && number >=1 ) {
                System.out.println();
                doModel(number);
            } else if(number <= 7 && number >= 5) {
                System.out.println();
                doCar(number);
            } else if(number <= 11 && number >= 8) {
                doTransit(number);
            }
            else if(number == 12) {
                break;
            }
            else{
                System.out.println("Invalid option! Please try again!\n");
            }
        }
    }
    
    public void doModel(Integer number) {
        Scanner sc = new Scanner(System.in);
        if(number == 1) {
            System.out.println("Enter make: ");
            String make = sc.nextLine();
            System.out.println("Enter model: ");
            String model = sc.nextLine();
            System.out.println("Enter category name: ");
            String name = sc.nextLine();
            
            try{
               Category category = categorySessionBeanRemote.retrieveCategoryByName(name);
                           
                Model newModel = new Model(make, model, false, category);
                Set<ConstraintViolation<Model>>constraintViolations = validator.validate(newModel);
                
                if(constraintViolations.isEmpty()) {
                    try {
                        modelSessionBeanRemote.createModel(newModel, category.getCategoryId());
                        System.out.println("Model is created successfully!\n");
                    } 
                    catch(UnknownPersistenceException ex){
                        System.out.println("An unknown error has occurred while creating the new model!: " + ex.getMessage() + "\n");
                    }
                    catch(InputDataValidationException ex){
                        System.out.println(ex.getMessage() + "\n");
                    } 
                    catch (MakeOrModelExistException ex) {
                       System.out.println("An error has occurred while creating the new model!: The make or model already exist\n");
                   }
                }

            } 
            catch(CategoryNotExistException ex) {
                System.out.println(ex.getMessage());
            }
            

        } else if(number == 2) {
            List<Model> models = modelSessionBeanRemote.retrieveAllModel();
            for(Model model: models) {
                System.out.println("Model ID: " + model.getModelId() + "Category: " + model.getCategory().getCategoryName() + " Make: " + model.getMake() + " Model: " + model.getModel());
            }
            
        } else if(number == 3) {
            System.out.println("Enter Model ID: ");
            Long modelId = sc.nextLong();
            sc.nextLine();
            try{
                Model model = modelSessionBeanRemote.retrieveModelbyId(modelId);
                
                System.out.println("Enter new make: (blank if no change)");
                String input = sc.nextLine().trim();
                
                if(input.length() > 0){
                    model.setMake(input);
                }
                
                System.out.println("Enter new model: (blank if no change)");
                input = sc.nextLine().trim();
                
                if(input.length() > 0){
                    model.setModel(input);
                }
                
                System.out.println("Enter new category name: (blank if no change)");
                input = sc.nextLine().trim();
                
                if(input.length() > 0){
                    try{
                        Category category = categorySessionBeanRemote.retrieveCategoryByName(input);
                        model.setCategory(category);
                        try{
                            modelSessionBeanRemote.updateModel(model);
                            System.out.println("Model is updated successfully!\n");
                        }
                        catch(InputDataValidationException ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                        catch(ModelNotExistException ex) {
                            System.out.println(ex.getMessage()+ "\n");
                        }
                    }
                    catch(CategoryNotExistException ex) {
                        System.out.println(ex.getMessage()+ "\n");
                    }
                }
                
            } catch(ModelNotExistException ex) {
                System.out.println(ex.getMessage());
            }
            
        } else {
            System.out.println("Enter Model ID: ");
            Long modelId = sc.nextLong();
            try{
                modelSessionBeanRemote.deleteModel(modelId);
                System.out.println("Model is deleted successfully!\n");
            } 
            catch(DeleteModelException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
            catch(ModelNotExistException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
    }
    
    public void doCar(Integer number) throws CarNotExistException {
        Scanner sc = new Scanner(System.in);
        if(number == 5) {
                System.out.println("Enter license plate number: ");
                String licensePlateNumber = sc.nextLine();
                System.out.println("Enter make: ");
                String make = sc.nextLine();
                System.out.println("Enter model: ");
                String model = sc.nextLine();
                System.out.println("Enter status: ");
                String availStatus = sc.nextLine();
                CarAvailabilityStatus status = checkStatus(availStatus);
                System.out.println("Enter outlet: ");
                String outlet = sc.nextLine();

                try{
                   Model models = modelSessionBeanRemote.retrieveModelbyMakeandModel(make, model);
                   Outlet outlets = outletSessionBeanRemote.retrieveOutletByName(outlet);
                   Car newCar = new Car(licensePlateNumber, models, outlets);
                   newCar.setAvailStatus(status);
                   Set<ConstraintViolation<Car>>constraintViolations = validator.validate(newCar);
                   
                    if(constraintViolations.isEmpty()) {
                        try {
                            carSessionBeanRemote.createCar(newCar, models.getModelId());
                            System.out.println("Car is created successfully!\n");
                        } 
                        catch(UnknownPersistenceException ex){
                            System.out.println("An unknown error has occurred while creating the new car!: " + ex.getMessage() + "\n");
                        }
                        catch(InputDataValidationException ex){
                            System.out.println(ex.getMessage() + "\n");
                        } 
                        catch(CarExistException ex) {
                           System.out.println("An error has occurred while creating the new car!: The car already exist\n");
                       }
                    }
                } 
                catch(ModelNotExistException ex) {
                    System.out.println(ex.getMessage());
                }
        } else if(number == 6) {
            List<Car> cars = carSessionBeanRemote.retrieveAllCar();
            for(Car car: cars) {
                System.out.println("Car ID: " + car.getCarId() + "Model: " + car.getModel().getModel() + " Make: " + car.getModel().getMake() + "Status: " + car.getStatus() + "License Plate Number: " + car.getLicensePlateNumber());
            }
            
        } else if(number == 7) {
           System.out.println("Enter Car ID: ");
           Long carId = sc.nextLong();
           sc.nextLine();
           Car car = carSessionBeanRemote.retrieveCarById(carId);
           while(true) {
           System.out.println("Model: " + car.getModel().getModel() + " Make: " + car.getModel().getMake() + "Status: " + car.getStatus() + "License Plate Number: " + car.getLicensePlateNumber());
           System.out.println("1. Update Car");
           System.out.println("2. Delete Car");
           System.out.println("3.Back");
           Integer num;
           num = sc.nextInt();
           
           if(num == 1) {
               updateCar(car);
           } else if(num == 2) {
               deleteCar(car.getCarId());
           } else if(num == 3) {
               break;
           } else {
               System.out.println("input invalid! please try again");
           }
           }
        }
    }
           
           public void updateCar(Car car) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter new  license plate number: (blank if no change)");
                String input = sc.nextLine().trim();
                
                if(input.length() > 0){
                    car.setLicensePlateNumber(input);
                }
                
                System.out.println("Enter new make: (blank if no change)");
                input = sc.nextLine().trim();
                
                if(input.length() > 0){
                    car.getModel().setMake(input);
                }
                
                System.out.println("Enter new model: (blank if no change)");
                input = sc.nextLine().trim();
                
                if(input.length() > 0){
                    car.getModel().setModel(input);
                }
                
                    System.out.println("Enter Status: (blank if no change)");
                    input = sc.nextLine().trim();
                    
                    if(input.length() > 0){
                        CarAvailabilityStatus status = checkStatus(input);
                        car.setAvailStatus(status);
                    }
                 
                System.out.println("Enter new outlet: (blank if no change)");
                input = sc.nextLine().trim();
                
                if(input.length() > 0){
                        Outlet outlet = outletSessionBeanRemote.retrieveOutletByName(input);
                        car.setCurrOutlet(outlet);
                        try{
                            carSessionBeanRemote.updateCar(car);
                            System.out.println("Car is updated successfully!\n");
                        }
                        catch(InputDataValidationException ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                        catch(CarNotExistException ex) {
                            System.out.println(ex.getMessage()+ "\n");
                        }
                    }
           }
           
           
        public void deleteCar(long carId) {
            try{
                carSessionBeanRemote.deleteCar(carId);
                System.out.println("Car is deleted successfully!\n");
            } 
            catch(DeleteCarException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
            catch(CarNotExistException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
 
    public CarAvailabilityStatus checkStatus(String status) {
        while(true) {
            if(status.equals("Available"))
            {
                 return CarAvailabilityStatus.AVAILABLE;
            }
            else if(status.equals("Repair"))
            {
                return CarAvailabilityStatus.REPAIR;
            }
            else
            {
                System.out.println("Invalid option for Status, please fill again!\n");
                System.out.println("Enter status: ");
            }    
    }

    }
    
    public void doTransit(Integer number) {
        Scanner sc = new Scanner(System.in);
        if(number == 8) {
            List<TransitDriverDispatch> transits = transitDriverDispatchSessionBeanRemote.retrieveAllDispatch(employee.getOutlet().getOutletId());
            System.out.println();
            
            for(TransitDriverDispatch transit : transits) {
                System.out.println("Transit ID " + transit.getTransitId() + "with car license plate number of " + transit.getCar().getLicensePlateNumber());
            }
        }
        else if(number == 9){
            System.out.println("Enter transit driver dispatch record ID: ");
            Long transitId = sc.nextLong();
            try{
                Long employeeId = employee.getOutlet().getEmployees().get(0).getEmployeeId();
                transitDriverDispatchSessionBeanRemote.assignTransitDriver(transitId, employeeId);
                System.out.println("Employee with the ID " + employeeId + " is assigned as transit driver for the transit ID " + transitId + "\n");
            }
            catch(EmployeeNotExistException ex) {
                System.out.println(ex.getMessage());
            }
            catch(TransitDriverDispatchNotExistException ex) {
                System.out.println(ex.getMessage());
            }
        }
        else if(number == 10) {
            System.out.println("Enter transit driver dispatch record ID: ");
            Long transitId = sc.nextLong();
            try {
                transitDriverDispatchSessionBeanRemote.updateTransitAsCompleted(transitId);
            } catch(TransitDriverDispatchNotExistException ex) {
                System.out.println(ex.getMessage());
            }
            
        }
        else if(number == 11) {
            Date date;
            while(true) {
                System.out.println("Enter Date: (dd/mm/yyyy format)");
                String currentDate = sc.nextLine();
                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
                try { 
                    date = format.parse(currentDate);
                    break;
                } catch (ParseException ex) {
                    System.out.println("Date is not according to the right format");
                }
            }
            
            ejbTimerSessionBeanRemote.allocateCars(date);
            System.out.println();
            System.out.println("Today's allocation is successfully done!");    
        }

    }
}
        

