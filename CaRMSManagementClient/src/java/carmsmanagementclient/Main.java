/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;

/**
 *
 * @author Natalienovaela
 */
public class Main {
    EmployeeSessionBeanRemote employeeSessionBeanRemote;
    /**
     * @param args the command line arguments
     */
    public void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(employeeSessionBeanRemote);
        mainApp.run();
    }
    
}
