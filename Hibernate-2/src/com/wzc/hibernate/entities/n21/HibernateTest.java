package com.wzc.hibernate.entities.n21;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.bytecode.enhance.spi.EnhancementContextWrapper;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	public void testDelete(){
		//在不设定级联关系的情况下，且 1 的一端有 n 的对象在引用，则不能删除
		Customer customer = session.get(Customer.class, 1);
		session.delete(customer);
	}
	
	@Test
	public void testUpdate(){
		Order order = session.get(Order.class, 1);
		order.getCustomer().setCustomerName("AAA");
	}

	// 由Order来查Customer
	@Test
	public void testMany2OneGet() {
		
		//1、若查询多的一端的一个对象，则默认情况下，只获得了一个多的那端的对象，
		//而没有与之关联的一的那端的对象
		Order order = session.get(Order.class, 1);
		System.out.println(order.getOrderName());
		
		
//		session.close();//在这里关闭session会发生懒加载异常
		
		//2、在需要使用到对应的customer对象时，才发送对应的SQL语句，此为懒加载
		Customer customer = order.getCustomer();
		System.out.println(customer.getCustomerName());
		
		//3、在查询Customer对象时，从多的一端导航到1的一端时
		//若关闭session时，默认情况下会发生懒加载异常 LazyInitializationException
		
		//4、在获取Order对象时，在默认情况下，其获得的Customer对象是一个代理对象，只有要用到时，才会初始化
		
	}

	@Test
	public void testMany2OneSave() {
		Customer customer = new Customer();
		customer.setCustomerName("AA");

		Order order1 = new Order();
		order1.setOrderName("order-1");

		Order order2 = new Order();
		order2.setOrderName("order-2");

		Order order3 = new Order();
		order3.setOrderName("order-3");

		Order order4 = new Order();
		order4.setOrderName("order-4");

		order1.setCustomer(customer);
		order2.setCustomer(customer);
		order3.setCustomer(customer);
		order4.setCustomer(customer);

		// 一个Customer可以对应多个Order，Order中含有Customer的引用，Order对应的数据表中包含Customer的主键值
		// 先插入1的那端，再插入多的那端，只有3条 INSERT 语句 推荐
		// 先插入Customer，再插入Order，只有 INSERT语句
		// session.save(customer);
		// session.save(order1);
		// session.save(order2);

		// 先插入多的那端，再插入1的那端，除了3条 INSERT 语句，还有 两条 UPDATE 语句
		// 因为在插入多的那端时，无法确定1的一端的主键值，只有等1的主键值确定了
		session.save(order3);
		session.save(order4);
		session.save(customer);

	}

}
