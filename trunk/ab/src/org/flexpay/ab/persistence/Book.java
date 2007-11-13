package org.flexpay.ab.persistence;

import javax.persistence.*;

@Entity
public class Book {

	private Long id;
	private String title;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
