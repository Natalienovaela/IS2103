/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.CarStatus;
import util.enumeration.CarAvailabilityStatus;

/**
 *
 * @author Natalienovaela
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(length = 8, unique = true)
    private String licensePlateNumber;
    @Column(nullable = false)
    private Boolean disabled;
    @Enumerated(EnumType.STRING)
    private CarStatus status;
    @Enumerated(EnumType.STRING)
    private CarAvailabilityStatus availStatus;
    
    @OneToOne(mappedBy = "car")
    private TransitDriverDispatch transit;
    
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Model model;
    
    @ManyToOne
    private Outlet currOutlet;
    
    @OneToMany
    private List<Reservation> reservations;

    public Car() {
        reservations = new ArrayList();
    }
    
    public Car(String licensePlateNumber, Model model, Outlet currOutlet) {
        this.licensePlateNumber = licensePlateNumber;
        this.model = model;
        this.currOutlet = currOutlet;
        this.disabled = false;
    }
    
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + carId + " ]";
    }

    /**
     * @return the licensePlateNumber
     */
    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    /**
     * @param licensePlateNumber the licensePlateNumber to set
     */
    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    /**
     * @return the disabled
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the status
     */
    public CarStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(CarStatus status) {
        this.status = status;
    }

    /**
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }

    public void setStatus(CarAvailabilityStatus carAvailabilityStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the availStatus
     */
    public CarAvailabilityStatus getAvailStatus() {
        return availStatus;
    }

    /**
     * @param availStatus the availStatus to set
     */
    public void setAvailStatus(CarAvailabilityStatus availStatus) {
        this.availStatus = availStatus;
    }

    /**
     * @return the transit
     */
    public TransitDriverDispatch getTransit() {
        return transit;
    }

    /**
     * @param transit the transit to set
     */
    public void setTransit(TransitDriverDispatch transit) {
        this.transit = transit;
    }

    /**
     * @return the currOutlet
     */
    public Outlet getCurrOutlet() {
        return currOutlet;
    }

    /**
     * @param currOutlet the currOutlet to set
     */
    public void setCurrOutlet(Outlet currOutlet) {
        this.currOutlet = currOutlet;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
}
