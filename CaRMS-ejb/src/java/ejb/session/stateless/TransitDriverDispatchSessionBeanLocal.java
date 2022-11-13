/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatch;
import javax.ejb.Local;

/**
 *
 * @author Natalienovaela
 */
@Local
public interface TransitDriverDispatchSessionBeanLocal {

    public void createDispatch(TransitDriverDispatch dispatch, Long outletId, Long carId);
    
}
