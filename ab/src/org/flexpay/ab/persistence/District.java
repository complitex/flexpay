package org.flexpay.ab.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * District generated by hbm2java
 */
@Entity
@Table(name="district"
    ,catalog="flexpay_db"
)
public class District  implements java.io.Serializable {


     private int id;
     private Town town;
     private int districtStatus;
     private Set<Street> streets = new HashSet<Street>(0);
     private Set<DistrictName> districtNames = new HashSet<DistrictName>(0);
     private Set<Building> buildings = new HashSet<Building>(0);

    public District() {
    }

	
    public District(int id, Town town, int districtStatus) {
        this.id = id;
        this.town = town;
        this.districtStatus = districtStatus;
    }
    public District(int id, Town town, int districtStatus, Set<Street> streets, Set<DistrictName> districtNames, Set<Building> buildings) {
       this.id = id;
       this.town = town;
       this.districtStatus = districtStatus;
       this.streets = streets;
       this.districtNames = districtNames;
       this.buildings = buildings;
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
    @JoinColumn(name="Town_ID", nullable=false)
    public Town getTown() {
        return this.town;
    }
    
    public void setTown(Town town) {
        this.town = town;
    }
    
    @Column(name="District_Status", nullable=false)
    public int getDistrictStatus() {
        return this.districtStatus;
    }
    
    public void setDistrictStatus(int districtStatus) {
        this.districtStatus = districtStatus;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="district")
    public Set<Street> getStreets() {
        return this.streets;
    }
    
    public void setStreets(Set<Street> streets) {
        this.streets = streets;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="district")
    public Set<DistrictName> getDistrictNames() {
        return this.districtNames;
    }
    
    public void setDistrictNames(Set<DistrictName> districtNames) {
        this.districtNames = districtNames;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="district")
    public Set<Building> getBuildings() {
        return this.buildings;
    }
    
    public void setBuildings(Set<Building> buildings) {
        this.buildings = buildings;
    }




}


