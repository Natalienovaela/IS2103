/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatch;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeNotExistException;
import util.exception.TransitDriverDispatchNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Remote
public interface TransitDriverDispatchSessionBeanRemote {
    
    public void createDispatch(TransitDriverDispatch dispatch, Long outletId, Long carId);

    public List<TransitDriverDispatch> retrieveAllDispatch(Long outletId);

    public void assignTransitDriver(Long transitId, Long employeeId) throws EmployeeNotExistException, TransitDriverDispatchNotExistException;

    public void updateTransitAsCompleted(Long transitId) throws TransitDriverDispatchNotExistException;
}
