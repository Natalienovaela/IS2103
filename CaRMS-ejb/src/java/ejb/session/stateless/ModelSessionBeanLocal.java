/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import entity.Outlet;
import java.util.List;
import javax.ejb.Local;
import util.exception.ModelNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Local
public interface ModelSessionBeanLocal {
    public List<Model> retrieveAllModel();
    
}
