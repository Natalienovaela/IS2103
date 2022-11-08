/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.EmployeeNotExistException;


/**
 *
 * @author Natalienovaela
 */
public class Main {

    @EJB
    private static CategorySessionBeanRemote categorySessionBeanRemote;

    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws EmployeeNotExistException {
        // TODO code application logic here
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, modelSessionBeanRemote, categorySessionBeanRemote);
        mainApp.run();
    }
    
}
