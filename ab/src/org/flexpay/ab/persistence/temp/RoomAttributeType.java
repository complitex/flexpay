package org.flexpay.ab.persistence.temp;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * RoomAttributeType generated by hbm2java
 */
@Entity
@Table (name = "room_attribute_type"
		, catalog = "flexpay_db"
)
public class RoomAttributeType implements java.io.Serializable {


	private int id;
	private String attributeTypeCode;
	private Set<RoomAttributeTypeName> roomAttributeTypeNames = new HashSet<RoomAttributeTypeName>(0);
	private Set<RoomAttribute> roomAttributes = new HashSet<RoomAttribute>(0);

	public RoomAttributeType() {
	}


	public RoomAttributeType(int id, String attributeTypeCode) {
		this.id = id;
		this.attributeTypeCode = attributeTypeCode;
	}

	public RoomAttributeType(int id, String attributeTypeCode, Set<RoomAttributeTypeName> roomAttributeTypeNames, Set<RoomAttribute> roomAttributes) {
		this.id = id;
		this.attributeTypeCode = attributeTypeCode;
		this.roomAttributeTypeNames = roomAttributeTypeNames;
		this.roomAttributes = roomAttributes;
	}

	@Id

	@Column (name = "ID", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column (name = "attribute_Type_code", nullable = false)
	public String getAttributeTypeCode() {
		return this.attributeTypeCode;
	}

	public void setAttributeTypeCode(String attributeTypeCode) {
		this.attributeTypeCode = attributeTypeCode;
	}

	@OneToMany (fetch = FetchType.LAZY, mappedBy = "roomAttributeType")
	public Set<RoomAttributeTypeName> getRoomAttributeTypeNames() {
		return this.roomAttributeTypeNames;
	}

	public void setRoomAttributeTypeNames(Set<RoomAttributeTypeName> roomAttributeTypeNames) {
		this.roomAttributeTypeNames = roomAttributeTypeNames;
	}

	@OneToMany (fetch = FetchType.LAZY, mappedBy = "roomAttributeType")
	public Set<RoomAttribute> getRoomAttributes() {
		return this.roomAttributes;
	}

	public void setRoomAttributes(Set<RoomAttribute> roomAttributes) {
		this.roomAttributes = roomAttributes;
	}

}


