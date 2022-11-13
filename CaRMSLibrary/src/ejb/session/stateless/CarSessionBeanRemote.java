/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarExistException;
import util.exception.CarNotExistException;
import util.exception.CategoryNotExistException;
import util.exception.DeleteCarException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author PERSONAL
 */
@Remote
public interface CarSessionBeanRemote {

    public void createCar(Car car, Long modelId) throws CarExistException, UnknownPersistenceException, InputDataValidationException;

    public void deleteCar(Long carId) throws CarNotExistException, DeleteCarException;

    public List<Car> retrieveAllCar();
    
    public Car retrieveCarById(Long carId) throws CarNotExistException;
    
    public List<Car> retrieveAllSearchCarByCategory(List<Model> models, String categoryName) throws CategoryNotExistException;

    public void updateCar(Car car) throws InputDataValidationException, CarNotExistException;

    public void pickUpCar(Long carId, Long reservationId);

    public void returnCar(Long carId, Long reservationId);

    public List<Car> retrieveAllSearchCarByModelandMake(List<Model> models, String model, String make) throws ModelNotExistException;
    
}
