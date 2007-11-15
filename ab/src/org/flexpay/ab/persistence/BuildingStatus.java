package org.flexpay.ab.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * BuildingStatus generated by hbm2java
 */
@Entity
@Table(name="building_status"
    ,catalog="flexpay_db"
)
public class BuildingStatus  implements java.io.Serializable {


     private int id;
     private Building building;
     private Date begin;
     private Date end;
     private int value;

    public BuildingStatus() {
    }

    public BuildingStatus(int id, Building building, Date begin, Date end, int value) {
       this.id = id;
       this.building = building;
       this.begin = begin;
       this.end = end;
       this.value = value;
    }
   
     @Id 
    
    @Column(name="ID", nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="Building_ID", nullable=false)
    public Building getBuilding() {
        return this.building;
    }
    
    public void setBuilding(Building building) {
        this.building = building;
    }
    
    @Column(name="begin", nullable=false, length=0)
    public Date getBegin() {
        return this.begin;
    }
    
    public void setBegin(Date begin) {
        this.begin = begin;
    }
    
    @Column(name="end", nullable=false, length=0)
    public Date getEnd() {
        return this.end;
    }
    
    public void setEnd(Date end) {
        this.end = end;
    }
    
    @Column(name="value", nullable=false)
    public int getValue() {
        return this.value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }




}


