/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import entity.Outlet;
import java.util.Date;
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
    public Model retrieveModelbyMakeandModel(String make, String model) throws ModelNotExistException;
    public List<Model> searchCar(Date pickupDateTime, Date returnDateTime, Outlet pickupOutlet, Outlet returnOutlet);

    
}
