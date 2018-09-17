package com.wzc.hibernate.union.subclass;

import java.util.List;
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
	
	/**
	 * 使用union-subclass的优点：
	 * 1、无需使用辨别者列
	 * 2、子类独有的字段能添加非空约束
	 * 
	 * 缺点：
	 * 1、有冗余字段
	 * 
	 * 2、更新父表的效率较低
	 */
	
	public void testUpdate(){
		String sql = "UPDATE Person p SET p.age=20";
		session.createQuery(sql).executeUpdate();
	}
	

	/**
	 * 查询：
	 * 1、查询父类对象，需将父类对象和子类对象汇总在一起再进行查询
	 * 2、查询子类对象，也只需要查询一张数据表
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
