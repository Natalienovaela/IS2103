/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.Category;
import entity.Model;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CategoryNotExistException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotAvailableException;
import util.exception.ModelNotExistException;
import util.exception.ReservationExistException;
import util.exception.ReservationNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Natalienovaela
 */
@Remote
public interface ReservationSessionBeanRemote {

    public List<Reservation> retrieveCurrentDateReservation(Date date);

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotExistException;

    public List<Reservation> retrieveMyReservations(long customerId);

    public void createReservation(Reservation reservation) throws ReservationExistException, UnknownPersistenceException, InputDataValidationException;
    
    public BigDecimal totalAmount(Reservation reservation);
    
}
