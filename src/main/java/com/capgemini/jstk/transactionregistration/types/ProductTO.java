package com.capgemini.jstk.transactionregistration.types;

import java.util.Collection;
import java.util.HashSet;

import com.capgemini.jstk.transactionregistration.exceptions.MissingAttributeException;

public class ProductTO {

	private Long id;
	
	private String name;
	
	private double unitPrice;
	
	private int marginPercent;
	
	private double weight;
		
	private Collection<Long> transactionIds = new HashSet<>();

	public ProductTO() {
		super();
	}

	public ProductTO(Long id, String name, double unitPrice, int marginPercent, double weight,
			Collection<Long> transactionIds) {
		super();
		this.id = id;
		this.name = name;
		this.unitPrice = unitPrice;
		this.marginPercent = marginPercent;
		this.weight = weight;
		this.transactionIds = transactionIds;
	}
	
	public static class ProductTOBuilder{

		private Long id;
		
		private String name;
		
		private double unitPrice;
		
		private int marginPercent;
		
		private double weight;
		
		private Collection<Long> transactionIds = new HashSet<>();
		
		public ProductTOBuilder() {
			super();
		}
		
		public ProductTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}
		
		public ProductTOBuilder withName(String name) {
			this.name = name;
			return this;
		}
		
		public ProductTOBuilder withUnitPrice(double unitPrice) {
			this.unitPrice = unitPrice;
			return this;
		}
		
		public ProductTOBuilder withMarginPercent(int marginPercent) {
			this.marginPercent = marginPercent;
			return this;
		}
		
		public ProductTOBuilder withWeight(Double weight) {
			this.weight = weight;
			return this;
		}
		
		public ProductTOBuilder withTransactionIds(Collection<Long> transactionIds) {
			this.transactionIds = transactionIds;
			return this;
		}
		
		public ProductTO build() {
			checkBeforeBuild(id, name, unitPrice, marginPercent, weight, transactionIds);
			return new ProductTO(id, name, unitPrice, marginPercent, weight, transactionIds);
		}

		private void checkBeforeBuild(Long id, String name, double unitPrice, int marginPercent, double weight,
				Collection<Long> transactionIds) {
			boolean canThrowException = false;
			String errorMessage = " ";
			if (name == null || name.isEmpty()) {
				canThrowException = true;
				errorMessage += "[Name] ";
			}
			if (unitPrice <= 0) {
				canThrowException = true;
				errorMessage += "[Unit Price] ";
			}
			if (marginPercent < 0) {
				canThrowException = true;
				errorMessage += "[Margin Percent] ";
			}
			if (weight <= 0) {
				canThrowException = true;
				errorMessage += "[Weight] ";
			}
			if (canThrowException) {
				throw new MissingAttributeException(errorMessage, "ProductTO");
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Collection<Long> getTransactionIds() {
		return transactionIds;
	}

	public void setTransactionIds(Collection<Long> transactionIds) {
		this.transactionIds = transactionIds;
	}
}
