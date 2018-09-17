package com.wzc.hibernate.n2n;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

public class HibernateTest {

	private SessionFactory SessionFactory;

	private Session session;

	private Transaction transaction;

	@Before
	public void init() {
		System.out.println("init");

		Configuration configuration = new Configuration().configure();

		SessionFactory = configuration.buildSessionFactory();


		session = SessionFactory.openSession();

		transaction = session.beginTransaction();
	}

	@After
	public void destroy() {
		System.out.println("destroy");
		transaction.commit();
		session.close();
		SessionFactory.close();
	}
	
	@Test
	public void testGet(){
		Category category = session.get(Category.class, 1);
		System.out.println(category.getName());
		
		//这里使用的是懒加载， inner join 需要连接中间表
		Set<Item> items = category.getItems();
		System.out.println(items.size());
	}
	
	@Test
	public void testSave(){
		
		Category category1 = new Category();
		category1.setName("C-AA");
		
		Category category2 = new Category();
		category2.setName("C-BB");
		
		Item item1 = new Item();
		item1.setName("I-AA");
		
		Item item2 = new Item();
		item2.setName("I-BB");
		
		category1.getItems().add(item1);
		category1.getItems().add(item2);
		
		
		category2.getItems().add(item1);
		category2.getItems().add(item2);
		
		
		//双向多对多需要
		item1.getCategories().add(category1);
		item1.getCategories().add(category2);
		
		item2.getCategories().add(category1);
		item2.getCategories().add(category2);
		
				
		session.save(category1);
		session.save(category2);
		
		session.save(item1);
		session.save(item2);
		
	}

}
