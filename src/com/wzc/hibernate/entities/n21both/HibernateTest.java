package com.wzc.hibernate.entities.n21both;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;

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
		System.out.println("null+" + SessionFactory == null);

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
	public void testCascade(){
		
	}
	
	
	@Test
	public void testDelete() {
		// 在不设定级联关系的情况下，且 1 的一端有 n 的对象在引用，则不能删除
		Customer customer = session.get(Customer.class, 1);
		session.delete(customer);
	}

	
	/**
	 * 更新操作
	 */
	@Test
	public void testUpdate() {
		Customer customer = session.get(Customer.class, 1);
		customer.getOrders().iterator().next().setOrderName("GGG");
	}

	@Test
	public void testOne2ManyGet() {
		
		//1、对 n 的一端的集合采用延迟加载
		Customer customer = session.get(Customer.class, 1);
		
		System.out.println(customer.getCustomerName());
		
		//2、这里返回的集合类型是 class org.hibernate.collection.internal.PersistentSet  Hibernate内置的集合类型
		//该类型具有延迟加载和存放代理对象的功能
		System.out.println(customer.getOrders().getClass());
		
		//3、可能会发生 LazyInitializationException 懒加载异常
		
		
		//4、在需要使用集合中元素时进行初始化
	}

	// 由Order来查Customer
	@Test
	public void testMany2OneGet() {

		// 1、若查询多的一端的一个对象，则默认情况下，只获得了一个多的那端的对象，
		// 而没有与之关联的一的那端的对象
		Order order = session.get(Order.class, 1);
		System.out.println(order.getOrderName());

		// session.close();//在这里关闭session会发生懒加载异常

		// 2、在需要使用到对应的customer对象时，才发送对应的SQL语句，此为懒加载
		Customer customer = order.getCustomer();
		System.out.println(customer.getCustomerName());

		// 3、在查询Customer对象时，从多的一端导航到1的一端时
		// 若关闭session时，默认情况下会发生懒加载异常 LazyInitializationException

		// 4、在获取Order对象时，在默认情况下，其获得的Customer对象是一个代理对象，只有要用到时，才会初始化

	}

	@Test
	public void testMany2OneSave() {

		// 设置双向关联关系
		Customer customer = new Customer();
		customer.setCustomerName("CC");

		Order order1 = new Order();
		order1.setOrderName("order-3");

		Order order2 = new Order();
		order2.setOrderName("order-4");

		order1.setCustomer(customer);
		order2.setCustomer(customer);

		customer.getOrders().add(order1);
		customer.getOrders().add(order2);

		Order order3 = new Order();
		order3.setOrderName("order-3");

		Order order4 = new Order();
		order4.setOrderName("order-4");

		// order3.setCustomer(customer);
		// order4.setCustomer(customer);

		// 一个Customer可以对应多个Order，Customer中包含Order的集合，Order中含有Customer的引用，Order对应的数据表中包含Customer的主键值和Order的集合值
		// 先插入Customer,再插入Order，有 3条 INSERT 语句， 2 条 UPDATE语句
		// 因为 1 的一端和 n 的一端都要维护关联关系，customer中包含order的集合
		// 通过在 1 的一端的 set 属性设置 inverse=true 来使 1 的一端放弃维护关联关系
		// 建议设定 Customer 中 set 属性的 inverse=true 先插入 1 的一端，再插入 n 的一端，不会多出 UPDATE
		// 语句

		session.save(customer);
		session.save(order1);
		session.save(order2);

		// 先插入多的那端，再插入1的那端，除了 3条 INSERT 语句，还有 4条 UPDATE 语句
		// session.save(order3);
		// session.save(order4);
		// session.save(customer);

	}

}
