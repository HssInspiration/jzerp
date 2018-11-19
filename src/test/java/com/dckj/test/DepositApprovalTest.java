package com.dckj.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.modules.depositmanagement.depositapproval.service.DepositApprovalService;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-context.xml")
@WebAppConfiguration
//public class DepositApprovalTest extends BaseTest{
public class DepositApprovalTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private DepositApprovalService depositApprovalService;
	
	@Autowired
	private SystemService systemService;

	@Test
	@Transactional // 标明此方法需使用事务
	@Rollback(false) // 标明使用完此方法后事务不回滚,true时为回滚
	public void testDeposit() {
//		System.out.println("测试Spring整合Junit4进行单元测试");
//		String id = "18e6fe837de14af68afcf8b30074ef68";
//		DepositApproval depositApproval = depositApprovalService.get(id);
//		System.out.println(depositApproval);
		User user = systemService.getUser("1");
		org.junit.Assert.assertNotNull(user);
	}
}
