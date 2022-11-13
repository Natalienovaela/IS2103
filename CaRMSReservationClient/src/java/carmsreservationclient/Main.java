/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Natalienovaela
 */
public class Main {

    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
        MainApp mainApp = new MainApp(customerSessionBeanRemote, reservationSessionBeanRemote, modelSessionBeanRemote, outletSessionBeanRemote);
        mainApp.run();
    }
    
}
