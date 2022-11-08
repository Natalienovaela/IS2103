/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CategoryNotExistException;

/**
 *
 * @author Natalienovaela
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Category retrieveCategoryByName(String name) throws CategoryNotExistException {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :name");
        query.setParameter("name", name);
        
        try{
            Category category = (Category)query.getSingleResult();
            category.getModels().size();
            return category;
        }
        catch (Exception ex) {
            throw new CategoryNotExistException("Category with the name " + name + " does not exist\n");
        }
    }
    
}
