package com.wzc.hibernate.one2one.primary;

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
		
	
		Manager manager = department.getManager();
		
		System.out.println(manager.getMgrName());
	}

	@Test
	public void testSave() {

		Department department = new Department();
		department.setDeptName("DEPT-AA");

		Manager manager = new Manager();
		manager.setMgrName("MGR-AA");

		department.setManager(manager);
		manager.setDepartment(department);

		//保存操作
		//在这里与之前用外键映射的不同，不管先插入哪一个都会先插入 manager 再插入 department 不会有
		//多余的 UPDATE
		session.save(department);
		session.save(manager);
	}

}
