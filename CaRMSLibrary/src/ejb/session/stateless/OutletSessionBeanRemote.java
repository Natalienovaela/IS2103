/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.Date;
import javax.ejb.Remote;
import util.exception.OutletNotExistException;
import util.exception.OutsideOutletAvailability;

/**
 *
 * @author Natalienovaela
 */
@Remote
public interface OutletSessionBeanRemote {
    public Outlet retrieveOutletByName(String name) throws OutletNotExistException;

    public void checkOutletAvailability(Date date, Long outletId) throws OutsideOutletAvailability;
    
}
