/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.depositapproval.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.depositmanagement.deposit.entity.Deposit;

import com.fasterxml.jackson.annotation.JsonFormat;



/**
 * 保证金审批Entity
 * @author lxh
 * @version 2018-04-20
 */
public class DepositApproval extends DataEntity<DepositApproval> {
	
	private static final long serialVersionUID = 1L;
	private String approvalNum;		// 审批编号
	private String approvaler;		// 审批人
	private Integer checkStatus;		// 审批状态
	private String depositId;		// 保证金id
	private String payWay;		// 缴纳方式
	private String receiver;		// 收款人
	private String receiverAccount;		// 收款账户
	private String receiverBank;		// 收款银行
	private String remittanceBank;		// 汇款银行
	private String remittanceAccount;		// 汇款账户
	private String operator;		// 经办人(签字)
	private String managingDirector;		// 分管负责人(签字)
	private String topManager;		// 总经理(审批)
	private String chairman;		// 董事长(审批)
	private String groupChairman;		// 集团董事长(审批)
	private Date statementDate;		// 出账日期
	private Date refundDate;		// 退还日期
	private Date beginStatementDate;		// 开始 出账日期
	private Date endStatementDate;		// 结束 出账日期
	private Date beginRefundDate;		// 开始 退还日期
	private Date endRefundDate;		// 结束 退还日期
	private Deposit deposit;        // 保证金对象
	private String checkClass;     // 审批等级
	private String depositType;    // 审批中的保证金类型；根据项目来匹配
	
	public DepositApproval() {
		super();
	}

	public DepositApproval(String id){
		super(id);
	}

	@ExcelField(title="审批编号", align=2, sort=6)
	public String getApprovalNum() {
		return approvalNum;
	}

	public void setApprovalNum(String approvalNum) {
		this.approvalNum = approvalNum;
	}
	
	@ExcelField(title="审批人", align=2, sort=7)
	public String getApprovaler() {
		return approvaler;
	}

	public void setApprovaler(String approvaler) {
		this.approvaler = approvaler;
	}
	
	public String getDepositType() {
		return depositType;
	}

	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}

	//	@NotNull(message="审批状态不能为空")
	@ExcelField(title="审批状态", dictType="", align=2, sort=8)
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	public String getCheckClass() {
		return checkClass;
	}

	public void setCheckClass(String checkClass) {
		this.checkClass = checkClass;
	}

	@ExcelField(title="保证金id", dictType="", align=2, sort=9)
	public String getDepositId() {
		return depositId;
	}

	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}
	
	@ExcelField(title="缴纳方式", dictType="", align=2, sort=10)
	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	
	@ExcelField(title="收款人", align=2, sort=11)
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	@ExcelField(title="收款账户", align=2, sort=12)
	public String getReceiverAccount() {
		return receiverAccount;
	}

	public void setReceiverAccount(String receiverAccount) {
		this.receiverAccount = receiverAccount;
	}
	
	@ExcelField(title="收款银行", align=2, sort=13)
	public String getReceiverBank() {
		return receiverBank;
	}

	public void setReceiverBank(String receiverBank) {
		this.receiverBank = receiverBank;
	}
	
	@ExcelField(title="汇款银行", align=2, sort=14)
	public String getRemittanceBank() {
		return remittanceBank;
	}

	public void setRemittanceBank(String remittanceBank) {
		this.remittanceBank = remittanceBank;
	}
	
	@ExcelField(title="汇款账户", align=2, sort=15)
	public String getRemittanceAccount() {
		return remittanceAccount;
	}

	public void setRemittanceAccount(String remittanceAccount) {
		this.remittanceAccount = remittanceAccount;
	}
	
	@ExcelField(title="经办人(签字)", align=2, sort=16)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@ExcelField(title="分管负责人(签字)", align=2, sort=17)
	public String getManagingDirector() {
		return managingDirector;
	}

	public void setManagingDirector(String managingDirector) {
		this.managingDirector = managingDirector;
	}
	
	@ExcelField(title="总经理(审批)", align=2, sort=18)
	public String getTopManager() {
		return topManager;
	}

	public void setTopManager(String topManager) {
		this.topManager = topManager;
	}
	
	@ExcelField(title="董事长(审批)", align=2, sort=19)
	public String getChairman() {
		return chairman;
	}

	public void setChairman(String chairman) {
		this.chairman = chairman;
	}
	
	@ExcelField(title="集团董事长(审批)", align=2, sort=20)
	public String getGroupChairman() {
		return groupChairman;
	}

	public void setGroupChairman(String groupChairman) {
		this.groupChairman = groupChairman;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="出账日期", align=2, sort=21)
	public Date getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(Date statementDate) {
		this.statementDate = statementDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="退还日期", align=2, sort=22)
	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}
	
	public Date getBeginStatementDate() {
		return beginStatementDate;
	}

	public void setBeginStatementDate(Date beginStatementDate) {
		this.beginStatementDate = beginStatementDate;
	}
	
	public Date getEndStatementDate() {
		return endStatementDate;
	}

	public void setEndStatementDate(Date endStatementDate) {
		this.endStatementDate = endStatementDate;
	}
		
	public Date getBeginRefundDate() {
		return beginRefundDate;
	}

	public void setBeginRefundDate(Date beginRefundDate) {
		this.beginRefundDate = beginRefundDate;
	}
	
	public Date getEndRefundDate() {
		return endRefundDate;
	}

	public void setEndRefundDate(Date endRefundDate) {
		this.endRefundDate = endRefundDate;
	}
		
}