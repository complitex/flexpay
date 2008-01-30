package org.flexpay.ab.persistence.temp;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import org.flexpay.ab.persistence.Apartment;

import javax.persistence.*;

/**
 * Rooms generated by hbm2java
 */
@Entity
@Table (name = "rooms"
		, catalog = "flexpay_db"
)
public class Rooms implements java.io.Serializable {


	private int id;
	private Room room;
	private Apartment apartment;
	private int roomId;

	public Rooms() {
	}

	public Rooms(int id, Room room, Apartment apartment, int roomId) {
		this.id = id;
		this.room = room;
		this.apartment = apartment;
		this.roomId = roomId;
	}

	@Id

	@Column (name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "id", unique = true, nullable = false, insertable = false, updatable = false)
	public Room getRoom() {
		return this.room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "Apartment_ID", nullable = false)
	public Apartment getApartment() {
		return this.apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	@Column (name = "Room_ID", nullable = false)
	public int getRoomId() {
		return this.roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

}


