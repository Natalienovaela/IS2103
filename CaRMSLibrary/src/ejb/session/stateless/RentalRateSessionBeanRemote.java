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
import util.exception.RentalRateNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
@Remote
public interface RentalRateSessionBeanRemote {

    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotExistException, DeleteRentalRateException;

    public void updateRentalRate(RentalRates rentalRate) throws InputDataValidationException, RentalRateNotExistException;

    public RentalRates retrieveRentalRateById(Long rentalRateId) throws RentalRateNotExistException;

    public List<RentalRates> retrieveAllRentalRate();

    public void createRentalRate(RentalRates rentalRate, Long categoryId) throws RentalRateExistException, UnknownPersistenceException, InputDataValidationException;

    public RentalRates retrieveRentalRateByName(String rentalRateName) throws RentalRateNotExistException;
    
}
