/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRates;
import javax.ejb.Local;
import util.exception.RentalRateNotExistException;

/**
 *
 * @author PERSONAL
 */
@Local
public interface RentalRateSessionBeanLocal {
    
    public RentalRates retrieveRentalRateById(Long rentalRateId) throws RentalRateNotExistException;
    
}
