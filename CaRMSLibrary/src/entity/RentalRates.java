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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author PERSONAL
 */
@Entity
public class RentalRates implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false, length = 10)
    private String name;
    @Column(nullable = false, length = 15)
    private String rentalRateType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date StartDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date EndDateTime;
    @Column(nullable = false, length = 8)
    private BigDecimal ratePerDay;
    @Column
    private Boolean disabled;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;
    @ManyToMany
    @JoinColumn(nullable = false)
    private List<Reservation> reservations;

    public RentalRates() {
        reservations = new ArrayList<>();
    }

    public RentalRates(String name, String rentalRateType,  Category category,  BigDecimal ratePerDay, Date StartDateTime, Date EndDateTime) {
        this.name = name;
        this.rentalRateType = rentalRateType;
        this.StartDateTime = StartDateTime;
        this.EndDateTime = EndDateTime;
        this.ratePerDay = ratePerDay;
        this.category = category;
    }

    
    public RentalRates(String name, String rentalRateType, Date StartDateTime, Date EndDateTime, BigDecimal ratePerDay, Boolean disabled) {
        this.name = name;
        this.rentalRateType = rentalRateType;
        this.StartDateTime = StartDateTime;
        this.EndDateTime = EndDateTime;
        this.ratePerDay = ratePerDay;
        this.disabled = disabled;
    }

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRates)) {
            return false;
        }
        RentalRates other = (RentalRates) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRates[ id=" + rentalRateId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the ratePerDay
     */
    public BigDecimal getRatePerDay() {
        return ratePerDay;
    }

    /**
     * @param ratePerDay the ratePerDay to set
     */
    public void setRatePerDay(BigDecimal ratePerDay) {
        this.ratePerDay = ratePerDay;
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
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
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
     * @return the rentalRateType
     */
    public String getRentalRateType() {
        return rentalRateType;
    }

    /**
     * @param rentalRateType the rentalRateType to set
     */
    public void setRentalRateType(String rentalRateType) {
        this.rentalRateType = rentalRateType;
    }

    /**
     * @return the StartDateTime
     */
    public Date getStartDateTime() {
        return StartDateTime;
    }

    /**
     * @param StartDateTime the StartDateTime to set
     */
    public void setStartDateTime(Date StartDateTime) {
        this.StartDateTime = StartDateTime;
    }

    /**
     * @return the EndDateTime
     */
    public Date getEndDateTime() {
        return EndDateTime;
    }

    /**
     * @param EndDateTime the EndDateTime to set
     */
    public void setEndDateTime(Date EndDateTime) {
        this.EndDateTime = EndDateTime;
    }
    
}
