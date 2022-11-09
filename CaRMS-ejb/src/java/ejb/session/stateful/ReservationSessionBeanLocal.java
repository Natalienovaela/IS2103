/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotExistException;
import util.exception.ModelNotExistException;
import util.exception.RentalRatesNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Local
public interface ReservationSessionBeanLocal {

    public List<Reservation> retrieveReservationByModelId(Long modelId) throws ModelNotExistException;

    public List<Reservation> retrieveReservationByRentalRateId(Long rentalRateId) throws RentalRatesNotExistException;

    public List<Reservation> retrieveReservationByCarId(Long carId) throws CarNotExistException;
    
}
