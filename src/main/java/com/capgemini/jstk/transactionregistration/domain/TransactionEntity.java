package com.capgemini.jstk.transactionregistration.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import com.capgemini.jstk.transactionregistration.domain.impl.AbstractEntity;
import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;

@Entity
@Table(name = "TRANSACTION")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TransactionEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 7386021909131036194L;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	
	private int productsAmount = 0;
	
	@ManyToOne
	private CustomerEntity customer;
	
	@ManyToMany
    @JoinTable(name = "DEAL",
            joinColumns = {@JoinColumn(name = "TRANSACTION_ID")},
            inverseJoinColumns = {@JoinColumn(name = "PRODUCT_ID")}
    )
    private Collection<ProductEntity> cart = new ArrayList<>();

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
