package com.wzc.hibernate.entities.n21both;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	private Integer customerId;
	private String customerName;

	//双向n-1，把集合初始化可以防止空指针问题
	/**
	 * 1、在声明集合类型时，需使用接口类型，因为 Hibernate 在获取集合类型时，返回的是 Hibernate内置的
	 * 集合类型，而不是 JavaSe 中的一个标准集合实现
	 * 2、需要把集合初始化。防止空指针的发生
	 */
	
	private Set<Order> orders = new HashSet<>();

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @return the orders
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
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



}
