package com.ytx.test.base;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 所有测试类的基类，之后的测试类都继承它
 * 配置了spring和junit整合，junit在启动的时候就会加载spring容器
 * ContextConfiguration注解自动加载应用上下文applicationContext，我们不需要手动去加载
 * @author yuantian xin
 *
 */
@Ignore //因为这个测试基类中，没有任何的测试方法，加上ignore忽略他，不然mvn打包的时候会报错
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
			"classpath:spring/spring-dao.xml",
			"classpath:spring/spring-service.xml"})
public class SpringBaseTest {

}
