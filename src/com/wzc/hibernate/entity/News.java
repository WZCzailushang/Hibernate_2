package com.wzc.hibernate.entity;

import java.sql.Blob;
import java.util.Date;

//javabean类
//对于每个属性，必须要写get和set方法，否则会抛异常

public class News {
	/*
	 * 提高开发效率:
	 * A:帮助我们自动提供构造方法
	 * 	a:无参构造方法 
	 * 		在代码区域右键--source--Generate Constructors from Superclass
	 * 		atl+shift+s+c
	 *	b:带参构造方法 
	 *		在代码区域右键--source--Generate Constructors using fields.. -- finish
	 *		atl+shift+s+o
	 * B:成对的getXxx()和setXxx()
	 * 		在代码区域右键--source--Generate Getters and Setters...
	 * 		atl+shift+s+r
	 */
	
	
	
	private Integer id;
	private String title;
	private String author;
	
	private Date date;
	
	//测试Property中的formula属性
	//用于描述：title:author
	
	private String desc;
	
	//大文本
	private String context;
	
	//二进制大文件
	private Blob image;
	

	public News() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public News(String title, String author, Date date) {
		super();
		this.title = title;
		this.author = author;
		this.date = date;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}


	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String Desc) {
		desc = Desc;
	}

	

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}


	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}


	/**
	 * @return the image
	 */
	public Blob getImage() {
		return image;
	}


	/**
	 * @param image the image to set
	 */
	public void setImage(Blob image) {
		this.image = image;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", author=" + author + ", date=" + date + "]";
	}
	
	

}
