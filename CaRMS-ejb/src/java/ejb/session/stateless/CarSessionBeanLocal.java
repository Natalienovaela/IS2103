/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotExistException;

/**
 *
 * @author PERSONAL
 */
@Local
public interface CarSessionBeanLocal {
    public Car retrieveCarById(Long carId) throws CarNotExistException;

    public List<Car> retrieveAllCarByOutletandDate(String outlet, Date pickupDate);
    
}
