package com.capgemini.jstk.transactionregistration.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.capgemini.jstk.transactionregistration.domain.impl.AbstractEntity;

@Entity
@Table(name = "CUSTOMER")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CustomerEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 2074780602801252300L;
	
	private String name;
	
	private String surname;
	
	private String email;
	
	private String phone;
	
	private String address;
	
	@Temporal(TemporalType.DATE)
	private Date birth;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    Set<TransactionEntity> transactions = new HashSet<>();

	public CustomerEntity() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String telephone) {
		this.phone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Set<TransactionEntity> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<TransactionEntity> transaction) {
		this.transactions = transaction;
	}
}
