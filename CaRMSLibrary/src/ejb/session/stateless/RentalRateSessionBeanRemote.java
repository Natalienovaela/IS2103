/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRates;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateExistException;
import util.exception.RentalRatesNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
@Remote
public interface RentalRateSessionBeanRemote {

    public void createRentalRate(RentalRates rentalRate, Long categoryId) throws RentalRateExistException, UnknownPersistenceException, InputDataValidationException;

    public List<RentalRates> retrieveAllRentalRate();

    public RentalRates retrieveRentalRateById(long rentalRateId) throws RentalRatesNotExistException;

    public void updateRentalRate(RentalRates rentalRate) throws RentalRatesNotExistException, InputDataValidationException;

    public void deleteRentalRate(long rentalRateId) throws RentalRatesNotExistException, DeleteRentalRateException;
    
}
