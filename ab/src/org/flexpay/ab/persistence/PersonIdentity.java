package org.flexpay.ab.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import java.util.Date;
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
 * PersonIdentity generated by hbm2java
 */
@Entity
@Table(name="person_identity"
    ,catalog="flexpay_db"
)
public class PersonIdentity  implements java.io.Serializable {


     private int id;
     private IdentityType identityType;
     private Date beginDate;
     private Date endDate;
     private String organization;
     private String firstName;
     private String lastName;
     private String middleName;
     private int serialNumber;
     private int documentNumber;
     private Set<PersonIdentityAttribute> personIdentityAttributes = new HashSet<PersonIdentityAttribute>(0);
     private Set<Identities> identitieses = new HashSet<Identities>(0);

    public PersonIdentity() {
    }

	
    public PersonIdentity(int id, IdentityType identityType, Date beginDate, Date endDate, String organization, String firstName, String lastName, int serialNumber, int documentNumber) {
        this.id = id;
        this.identityType = identityType;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.organization = organization;
        this.firstName = firstName;
        this.lastName = lastName;
        this.serialNumber = serialNumber;
        this.documentNumber = documentNumber;
    }
    public PersonIdentity(int id, IdentityType identityType, Date beginDate, Date endDate, String organization, String firstName, String lastName, String middleName, int serialNumber, int documentNumber, Set<PersonIdentityAttribute> personIdentityAttributes, Set<Identities> identitieses) {
       this.id = id;
       this.identityType = identityType;
       this.beginDate = beginDate;
       this.endDate = endDate;
       this.organization = organization;
       this.firstName = firstName;
       this.lastName = lastName;
       this.middleName = middleName;
       this.serialNumber = serialNumber;
       this.documentNumber = documentNumber;
       this.personIdentityAttributes = personIdentityAttributes;
       this.identitieses = identitieses;
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
    @JoinColumn(name="identity_type_id", nullable=false)
    public IdentityType getIdentityType() {
        return this.identityType;
    }
    
    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }
    
    @Column(name="begin_date", nullable=false, length=0)
    public Date getBeginDate() {
        return this.beginDate;
    }
    
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    
    @Column(name="end_date", nullable=false, length=0)
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    @Column(name="organization", nullable=false, length=4000)
    public String getOrganization() {
        return this.organization;
    }
    
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    @Column(name="first_name", nullable=false)
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Column(name="last_name", nullable=false)
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @Column(name="middle_name")
    public String getMiddleName() {
        return this.middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    @Column(name="serial_number", nullable=false)
    public int getSerialNumber() {
        return this.serialNumber;
    }
    
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    @Column(name="document_number", nullable=false)
    public int getDocumentNumber() {
        return this.documentNumber;
    }
    
    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="personIdentity")
    public Set<PersonIdentityAttribute> getPersonIdentityAttributes() {
        return this.personIdentityAttributes;
    }
    
    public void setPersonIdentityAttributes(Set<PersonIdentityAttribute> personIdentityAttributes) {
        this.personIdentityAttributes = personIdentityAttributes;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="personIdentity")
    public Set<Identities> getIdentitieses() {
        return this.identitieses;
    }
    
    public void setIdentitieses(Set<Identities> identitieses) {
        this.identitieses = identitieses;
    }




}


