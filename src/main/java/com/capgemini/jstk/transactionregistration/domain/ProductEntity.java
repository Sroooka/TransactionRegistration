package com.capgemini.jstk.transactionregistration.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.capgemini.jstk.transactionregistration.domain.impl.AbstractEntity;

@Entity
@Table(name = "PRODUCT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ProductEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 7734662192684689750L;

	private String name;
	
	@Column(precision=10, scale=2)
	private double unitPrice;
	
	private int marginPercent;
	
	@Column(precision=10, scale = 4)
	private double weight;

	@ManyToMany (mappedBy = "products")
	private Collection<TransactionEntity> transactions = new ArrayList<>();
	
	public ProductEntity() {
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
	
	public Collection<TransactionEntity> getTransactions() {
		return transactions;
	}

	public void setTransactions(Collection<TransactionEntity> transactions) {
		this.transactions = transactions;
	}
}
