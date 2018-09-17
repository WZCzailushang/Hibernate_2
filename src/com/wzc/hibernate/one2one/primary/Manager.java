package com.wzc.hibernate.one2one.primary;

public class Manager {
	
	private Integer mgrId;
	
	private String mgrName;
	
	private Department department;

	/**
	 * @return the mgrId
	 */
	public Integer getMgrId() {
		return mgrId;
	}

	/**
	 * @param mgrId the mgrId to set
	 */
	public void setMgrId(Integer mgrId) {
		this.mgrId = mgrId;
	}

	/**
	 * @return the mgrName
	 */
	public String getMgrName() {
		return mgrName;
	}

	/**
	 * @param mgrName the mgrName to set
	 */
	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	

}
