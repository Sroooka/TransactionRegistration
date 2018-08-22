package com.capgemini.jstk.transactionregistration.types;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.capgemini.jstk.transactionregistration.exceptions.MissingAttributeException;

public class TransactionTO {
	
	private Long id;
	
	private Date date;
	
	private TransactionStatus status;
	
	private int productsAmount;
	
	private Long customerId;
	
    private Collection<Long> productIds = new HashSet<>();

	public TransactionTO() {
		super();
	}

	public TransactionTO(Long id, Date date, TransactionStatus status, int productsAmount, Long customerId,
			Collection<Long> productIds) {
		super();
		this.id = id;
		this.date = date;
		this.status = status;
		this.productsAmount = productsAmount;
		this.customerId = customerId;
		this.productIds = productIds;
	}
	
	public static class TransactionTOBuilder{

		private Long id;
		
		private Date date;
		
		private TransactionStatus status;
		
		private int productsAmount;
		
		private Long customerId;
		
	    private Collection<Long> productIds = new HashSet<>();
	    
	    public TransactionTOBuilder() {
			super();
		}
	    
	    public TransactionTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}
	    
	    public TransactionTOBuilder withDate(Date date) {
			this.date = date;
			return this;
		}
	    
	    public TransactionTOBuilder withTransactionStatus(TransactionStatus status) {
			this.status = status;
			return this;
		}
	    
	    public TransactionTOBuilder withProductsAmount(int productsAmount) {
			this.productsAmount = productsAmount;
			return this;
		}
	    
	    public TransactionTOBuilder withCustomerId(Long customerId) {
			this.customerId = customerId;
			return this;
		}
	    
	    public TransactionTOBuilder withProductIds(Collection<Long> productIds) {
			this.productIds = productIds;
			return this;
		}
	    
	    public TransactionTO build() {
			checkBeforeBuild(date, status, productsAmount, customerId, productIds);
			return new TransactionTO(id, date, status, productsAmount, customerId, productIds);
		}

		private void checkBeforeBuild(Date date, TransactionStatus status, int productsAmount, Long customerId,
				Collection<Long> productIds) {
			boolean canThrowException = false;
			String errorMessage = " ";
			if (date == null || date.before(new GregorianCalendar(1900, 1, 1).getTime())) {
				canThrowException = true;
				errorMessage += "[Date] ";
			}
			if (status == null) {
				canThrowException = true;
				errorMessage += "[Transaction Status] ";
			}
			if (canThrowException) {
				throw new MissingAttributeException(errorMessage, "TransactionTO");
			}
			
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Collection<Long> getProductIds() {
		return productIds;
	}

	public void setProductIds(Collection<Long> cartIds) {
		this.productIds = cartIds;
	}
}
