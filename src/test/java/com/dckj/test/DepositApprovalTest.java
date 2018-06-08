package com.dckj.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.modules.depositmanagement.depositapproval.entity.DepositApproval;
import co.dc.ccpt.modules.depositmanagement.depositapproval.service.DepositApprovalService;

public class DepositApprovalTest extends BaseTest{

	@Autowired
	private DepositApprovalService depositApprovalService;
	@Test  
    @Transactional   //标明此方法需使用事务    
    @Rollback(false)  //标明使用完此方法后事务不回滚,true时为回滚   
    public void test(){
        System.out.println("测试Spring整合Junit4进行单元测试");  
        String id = "18e6fe837de14af68afcf8b30074ef68";
        DepositApproval depositApproval = depositApprovalService.get(id);
        System.out.println(depositApproval);
    }  
}
