/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import javax.ejb.Local;
import util.exception.CategoryNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Local
public interface CategorySessionBeanLocal {
    public Category retrieveCategoryByName(String name) throws CategoryNotExistException;
    
}
