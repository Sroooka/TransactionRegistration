package com.capgemini.jstk.transactionregistration.domain;

import java.util.Date;

public class TransactionSearchCriteria {

	private String customerName;

	private String customerSurame;

	private Date from;

	private Date to;

	private Long productId;

	private double totalPrice;

	public TransactionSearchCriteria() {
	}

	public TransactionSearchCriteria(String customerName, String customerSurame, Date from, Date to, Long productId,
			double totalPrice) {
		super();
		this.customerName = customerName;
		this.customerSurame = customerSurame;
		this.from = from;
		this.to = to;
		this.productId = productId;
		this.totalPrice = totalPrice;
	}

	public String getCustomerSurame() {
		return customerSurame;
	}

	public void setCustomerSurame(String customerSurame) {
		this.customerSurame = customerSurame;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
