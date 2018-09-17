package com.wzc.hibernate.one2one.foreign;

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
	public void testGet2(){
		
		//在查询没有外键的实体对象时，使用的是左外连接查询（left outer join），
		//将关联的那个实体对象一并查出并进行初始化
		
		Manager manager = session.get(Manager.class, 1);
		System.out.println(manager.getMgrName());
		System.out.println(manager.getDepartment().getDeptName());
	}
	
	@Test
	public void testGet(){
		
		//1、这里加载 Manager 对象时使用的是懒加载
		//2、可能会发生懒加载异常
		Department department = session.get(Department.class, 1);
		
		System.out.println(department.getDeptName());
		
		//获得 Manager 对象时，发送的 SQL查询语句是  manager0_.MGR_ID=department1_.DEPT_ID ，
		//正确的应该是 manager0_.MGR_ID=department1_.MGR_ID
		//所以需要在 Manager.hbm.xml 文件中 one-to-one 中配置 property-ref="manager" 属性，使得在查询时
		//在获得 department 对象后 使用其 manager 属性所对应外键的列来查询 Manager 数据表中对应的元素
		
		Manager manager = department.getManager();
		
		System.out.println(manager.getMgrName());
	}

	@Test
	public void testSave() {

		Department department = new Department();
		department.setDeptName("DEPT-BB");

		Manager manager = new Manager();
		manager.setMgrName("MGR-BB");

		department.setManager(manager);
		manager.setDepartment(department);

		//保存操作
		//建议先保存没有外键列的对象，这样可以减少 UPDATE 语句
		session.save(manager);
		session.save(department);
	}

}
