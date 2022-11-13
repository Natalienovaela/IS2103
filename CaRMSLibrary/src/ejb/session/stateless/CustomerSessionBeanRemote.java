/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Remote;
import util.exception.CustomerExistException;
import util.exception.CustomerNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
@Remote
public interface CustomerSessionBeanRemote {

    public void registerCustomer(Customer customer) throws CustomerExistException, UnknownPersistenceException, InputDataValidationException;

    public Customer retrieveCustomerByEmail(String email) throws CustomerNotExistException;

    public void setCreditCard(Long customerId, String cVV, String nameOnCard, String cardNumber);
    
}
