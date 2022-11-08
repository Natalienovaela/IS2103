/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import javax.ejb.Remote;
import util.exception.CategoryNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Remote
public interface CategorySessionBeanRemote {

    public Category retrieveCategoryByName(String name) throws CategoryNotExistException;
    
}
