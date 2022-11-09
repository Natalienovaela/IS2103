/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarExistException;
import util.exception.CarNotExistException;
import util.exception.DeleteCarException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
@Remote
public interface CarSessionBeanRemote {

    public void createCar(Car car, Long modelId) throws CarExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Car> retrieveAllCar();

    public Car retrieveCarById(long carId) throws CarNotExistException;

    public void updateCar(Car car) throws CarNotExistException, InputDataValidationException;

    public void deleteCar(long carId) throws CarNotExistException, DeleteCarException;
    
}
