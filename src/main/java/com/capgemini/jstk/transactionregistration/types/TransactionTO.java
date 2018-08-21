package com.capgemini.jstk.transactionregistration.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.capgemini.jstk.transactionregistration.exceptions.MissingAttributeException;

public class TransactionTO {
	
	private Long id;
	
	private Date date;
	
	private TransactionStatus status;
	
	private int productsAmount;
	
	private Long customerId;
	
    private Collection<Long> cartIds = new ArrayList<>();

	public TransactionTO() {
		super();
	}

	public TransactionTO(Long id, Date date, TransactionStatus status, int productsAmount, Long customerId,
			Collection<Long> cartIds) {
		super();
		this.id = id;
		this.date = date;
		this.status = status;
		this.productsAmount = productsAmount;
		this.customerId = customerId;
		this.cartIds = cartIds;
	}
	
	public static class TransactionTOBuilder{

		private Long id;
		
		private Date date;
		
		private TransactionStatus status;
		
		private int productsAmount;
		
		private Long customerId;
		
	    private Collection<Long> cartIds = new ArrayList<>();
	    
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
	    
	    public TransactionTOBuilder withCartIds(Collection<Long> cartIds) {
			this.cartIds = cartIds;
			return this;
		}
	    
	    public TransactionTO build() {
			checkBeforeBuild(date, status, productsAmount, customerId, cartIds);
			return new TransactionTO(id, date, status, productsAmount, customerId, cartIds);
		}

		private void checkBeforeBuild(Date date, TransactionStatus status, int productsAmount, Long customerId,
				Collection<Long> cartIds) {
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

	public Collection<Long> getCartIds() {
		return cartIds;
	}

	public void setCartIds(Collection<Long> cartIds) {
		this.cartIds = cartIds;
	}
}
