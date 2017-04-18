package edu.ncat.dempstershafer.dao;

import java.io.Serializable;

public class DSInfo implements Serializable{
	private double belief;
	private double plausiblity;
	public double getBelief() {
		return belief;
	}
	public void setBelief(double belief) {
		this.belief = belief;
	}
	public double getPlausiblity() {
		return plausiblity;
	}
	public void setPlausiblity(double plausiblity) {
		this.plausiblity = plausiblity;
	}
}
