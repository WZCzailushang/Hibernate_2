package com.wzc.hibernate.entity;

import com.sun.xml.internal.ws.message.PayloadElementSniffer;

public class Worker {
	private Integer id;
	
	private String name;
	
	private Pay pay;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pay
	 */
	public Pay getPay() {
		return pay;
	}

	/**
	 * @param pay the pay to set
	 */
	public void setPay(Pay pay) {
		this.pay = pay;
	}
	

}
