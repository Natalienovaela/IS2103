/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Car;
import entity.Category;
import entity.Model;
import entity.Outlet;
import entity.RentalRates;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarAvailabilityStatus;
import util.exception.CarExistException;
import util.exception.CarNotExistException;
import util.exception.CategoryNotExistException;
import util.exception.DeleteCarException;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotExistException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
public class SalesManagementModule {
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;





    public SalesManagementModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        }

    public void menuSalesManagement() throws RentalRateNotExistException {
        Scanner sc = new Scanner(System.in);
        Integer number;
        
        while(true) {
         System.out.println("*** CaRMS Sales Management Module ***\n");
            System.out.println("1. Create New Rental Rate");
            System.out.println("2. View All Rental Rates");
            System.out.println("3. View Rental Rates Detail");
            System.out.println("4. Back");
            
            number = sc.nextInt();
            
            if(number<=3 && number >=1 ) {
                System.out.println();
                doRentalRate(number);
            }
            else if(number == 4) {
                break;
            }
            else{
                System.out.println("Invalid option! Please try again!\n");
            }
        }
    }
     
    public void doRentalRate(Integer number) {
        try {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        
        if(number == 1) {
                System.out.println("Enter Name: ");
                String name = sc.nextLine();
                System.out.println("Enter Rental Rate Type: ");
                String rentalRateType = sc.nextLine();
                System.out.println("Enter Car Category: ");
                String carCategory = sc.nextLine();
                System.out.println("Enter Rate per Day: ");
                BigDecimal ratePerDay = sc.nextBigDecimal();
                System.out.println("Enter Start Date Time(dd/MM/yyyy hh:mm a):");
                String dates = sc.nextLine().trim();
                Date startDate = date.parse(dates);
                System.out.println("Enter End Date Time(dd/MM/yyyy hh:mm a):");
                dates = sc.nextLine().trim();
                Date endDate = date.parse(dates);

                try{
                   Category category = categorySessionBeanRemote.retrieveCategoryByName(carCategory);
                   RentalRates newRentalRate = new RentalRates(name, rentalRateType, category, ratePerDay, startDate, endDate);
                   Set<ConstraintViolation<RentalRates>>constraintViolations = validator.validate(newRentalRate);
                   
                    if(constraintViolations.isEmpty()) {
                        try {
                            rentalRateSessionBeanRemote.createRentalRate(newRentalRate, category.getCategoryId());
                            System.out.println("Rental rate is created successfully!\n");
                        } 
                        catch(UnknownPersistenceException ex){
                            System.out.println("An unknown error has occurred while creating the new rental Rate!: " + ex.getMessage() + "\n");
                        }
                        catch(InputDataValidationException ex){
                            System.out.println(ex.getMessage() + "\n");
                        } 
                        catch(RentalRateExistException ex) {
                           System.out.println("An error has occurred while creating the new rental rate!: The rental rate already exist\n");
                       }
                    }
                } 
                catch(CategoryNotExistException ex) {
                    System.out.println(ex.getMessage());
                }
        } else if(number == 2) {
            List<RentalRates> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRate();
            for(RentalRates rentalRate: rentalRates) {
                System.out.println("Rental Rate ID: " + rentalRate.getRentalRateId() +"Rental Rate Name: " + rentalRate.getName() + "Category: " + rentalRate.getCategory().getCategoryName() + " Rate per Day: " + rentalRate.getRatePerDay() + "Period of Event: " + rentalRate.getStartDateTime() + "-" + rentalRate.getEndDateTime());
            }
            
        } else if(number == 3) {
           System.out.println("Enter Rental Rate ID: ");
           Long rentalRateId = sc.nextLong();
           sc.nextLine();
           try {
           RentalRates rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateById(rentalRateId);
           System.out.println("Rental Rate Name: " + rentalRate.getName() + "Category: " + rentalRate.getCategory().getCategoryName() + " Rate per Day: " + rentalRate.getRatePerDay() + "Period of Event: " + rentalRate.getStartDateTime() + "-" + rentalRate.getEndDateTime());
           while(true) {
                System.out.println("1. Update Rental Rate");
                System.out.println("2. Delete Rental Rate");
                System.out.println("3.Back");
                Integer num;
                num = sc.nextInt();
           
                if(num == 1) {
                    updateRentalRate(rentalRate);
                } else if(num == 2) {
                    deleteRentalRate(rentalRateId);
                } else if(num == 3) {
                    break;
                } else {
                    System.out.println("input invalid! please try again");
                }
           }
           } catch(RentalRateNotExistException ex) {
               System.out.println(ex.getMessage()+ "\n");
           }
        }
        }catch(ParseException ex) {
           System.out.println("Invalid date input!\n");
        }
    }
           
    public void updateRentalRate(RentalRates rentalRate) {
        try {
            Scanner sc = new Scanner(System.in);
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            System.out.println("Enter new  Rental Rate Name: (blank if no change)");
            String input = sc.nextLine().trim();

            if(input.length() > 0){
                rentalRate.setName(input);
            }

            System.out.println("Enter new Rental Rate Type: (blank if no change)");
            input = sc.nextLine().trim();

            if(input.length() > 0){
                rentalRate.setRentalRateType(input);
            }

            System.out.println("Enter new Car Category: (blank if no change)");
            input = sc.nextLine().trim();

            if(input.length() > 0){
                 try{
                    Category category = categorySessionBeanRemote.retrieveCategoryByName(input);
                    rentalRate.setCategory(category);
                 } catch(CategoryNotExistException ex) {
                     System.out.println(ex.getMessage()+ "\n");
                 }
            }

                System.out.println("Enter new Rate per Day: (blank if no change)");
                BigDecimal input1 = sc.nextBigDecimal();

                if(input1 != null){
                    rentalRate.setRatePerDay(input1);
                }

                System.out.println("Enter new start Date Time(dd/MM/yyyy hh:mm a): (blank if no change) ");
                input = sc.nextLine().trim();

                if(input.length() > 0){
                    try {
                    Date startDate = inputDateFormat.parse(input);
                    rentalRate.setStartDateTime(startDate);
                    } catch(ParseException ex) {
                        System.out.println("Invalid date input!\n");
                    }
                }

                System.out.println("Enter new end Date Time(dd/MM/yyyy hh:mm a): (blank if no change) ");
                input = sc.nextLine().trim();

                if(input.length() > 0){
                    Date endDate = inputDateFormat.parse(input);
                    rentalRate.setEndDateTime(endDate);

                }
                    try{
                        rentalRateSessionBeanRemote.updateRentalRate(rentalRate);
                        System.out.println("rentalRate is updated successfully!\n");
                    }
                    catch(InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }
                    catch(RentalRateNotExistException ex) {
                        System.out.println(ex.getMessage()+ "\n");
                    }
                    }
            catch(ParseException ex)
            {
                System.out.println("Invalid date input!\n");
            }
    }
     
    public void deleteRentalRate(long rentalRateId) {
        try{
            rentalRateSessionBeanRemote.deleteRentalRate(rentalRateId);
            System.out.println("Rental Rate is deleted successfully!\n");
        } 
        catch(DeleteRentalRateException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        catch(RentalRateNotExistException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
}
