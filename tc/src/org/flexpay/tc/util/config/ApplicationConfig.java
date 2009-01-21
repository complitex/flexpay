package org.flexpay.tc.util.config;

public class ApplicationConfig extends org.flexpay.ab.util.config.ApplicationConfig{

	private int maximumFloors;
	private int maximuPporches;
	private int maximumAppartments;

	public int getMaximumFloors() {
		return maximumFloors;
	}

	public int getMaximuPporches() {
		return maximuPporches;
	}

	public int getMaximumAppartments() {
		return maximumAppartments;
	}

	public void setMaximumFloors(int maximumFloors) {
		this.maximumFloors = maximumFloors;
	}

	public void setMaximuPporches(int maximuPporches) {
		this.maximuPporches = maximuPporches;
	}

	public void setMaximumAppartments(int maximumAppartments) {
		this.maximumAppartments = maximumAppartments;
	}
}
