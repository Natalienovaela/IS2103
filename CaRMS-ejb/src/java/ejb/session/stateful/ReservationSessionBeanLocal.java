/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotExistException;
import util.exception.ModelNotExistException;
import util.exception.RentalRateNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Local
public interface ReservationSessionBeanLocal {

    public List<Reservation> retrieveReservationByModelId(Long modelId) throws ModelNotExistException;
    public List<Reservation> retrieveCurrentDateReservation(Date date);
    public List<Reservation> retrieveReservationByCarId(Long CarId) throws CarNotExistException;
    public List<Reservation> retrieveReservationByRentalRateId(Long rentalRateId) throws RentalRateNotExistException;
    public List<Reservation> retrieveMyReservations(long customerId); 
}
