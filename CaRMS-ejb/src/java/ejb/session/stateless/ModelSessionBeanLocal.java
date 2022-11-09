/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import javax.ejb.Local;
import util.exception.ModelNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Local
public interface ModelSessionBeanLocal {

    public Model retrieveModelbyModelandMake(String model, String make) throws ModelNotExistException;
    
}
