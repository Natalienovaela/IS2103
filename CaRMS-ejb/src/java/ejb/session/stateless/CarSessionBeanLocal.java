/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotExistException;
import util.exception.CategoryNotExistException;

/**
 *
 * @author PERSONAL
 */
@Local
public interface CarSessionBeanLocal {
    public Car retrieveCarById(Long carId) throws CarNotExistException;

    public List<Car> retrieveAllSearchCarByCategory(List<Model> models, String categoryName) throws CategoryNotExistException;
    
}
