/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Natalienovaela
 */
@Entity
public class TransitDriverDispatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitId;
    private Date transitDate;
    @Column(nullable = false)
    private Boolean completed;
    
    @OneToOne
    private Employee transitDriver;
    
    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Outlet outlet;
    
    @JoinColumn(nullable = false)
    @OneToOne(optional = false)
    private Car car;

    public TransitDriverDispatch(Date transitDate, Outlet outlet, Car car) {
        this.completed = false;
        this.transitDate = transitDate;
        this.outlet = outlet;
        this.car = car;
    }

    public TransitDriverDispatch() {
    }

    public Long getTransitId() {
        return transitId;
    }

    public void setTransitId(Long transitId) {
        this.transitId = transitId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitId != null ? transitId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitId fields are not set
        if (!(object instanceof TransitDriverDispatch)) {
            return false;
        }
        TransitDriverDispatch other = (TransitDriverDispatch) object;
        if ((this.transitId == null && other.transitId != null) || (this.transitId != null && !this.transitId.equals(other.transitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatch[ id=" + transitId + " ]";
    }

    public Date getTransitDate() {
        return transitDate;
    }

    public void setTransitDate(Date transitDate) {
        this.transitDate = transitDate;
    }

    public Employee getTransitDriver() {
        return transitDriver;
    }

    public void setTransitDriver(Employee transitDriver) {
        this.transitDriver = transitDriver;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
}
