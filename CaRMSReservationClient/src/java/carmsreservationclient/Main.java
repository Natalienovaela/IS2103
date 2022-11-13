/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.ModelNotInTheSearchListException;
import util.exception.ReservationExistException;
import util.exception.ReservationNotExistException;

/**
 *
 * @author Natalienovaela
 */
public class Main {

    @EJB
    private static CategorySessionBeanRemote categorySessionBeanRemote;

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
    public static void main(String[] args) throws ModelNotInTheSearchListException, ReservationExistException, ReservationNotExistException{
        // TODO code application logic here
        MainApp mainApp = new MainApp(categorySessionBeanRemote, customerSessionBeanRemote, reservationSessionBeanRemote, modelSessionBeanRemote, outletSessionBeanRemote);
        mainApp.run();
    }
    
}
