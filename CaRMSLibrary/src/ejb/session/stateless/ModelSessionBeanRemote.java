/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import entity.Outlet;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.MakeOrModelExistException;
import util.exception.ModelNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Natalienovaela
 */
@Remote
public interface ModelSessionBeanRemote {

    public void createModel(Model model, Long categoryId) throws MakeOrModelExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Model> retrieveAllModel();

    public Model retrieveModelbyId(Long modelId) throws ModelNotExistException;

    public void updateModel(Model model) throws InputDataValidationException, ModelNotExistException;

    public void deleteModel(Long modelId) throws ModelNotExistException, DeleteModelException;

    public Model retrieveModelbyMakeandModel(String make, String model) throws ModelNotExistException;

    public List<Model> searchCar(Date pickupDateTime, Date returnDateTime, Outlet pickupOutlet, Outlet returnOutlet);
    
}
