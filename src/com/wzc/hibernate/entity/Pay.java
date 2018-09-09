package com.wzc.hibernate.entity;

public class Pay {
	
	private int monthlyPay;
	
	private int yearPay;
	
	private int vocationWithPay;
	
	private Worker worker;
	

	/**
	 * @return the monthlyPay
	 */
	public int getMonthlyPay() {
		return monthlyPay;
	}

	/**
	 * @param monthlyPay the monthlyPay to set
	 */
	public void setMonthlyPay(int monthlyPay) {
		this.monthlyPay = monthlyPay;
	}

	/**
	 * @return the yearPay
	 */
	public int getYearPay() {
		return yearPay;
	}

	/**
	 * @param yearPay the yearPay to set
	 */
	public void setYearPay(int yearPay) {
		this.yearPay = yearPay;
	}

	/**
	 * @return the vocationWithPay
	 */
	public int getVocationWithPay() {
		return vocationWithPay;
	}

	/**
	 * @param vocationWithPay the vocationWithPay to set
	 */
	public void setVocationWithPay(int vocationWithPay) {
		this.vocationWithPay = vocationWithPay;
	}

	/**
	 * @return the worker
	 */
	public Worker getWorker() {
		return worker;
	}

	/**
	 * @param worker the worker to set
	 */
	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	
	
	

}
