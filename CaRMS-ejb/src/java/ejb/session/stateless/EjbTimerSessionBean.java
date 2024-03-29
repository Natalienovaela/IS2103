/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateful.ReservationSessionBeanLocal;
import entity.Car;
import entity.Category;
import entity.Model;
import entity.Reservation;
import entity.TransitDriverDispatch;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Natalienovaela
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private TransitDriverDispatchSessionBeanLocal transitDriverDispatchSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;
    
    
    @Schedule(hour = "2", minute = "0", second = "0")
    @Override
    public void allocateCars() {
        List<Reservation> reservations = reservationSessionBean.retrieveCurrentDateReservation(new Date());
        for(Reservation reservation: reservations) {
            Category category = reservation.getCategory();
            
            Boolean reserved = false;
            
            if(reservation.getModel() != null) {
                Query query = em.createQuery("SELECT c FROM Car c WHERE c.reservations IS EMPTY AND c.currOutlet = :outlet AND c.disabled = :false");
                query.setParameter("outlet", reservation.getPickupOutlet());
                query.setParameter("false", Boolean.FALSE);
                List<Car> carsWithNoReservation = query.getResultList();
                    
                for(Car car: carsWithNoReservation) {
                        reservation.setCar(car);
                        car.getReservations().add(reservation);
                        reserved = Boolean.TRUE;
                        break;
                } 
                
                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND r.returnOutlet = :outlet AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithReservation = query.getResultList();

                    for(Car car: carsWithReservation) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(car.getReservations().get(0).getReturnDate());

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(reservation.getPickUpDate());

                        if(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) <= cal2.get(Calendar.HOUR_OF_DAY) * 60 + cal2.get(Calendar.MINUTE)){
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            reserved = Boolean.TRUE;
                            break;
                        }
                    }   
                }
                
                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c WHERE c.reservations IS EMPTY AND c.currOutlet != :outlet AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithNoReservationNotTheSameOutlet = query.getResultList();
                    
                    for(Car car: carsWithNoReservationNotTheSameOutlet) {
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            reserved = Boolean.TRUE;
                            TransitDriverDispatch dispatch = new TransitDriverDispatch(new Date(), reservation.getPickupOutlet(), car);
                            transitDriverDispatchSessionBean.createDispatch(dispatch, reservation.getPickupOutlet().getOutletId(), car.getCarId());
                            break;
                    }

                }

                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND r.returnOutlet != :outlet AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithReservationNotTheSameOutlet = query.getResultList();

                    for(Car car: carsWithReservationNotTheSameOutlet) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(car.getReservations().get(0).getReturnDate());

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(reservation.getPickUpDate());

                        if(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) + 120 <= cal2.get(Calendar.HOUR_OF_DAY) * 60 + cal2.get(Calendar.MINUTE)){
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            TransitDriverDispatch dispatch = new TransitDriverDispatch(new Date(), reservation.getPickupOutlet(), car);
                            transitDriverDispatchSessionBean.createDispatch(dispatch, reservation.getPickupOutlet().getOutletId(), car.getCarId());
                            break;
                        }
                    }
                }    
            }
            else {
                Query query = em.createQuery("SELECT c FROM Car c WHERE c.reservations IS EMPTY AND c.currOutlet = :outlet AND c.model = :model AND c.disabled = :false");
                query.setParameter("outlet", reservation.getPickupOutlet());
                query.setParameter("model", reservation.getModel());
                query.setParameter("false", Boolean.FALSE);
                List<Car> carsWithNoReservation = query.getResultList();
                    
                for(Car car: carsWithNoReservation) {
                        reservation.setCar(car);
                        car.getReservations().add(reservation);
                        reserved = Boolean.TRUE;
                        break;
                } 
                
                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND r.returnOutlet = :outlet AND c.model = :model AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("model", reservation.getModel());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithReservation = query.getResultList();

                    for(Car car: carsWithReservation) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(car.getReservations().get(0).getReturnDate());

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(reservation.getPickUpDate());

                        if(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) <= cal2.get(Calendar.HOUR_OF_DAY) * 60 + cal2.get(Calendar.MINUTE)){
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            reserved = Boolean.TRUE;
                            break;
                        }
                    }   
                }
                
                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c WHERE c.reservations IS EMPTY AND c.currOutlet != :outlet AND c.model :model AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("model", reservation.getModel());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithNoReservationNotTheSameOutlet = query.getResultList();
                    
                    for(Car car: carsWithNoReservationNotTheSameOutlet) {
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            reserved = Boolean.TRUE;
                            TransitDriverDispatch dispatch = new TransitDriverDispatch(new Date(), reservation.getPickupOutlet(), car);
                            transitDriverDispatchSessionBean.createDispatch(dispatch, reservation.getPickupOutlet().getOutletId(), car.getCarId());
                            break;
                    }

                }

                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND r.returnOutlet != :outlet AND c.model = :model AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("model", reservation.getModel());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithReservationNotTheSameOutlet = query.getResultList();

                    for(Car car: carsWithReservationNotTheSameOutlet) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(car.getReservations().get(0).getReturnDate());

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(reservation.getPickUpDate());

                        if(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) + 120 <= cal2.get(Calendar.HOUR_OF_DAY
                        ) * 60 + cal2.get(Calendar.MINUTE)){
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            TransitDriverDispatch dispatch = new TransitDriverDispatch(new Date(), reservation.getPickupOutlet(), car);
                            transitDriverDispatchSessionBean.createDispatch(dispatch, reservation.getPickupOutlet().getOutletId(), car.getCarId());
                            break;
                        }
                    }
                }    
                
            }
        }
    }
    
    @Override
    public void allocateCars(Date date) {
        List<Reservation> reservations = reservationSessionBean.retrieveCurrentDateReservation(date);
        for(Reservation reservation: reservations) {
            Category category = reservation.getCategory();
            
            Boolean reserved = false;
            
            if(reservation.getModel() != null) {
                Query query = em.createQuery("SELECT c FROM Car c WHERE c.reservations IS EMPTY AND c.currOutlet = :outlet AND c.disabled = :false");
                query.setParameter("outlet", reservation.getPickupOutlet());
                query.setParameter("false", Boolean.FALSE);
                List<Car> carsWithNoReservation = query.getResultList();
                    
                for(Car car: carsWithNoReservation) {
                        reservation.setCar(car);
                        car.getReservations().add(reservation);
                        reserved = Boolean.TRUE;
                        break;
                } 
                
                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND r.returnOutlet = :outlet AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithReservation = query.getResultList();

                    for(Car car: carsWithReservation) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(car.getReservations().get(0).getReturnDate());

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(reservation.getPickUpDate());

                        if(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) <= cal2.get(Calendar.HOUR_OF_DAY) * 60 + cal2.get(Calendar.MINUTE)){
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            reserved = Boolean.TRUE;
                            break;
                        }
                    }   
                }
                
                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c WHERE c.reservations IS EMPTY AND c.currOutlet != :outlet AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithNoReservationNotTheSameOutlet = query.getResultList();
                    
                    for(Car car: carsWithNoReservationNotTheSameOutlet) {
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            reserved = Boolean.TRUE;
                            TransitDriverDispatch dispatch = new TransitDriverDispatch(new Date(), reservation.getPickupOutlet(), car);
                            transitDriverDispatchSessionBean.createDispatch(dispatch, reservation.getPickupOutlet().getOutletId(), car.getCarId());
                            break;
                    }

                }

                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND r.returnOutlet != :outlet AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithReservationNotTheSameOutlet = query.getResultList();

                    for(Car car: carsWithReservationNotTheSameOutlet) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(car.getReservations().get(0).getReturnDate());

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(reservation.getPickUpDate());

                        if(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) + 120 <= cal2.get(Calendar.HOUR_OF_DAY) * 60 + cal2.get(Calendar.MINUTE)){
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            TransitDriverDispatch dispatch = new TransitDriverDispatch(new Date(), reservation.getPickupOutlet(), car);
                            transitDriverDispatchSessionBean.createDispatch(dispatch, reservation.getPickupOutlet().getOutletId(), car.getCarId());
                            break;
                        }
                    }
                }    
            }
            else {
                Query query = em.createQuery("SELECT c FROM Car c WHERE c.reservations IS EMPTY AND c.currOutlet = :outlet AND c.model = :model AND c.disabled = :false");
                query.setParameter("outlet", reservation.getPickupOutlet());
                query.setParameter("model", reservation.getModel());
                query.setParameter("false", Boolean.FALSE);
                List<Car> carsWithNoReservation = query.getResultList();
                    
                for(Car car: carsWithNoReservation) {
                        reservation.setCar(car);
                        car.getReservations().add(reservation);
                        reserved = Boolean.TRUE;
                        break;
                } 
                
                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND r.returnOutlet = :outlet AND c.model = :model AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("model", reservation.getModel());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithReservation = query.getResultList();

                    for(Car car: carsWithReservation) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(car.getReservations().get(0).getReturnDate());

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(reservation.getPickUpDate());

                        if(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) <= cal2.get(Calendar.HOUR_OF_DAY) * 60 + cal2.get(Calendar.MINUTE)){
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            reserved = Boolean.TRUE;
                            break;
                        }
                    }   
                }
                
                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c WHERE c.reservations IS EMPTY AND c.currOutlet != :outlet AND c.model :model AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("model", reservation.getModel());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithNoReservationNotTheSameOutlet = query.getResultList();
                    
                    for(Car car: carsWithNoReservationNotTheSameOutlet) {
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            reserved = Boolean.TRUE;
                            TransitDriverDispatch dispatch = new TransitDriverDispatch(new Date(), reservation.getPickupOutlet(), car);
                            transitDriverDispatchSessionBean.createDispatch(dispatch, reservation.getPickupOutlet().getOutletId(), car.getCarId());
                            break;
                    }

                }

                if(!reserved) {
                    query = em.createQuery("SELECT c FROM Car c JOIN c.reservations r WHERE c.reservations IS NOT EMPTY AND r.returnOutlet != :outlet AND c.model = :model AND c.disabled = :false");
                    query.setParameter("outlet", reservation.getPickupOutlet());
                    query.setParameter("model", reservation.getModel());
                    query.setParameter("false", Boolean.FALSE);
                    List<Car> carsWithReservationNotTheSameOutlet = query.getResultList();

                    for(Car car: carsWithReservationNotTheSameOutlet) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(car.getReservations().get(0).getReturnDate());

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(reservation.getPickUpDate());

                        if(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) + 120 <= cal2.get(Calendar.HOUR_OF_DAY) * 60 + cal2.get(Calendar.MINUTE)){
                            reservation.setCar(car);
                            car.getReservations().add(reservation);
                            TransitDriverDispatch dispatch = new TransitDriverDispatch(new Date(), reservation.getPickupOutlet(), car);
                            transitDriverDispatchSessionBean.createDispatch(dispatch, reservation.getPickupOutlet().getOutletId(), car.getCarId());
                            break;
                        }
                    }
                }    
                
            }
        }
    }
}
