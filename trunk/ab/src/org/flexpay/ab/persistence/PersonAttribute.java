package org.flexpay.ab.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PersonAttribute generated by hbm2java
 */
@Entity
@Table(name="person_attribute"
    ,catalog="flexpay_db"
)
public class PersonAttribute  implements java.io.Serializable {


     private int id;
     private Person person;
     private String value;
     private String name;
     private String lang;

    public PersonAttribute() {
    }

    public PersonAttribute(int id, Person person, String value, String name, String lang) {
       this.id = id;
       this.person = person;
       this.value = value;
       this.name = name;
       this.lang = lang;
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
    @JoinColumn(name="Person_ID", nullable=false)
    public Person getPerson() {
        return this.person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }
    
    @Column(name="VALUE", nullable=false, length=4000)
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @Column(name="NAME", nullable=false)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="LANG", nullable=false)
    public String getLang() {
        return this.lang;
    }
    
    public void setLang(String lang) {
        this.lang = lang;
    }




}


