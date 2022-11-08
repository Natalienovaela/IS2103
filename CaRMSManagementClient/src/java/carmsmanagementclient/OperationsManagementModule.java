/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import entity.Model;
import entity.Category;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotExistException;
import util.exception.InputDataValidationException;
import util.exception.MakeOrModelExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Natalienovaela
 */

public class OperationsManagementModule {
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public OperationsManagementModule(ModelSessionBeanRemote modelSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote) {
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public void menuOperationsManagement() {
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
            
            number = sc.nextInt();
            
            if(number<=4 && number >=1 ) {
                System.out.println();
                doModel(number);
            } else if(number <= 7 && number >= 5) {
                System.out.println();
                doCar(number);
            }
            else {
                System.out.println("Invalid option! Please try again!\n");
            }
        }
    }
    
    public void doModel(Integer number) {
        Scanner sc = new Scanner(System.in);
        if(number == 1) {
            System.out.println("Enter make: ");
            String make = sc.next();
            System.out.println("Enter model: ");
            String model = sc.next();
            System.out.println("Enter category name: ");
            String name = sc.next();
            
            try{
               Category category = categorySessionBeanRemote.retrieveEmployeeByName(name);
                           
                Model newModel = new Model(make, model, false, category);
                Set<ConstraintViolation<Model>>constraintViolations = validator.validate(newModel);
                
                if(constraintViolations.isEmpty()) {
                    try {
                        modelSessionBeanRemote.createModel(newModel, category.getCategoryId());
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
            
        } else if(number == 3) {
            
        } else {
            
        }
    }
    
    public void doCar(Integer umber) {
        
    }
}
