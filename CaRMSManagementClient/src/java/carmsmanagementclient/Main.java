/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.CarNotExistException;
import util.exception.EmployeeNotExistException;
import util.exception.ModelNotExistException;
import util.exception.RentalRateNotExistException;


/**
 *
 * @author Natalienovaela
 */
public class Main {

    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;

    @EJB
    private static CategorySessionBeanRemote categorySessionBeanRemote;

    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws EmployeeNotExistException, ModelNotExistException, CarNotExistException, RentalRateNotExistException {
        // TODO code application logic here
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, modelSessionBeanRemote, categorySessionBeanRemote, carSessionBeanRemote,rentalRateSessionBeanRemote, outletSessionBeanRemote);
        mainApp.run();
    }
    
}
