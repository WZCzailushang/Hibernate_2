package com.wzc.hibernate.entity;

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

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import sun.management.VMOptionCompositeData;

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
	 * 测试组件
	 * Worker内有一个Pay类引用
	 * 配置文件中配置Component
	 */
	
	@Test
	public void testComponent(){
		Worker worker = new Worker();
		
		Pay pay = new Pay();
		
		pay.setMonthlyPay(1000);
		pay.setYearPay(20000);
		pay.setVocationWithPay(5);
		
		worker.setName("ABCD");
		worker.setPay(pay);
		
		session.save(worker);
	}
	
	@Test
	public void testBlob() throws IOException, SQLException{
		
		//将Blob对象存到数据库中
//		News news = new News();
//		news.setAuthor("cc");
//		news.setContext("Context");
//		news.setDate(new Date());
//		news.setTitle("CC");
//		news.setDesc("Desc");
//		
//		InputStream iStream = new FileInputStream("DSCF1814.jpg");
//		
//		Blob image = Hibernate.getLobCreator(session).createBlob(iStream, iStream.available());
//		news.setImage(image);
//		
//		session.save(news);
		
		//将Blob对象从数据库中取出来
		
		News news = session.get(News.class, 1);
		Blob image = news.getImage();
		
		InputStream iStream = image.getBinaryStream();
		System.out.println(iStream.available());
	}
	
	/**
	 * 测试Property 中的formula属性
	 * 设置一个 SQL 表达式, Hibernate 将根据它来计算出派生属性的值. 
	 *派生属性: 并不是持久化类的所有属性都直接和表的字段匹配, 持久化类的有些属性的值必须在运行时通过计算才能得出来, 这种属性称为派生属性
	 */
	@Test
	public void testPropertyFormula(){
		News news = session.load(News.class, 1);
		news.setAuthor("Oracle");

		
		System.out.println(news.getDesc());
	}
	
	/**
	 * 测试ID标识器，即数据表中ID与对象的OID对应关系策略
	 */
	@Test
	public void testIdIdentifier(){
		News news = new News("AA","bb",new java.sql.Date(new Date().getTime()));
		
		session.save(news);
	}
	
	/**
	 * 测试动态更新
	 * 在hbm.xml中配置dynamic-update: 
	 * 若设置为 true, 表示当更新一个对象时, 会动态生成 update 语句, 
	 * update 语句中仅包含所有取值需要更新的字段. 默认值为 false
	 */
	@Test
	public void testDynamicUpdate(){
		News news = session.get(News.class, 1);
		news.setAuthor("Oracle");
	}
	
	/**
	 * 调用JDBC原生的存储过程
	 * 如果这里打印的Connection显示是来自C3P0，则表示在配置文件hibernate.cfg.xml中c3p0数据源已经配好了
	 */
	@Test
	public void testDoWork() {
		// TODO Auto-generated method stub
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				
				System.out.println(connection);
				
				//在这里可以通过Connection调用JDBC原生的存储过程
				
			}
		});

	}

	/**
	 * evict：从session缓存中把指定的持久化对象移除
	 */

	@Test
	public void testEvict() {
		News news = session.get(News.class, 1);
		News news2 = session.get(News.class, 2);

		news.setTitle("AA");
		news2.setTitle("BB");

		session.evict(news);// 在提交事务之前将持久化对象从缓存中移除，就不会执行更新操作

	}

	/**
	 * delete：执行删除操作 若OID和数据表中的一条记录对应，就会准备执行删除操作 若OID在数据表中没有相应的记录，则抛出异常
	 * 
	 * delete是在执行完flush后才进行删除，而不是调用方法后马上删，并且删除完的对象不能再进行saveOrUpdate操作，
	 * 因为OID不为null 可以通过设置 hibernate 配置文件
	 * hibernate.use_identifier_rollback为true，使删除对象后， 把其OID设为null
	 */

	@Test
	public void testDelete() {

		// 这个是一个游离对象，可以进行删除
		// News news = new News();
		// news.setId(4);

		// 这是一个持久化对象，可以进行删除
		News news = session.get(News.class, 6);

		session.delete(news);

		System.out.println(news);
	}

	/**
	 * Session 的 saveOrUpdate() 方法同时包含了 save() 与 update() 方法的功能
	 * 如果是游离对象，则update，如果是临时对象，则save 判定对象为临时对象的标准 1、Java 对象的 OID 为 null 2、映射文件中为
	 * <id> 设置了 unsaved-value 属性, 并且 Java 对象的 OID 取值与这个 unsaved-value 属性值匹配
	 * 
	 * 注意： 1、若OID不为null，但数据表中还没有和其对应的记录，则抛异常 2、了解：OID的值等于ID的 unsaved-value
	 * 属性值的对象，也被认为是一个游离对象
	 * 
	 */

	@Test
	public void testSaveOrUpdate() {
		News news = new News("FFF", "ff", new Date());
		// news.setId(100);//若数据表中没有此ID，则会抛异常

		
		session.saveOrUpdate(news);
		
	}

	/**
	 * update: 1、若更新一个持久化对象，不需要显式地调用update方法，因为在调用Transaction的commit方法时
	 * ，会先执行session的flush操作
	 * 
	 * 2、更新一个游离对象，需要显式地调用session的update方法，可以把一个游离对象变为持久化对象
	 * 
	 * 
	 * 需要注意的点： 1、无论要更新的游离对象和数据表中的记录是否一致，都会发送 UPDATE 语句，注意是游离对象，而不是持久化对象，
	 * 持久化对象在未修改对象的属性时调用update方法不会发送 UPDATE 语句 如何能让update方法不再盲目地发送 UPDATE 语句？在
	 * .hbm.xml的class 节点设置 select-before-update=true（默认为false），但通常不需要设置该属性
	 * 那么在调用update方法更新之前会发送一个 SELECT 语句再查询一次，如果相同则不会发送 UPDATE 语句
	 * 
	 * 2、如果数据表中没有对应的记录,但还调用了 update方法，则会抛出异常
	 * 
	 * 3、当 update() 方法关联一个游离对象时, 如果在 Session 的缓存中已经存在相同 OID 的持久化对象,
	 * 会抛出异常，因为在session缓存中，不能有两个OID相同的对象
	 */
	@Test
	public void testUpdate() {
		News news = session.get(News.class, 4);// 切记：第二个参数是在数据表中对应的id，如果没有相应的id，则获取不到对象

		/**
		 * 如果加上了下面四行代码，即提交transaction和关闭session后又打开，则必须要调用session.update方法
		 * 此时news已经变成一个游离对象 即在数据表中还有它对应的记录，但已经不在session缓存中了 所以改变它的属性也不会被检测到
		 */

		transaction.commit();
		session.close();

		// news.setId(100); //如果在这个地方将ID改了，那么数据库中没有这个对象的记录，继续执行下面的update语句会抛出异常

		session = SessionFactory.openSession();
		transaction = session.beginTransaction();
		//

		news.setAuthor("Oracle");
		session.update(news);// 此时必须要调用session.update方法才能使修改在数据表中生效

	}

	/**
	 * get VS load: 1、执行get方法：会立即加载对象 执行load方法：若不使用该对象，则不会立即执行查询操作，而是返回一个代理对象
	 * get是立即检索，load是延迟检索
	 * 
	 * 2、若数据表中没有对应的记录，且Session 也没有被关闭 get返回null load
	 * 若不使用这个对象的属性，没问题；若需要初始化了，抛出异常
	 * 
	 * 3、load可能会抛出 LazyInitializationException 异常，原因是：在执行load时先返回的是一个
	 * 代理对象，在代理对象初始化之前将session关闭了，所以会抛出这个异常
	 */

	@Test
	public void testLoad() {

		News news = session.load(News.class, 1);// 第二个参数为在数据表中的id
		// session.close(); //如果执行这句话，就会抛出懒加载异常
		System.out.println(news);

	}

	/**
	 * 从数据库中加载持久化对象
	 */

	@Test
	public void testGet() {
		News news = session.get(News.class, 4);
		System.out.println(news);
	}

	/**
	 * persist也是执行保存操作 和save的区别: 在调用persist之前，若对象已经有ID了，则不会执行INSERT 而是会抛出异常
	 */
	@Test
	public void testPersist() {
		News news = new News();
		news.setAuthor("dd");
		news.setTitle("DD");
		news.setDate(new Date());
		// news.setId(100); 这句代码如果执行会抛出异常

		session.persist(news);

	}

	/**
	 * 1、save(): 1)、使一个临时对象变为持久化对象 2)、为对象分配ID 3)、在flush缓存时，会发送一条INSERT语句
	 * 4)、在save()方法之前设置ID是无效的 5)、持久化对象的ID是不能被修改的
	 * 
	 */
	@Test
	public void testSave() {
		News news = new News();
		news.setAuthor("aa");
		news.setTitle("AA");
		news.setId(100);// 这里设置ID是无效的，不会对在数据库中存储的数据起任何作用
		news.setDate(new Date());

		/**
		 * 两次打印不一样 第一次：News [id=null, title=AA, author=aa, date=Mon Aug 27
		 * 21:06:41 CST 2018] 第二次：News [id=10, title=AA, author=aa, date=Mon Aug
		 * 27 21:06:41 CST 2018]
		 * 
		 */
		System.out.println(news);

		session.save(news);

		System.out.println(news);

	}

	/**
	 * clear()：清理session缓存
	 */
	@Test
	public void testClear() {
		News news = session.get(News.class, 1);

		/**
		 * 调用clear()后，就会再发一条SQL的查询语句，原先Session中的缓存对象已经没了
		 */
		session.clear();

		News news2 = session.get(News.class, 1);
	}

	/**
	 * refresh():会强制发送 SELECT 语句，以使Session缓存中的对象状态和数据库中数据表的状态保持一致
	 * 这个是改变数据库中的值，进而影响到此处对象的值
	 * 为了使下面两次打印的结果不同（用debug设置断点在第一次打印后改变数据表的值，调用refresh之后
	 * 第二次打印就会更新Session缓存中的对象的值，适用于数据表中的内容改变之后，更新Session缓存对象中的值）
	 * 需要在hibernate.cfg.xml中设置隔离级别为2
	 */
	@Test
	public void testRefresh() {
		News news = session.get(News.class, 1);
		System.out.println(news);

		session.refresh(news);

		System.out.println(news);

	}

	/**
	 * 测试session.flush方法
	 * flush:使数据表中的记录和Session缓存中的对象的状态保持一致，为了保持一致，可能会发送相应的sql语句
	 * 先改变此处对象的值，继而影响到数据库中的值 1、在Transaction的commit方法中，先调用session.flush方法，再提交事务
	 * 2、flush()可能会发送sql语句，但不会提交事务
	 * 3、注意：在未提交事务和显示调用session.flush操作之前，也有可能进行flush操作
	 * 
	 * 1）、执行HQL或QBC查询
	 * 要求查询的是最新的状态，会先进行flush操作，以得到数据表的最新操作，对应下面的testSessionFlush() 2)
	 * 、若记录的id是由底层数据库使用自增方法生成的，则在调用save()时，会立即发送insert语句 因为save方法后，必须保证对象的ID是存在的
	 * 
	 * 
	 */
	@Test
	public void testSessionFlush2() {
		News news = new News("Java", "SUN", new Date());
		session.save(news);
	}

	@Test
	public void testSessionFlush() {
		News news = session.get(News.class, 1);
		/**
		 * 在此处，因为还处于session生命周期和transaction中。所以会在transaction.commit方法执行之前
		 * 调用session.flush方法，将此处的信息和数据库中数据表的信息进行同步，执行sql语句如下： Hibernate: update
		 * NEWS set TITLE=?, AUTHOR=?, DATE=? where ID=?
		 */
		news.setAuthor("SUN");

		/**
		 * 在此处 ，要求查询的结果是最新的，所以session缓存中的对象状态已经因为setAuthor("Oracle")改变,虽然
		 * 数据表中还没有变，但是下面news2的打印已经变了
		 */
		News news2 = (News) session.createCriteria(News.class).uniqueResult();
		System.out.println(news2);

	}

	/**
	 * 测试Session缓存
	 * 
	 */
	@Test
	public void testSessionCache() {

		/**
		 * 连续两条用session从数据库中获取数据，只有第一条执行了sql的查询语句，第二条直接打印 Hibernate: select
		 * news0_.ID as ID1_0_0_, news0_.TITLE as TITLE2_0_0_, news0_.AUTHOR as
		 * AUTHOR3_0_0_, news0_.DATE as DATE4_0_0_ from NEWS news0_ where
		 * news0_.ID=?
		 * 
		 * Session接口的实现中有一些集合，这些集合构成了Session缓存，保存了从数据库中 获得的信息 Session缓存为一级缓存
		 */
		News news = session.get(News.class, 1);
		System.out.println(news);

		News news2 = session.get(News.class, 1);
		System.out.println(news2);

		System.out.println("Test");

	}

}
