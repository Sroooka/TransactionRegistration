package com.capgemini.jstk.transactionregistration.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.capgemini.jstk.transactionregistration.domain.impl.AbstractEntity;
import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;

@Entity
@Table(name = "TRANSACTION")
public class TransactionEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 7386021909131036194L;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private TransactionStatus status;
	
	private int productsAmount = 0;

	public TransactionEntity() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public int getProductsAmount() {
		return productsAmount;
	}

	public void setProductsAmount(int productsAmount) {
		this.productsAmount = productsAmount;
	}
}
