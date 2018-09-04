package com.wzc.hibernate.entities.n21both;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	private Integer customerId;
	private String customerName;

	//Ë«Ïòn-1
	private Set<Order> set = new HashSet<>();

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName
	 *            the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the set
	 */
	public Set<Order> getSet() {
		return set;
	}

	/**
	 * @param set the set to set
	 */
	public void setSet(Set<Order> set) {
		this.set = set;
	}
	

}
