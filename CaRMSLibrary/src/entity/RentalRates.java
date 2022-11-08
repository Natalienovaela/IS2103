/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    @Column(nullable = false, length = 10)
    private String validityPeriod;
    @Column(nullable = false, length = 8)
    private BigDecimal ratePerDay;

    public RentalRates() {
    }

    public RentalRates(String name, String validityPeriod, BigDecimal ratePerDay) {
        this.name = name;
        this.validityPeriod = validityPeriod;
        this.ratePerDay = ratePerDay;
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
     * @return the validityPeriod
     */
    public String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
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
    
}
