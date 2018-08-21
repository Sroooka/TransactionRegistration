package com.capgemini.jstk.transactionregistration.domain;

import java.io.Serializable;

import javax.persistence.Column;

import com.capgemini.jstk.transactionregistration.domain.impl.AbstractEntity;

public class ProductEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 7734662192684689750L;

	private String name;
	
	@Column(precision=10, scale=2)
	private double unitPrice;
	
	private int marginPercent;
	
	@Column(precision=10, scale = 4)
	private double weight;

	public ProductEntity() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getMarginPercent() {
		return marginPercent;
	}

	public void setMarginPercent(int marginPercent) {
		this.marginPercent = marginPercent;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}	
}
