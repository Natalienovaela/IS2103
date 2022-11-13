/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import entity.Model;

/**
 *
 * @author PERSONAL
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickUpDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;
    @Column(nullable = false)
    private Boolean pickedUp;
    @Column(nullable = false)
    private Boolean returned;
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal totalAmount;
    @Column(nullable = false)
    private Boolean paid;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Outlet pickupOutlet;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Outlet returnOutlet;
    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;
    @ManyToOne(fetch = FetchType.LAZY)
    private Model model;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Car car;
            
    public Reservation() {
        this.totalAmount = new BigDecimal("0.00");
    }

    public Reservation(Date pickUpDate, Date returnDate, Outlet pickupOutlet, Outlet returnOutlet,  Category category, Model model) {
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
        this.category = category;
        this.pickupOutlet = pickupOutlet;
        this.returnOutlet = returnOutlet;
        this.model = model;
    }
    

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }

    /**
     * @return the pickUpDate
     */
    public Date getPickUpDate() {
        return pickUpDate;
    }

    /**
     * @param pickUpDate the pickUpDate to set
     */
    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    /**
     * @return the returnDate
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the returnDate to set
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @return the pickedUp
     */
    public Boolean getPickedUp() {
        return pickedUp;
    }

    /**
     * @param pickedUp the pickedUp to set
     */
    public void setPickedUp(Boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    /**
     * @return the returned
     */
    public Boolean getReturned() {
        return returned;
    }

    /**
     * @param returned the returned to set
     */
    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
    
    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Outlet getPickupOutlet() {
        return pickupOutlet;
    }

    public void setPickupOutlet(Outlet pickupOutlet) {
        this.pickupOutlet = pickupOutlet;
    }

    public Outlet getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(Outlet returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
    
}
