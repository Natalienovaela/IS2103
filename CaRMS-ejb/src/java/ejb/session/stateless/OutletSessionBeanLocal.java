/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Local;

/**
 *
 * @author Natalienovaela
 */
@Local
public interface OutletSessionBeanLocal {

    public Outlet retrieveOutletByName(String name);
    
}
