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
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotExistException;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.MakeOrModelExistException;
import util.exception.ModelNotExistException;
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
            System.out.println("11. Back");
            
            number = sc.nextInt();
            
            if(number<=4 && number >=1 ) {
                System.out.println();
                doModel(number);
            } else if(number <= 7 && number >= 5) {
                System.out.println();
                doCar(number);
            }
            else if(number == 11) {
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
    
    public void doCar(Integer umber) {
        
    }
}
