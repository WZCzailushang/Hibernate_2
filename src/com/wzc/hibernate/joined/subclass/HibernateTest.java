package com.wzc.hibernate.joined.subclass;

import java.util.List;
import java.util.Set;

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
	
	/**
	 * 使用joined-subclass的优点：
	 * 1、不需要使用了辨别者列
	 * 2、子类独有的字段能添加非空约束
	 * 3、没有冗余的字段
	 */

	/**
	 * 查询：
	 * 1、查询父类对象，做一个左外连接查询
	 * 2、查询子类对象，做一个内查询
	 */
	@Test
	public void testQuery(){
		
		/**
		 * 这里查询使用了多态。将数据表中Person和Person的子类全都查了出来。persons.size()为2
		 */
		List<Person> persons = session.createQuery("FROM Person").list();
		System.out.println("persons.size:"+persons.size());
		
		/**
		 * 这里只查询了student对象的数量，students.size()为1
		 */
		List<Student> students = session.createQuery("FROM Student").list();
		System.out.println(students.size());
		
	}

	/**
	 * 插入操作：
	 *  1、插入子类对象至少要插入到两张数据表中
	 */
	@Test
	public void testSave() {

		Person person = new Person();

		person.setAge(24);
		person.setName("WZC");
		session.save(person);

		Student student = new Student();
		student.setAge(23);
		student.setName("WZC");
		student.setSchool("chongyou");

		session.save(student);

	}

}
