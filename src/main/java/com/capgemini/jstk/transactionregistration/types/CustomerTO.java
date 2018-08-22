package com.capgemini.jstk.transactionregistration.types;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import com.capgemini.jstk.transactionregistration.exceptions.MissingAttributeException;

public class CustomerTO {

	private Long id;

	private String name;

	private String surname;

	private String email;

	private String phone;

	private String address;

	private Date birth;

	Collection<Long> transactionIds = new HashSet<>();

	public CustomerTO() {
		super();
	}

	public CustomerTO(Long id, String name, String surname, String email, String phone, String address, Date birth,
			Collection<Long> transactionIds) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.birth = birth;
		this.transactionIds = transactionIds;
	}

	public static class CustomerTOBuilder {

		private Long id = 0L;

		private String name;

		private String surname;

		private String email;

		private String phone;

		private String address;

		private Date birth;

		Collection<Long> transactionIds = new HashSet<>();

		public CustomerTOBuilder() {
			super();
		}

		public CustomerTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public CustomerTOBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public CustomerTOBuilder withSurname(String surname) {
			this.surname = surname;
			return this;
		}

		public CustomerTOBuilder withEmail(String email) {
			this.email = email;
			return this;
		}

		public CustomerTOBuilder withPhone(String phone) {
			this.phone = phone;
			return this;
		}

		public CustomerTOBuilder withAddress(String address) {
			this.address = address;
			return this;
		}

		public CustomerTOBuilder withBirth(Date birth) {
			this.birth = birth;
			return this;
		}
		
		public CustomerTOBuilder withTrasactionIds(Collection<Long> transactionIds) {
			this.transactionIds = transactionIds;
			return this;
		}

		public CustomerTO build() {
			checkBeforeBuild(name, surname, email, phone, address, birth, transactionIds);
			return new CustomerTO(id, name, surname, email, phone, address, birth, transactionIds);
		}

		private void checkBeforeBuild(String name, String surname, String email, String phone, String address,
				Date birth, Collection<Long> transactionIds) throws MissingAttributeException {
			boolean canThrowException = false;
			String errorMessage = " ";
			if (name == null || name.isEmpty()) {
				canThrowException = true;
				errorMessage += "[Name] ";
			}
			if (surname == null || surname.isEmpty()) {
				canThrowException = true;
				errorMessage += "[Surname] ";
			}
			if (address == null || address.isEmpty()) {
				canThrowException = true;
				errorMessage += "[Address] ";
			}
			if (birth == null || 
				birth.before(new GregorianCalendar(1900, 1, 1).getTime()) || 
				birth.after(new Date())
				) {
				canThrowException = true;
				errorMessage += "[Birth date] ";
			}
			if (canThrowException) {
				throw new MissingAttributeException(errorMessage, "CustomerTO");
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

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Collection<Long> getTransactionIds() {
		return transactionIds;
	}

	public void setTransactionIds(List<Long> transactionsId) {
		this.transactionIds = transactionsId;
	}
}
