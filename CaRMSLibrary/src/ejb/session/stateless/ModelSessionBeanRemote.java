/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.MakeOrModelExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Natalienovaela
 */
@Remote
public interface ModelSessionBeanRemote {

    public void createModel(Model model, Long categoryId) throws MakeOrModelExistException, UnknownPersistenceException, InputDataValidationException;
    
}
