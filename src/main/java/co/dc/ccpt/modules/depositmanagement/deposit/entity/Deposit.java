/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.deposit.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;



/**
 * 保证金信息管理Entity
 * @author lxh
 * @version 2018-04-11
 */
public class Deposit extends DataEntity<Deposit> {
	
	private static final long serialVersionUID = 1L;
	private String depositNum;		// 保证金编号
	private String depositName;     // 保证金名称
	private String programId;		// 项目id
	private String depositType;		// 保证金类型
	private Integer checkStatus;		// 审批状态
	private Integer isDraw;		        // 是否汇领
	private Integer isReturn;		    // 是否催退
	private String payWay;		        // 缴纳方式
	private String receiver;		    // 收款人名称
	private String receiverAccount;		// 收款人账号
	private String remittanceBank;		// 汇款银行
	private String receiverBank;		// 收款银行
	private Program program;        // 项目对象
	private Double payCount;        // 缴纳金额
	private String applyer;         // 申请人
	private Date applyDate;         // 申请时间 
	private Date beginApplyDate;	// 开始 申请时间 
	private Date endApplyDate;		// 结束 申请时间 
	
	public Deposit() {
		super();
	}

	public Deposit(String id){
		super(id);
	}

	@ExcelField(title="保证金编号", align=2, sort=7)
	public String getDepositNum() {
		return depositNum;
	}

	public void setDepositNum(String depositNum) {
		this.depositNum = depositNum;
	}
	
	public Program getProgram() {
		return program;
	}

	public String getApplyer() {
		return applyer;
	}

	public void setApplyer(String applyer) {
		this.applyer = applyer;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	@ExcelField(title="项目id", dictType="", align=2, sort=8)
	public String getProgramId() {
		return programId;
	}

	public String getDepositName() {
		return depositName;
	}

	public void setDepositName(String depositName) {
		this.depositName = depositName;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}
	
	@ExcelField(title="保证金类型", align=2, sort=9)
	public String getDepositType() {
		return depositType;
	}

	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}
	
	public Double getPayCount() {
		return payCount;
	}

	public void setPayCount(Double payCount) {
		this.payCount = payCount;
	}

//	@NotNull(message="审批状态不能为空")
	@ExcelField(title="审批状态", dictType="", align=2, sort=10)
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	
//	@NotNull(message="是否汇领不能为空")
	@ExcelField(title="是否汇领", dictType="", align=2, sort=11)
	public Integer getIsDraw() {
		return isDraw;
	}

	public void setIsDraw(Integer isDraw) {
		this.isDraw = isDraw;
	}
	
//	@NotNull(message="是否催退不能为空")
	@ExcelField(title="是否催退", dictType="", align=2, sort=12)
	public Integer getIsReturn() {
		return isReturn;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginApplyDate() {
		return beginApplyDate;
	}
	
	public void setBeginApplyDate(Date beginApplyDate) {
		this.beginApplyDate = beginApplyDate;
	}

	public Date getEndApplyDate() {
		return endApplyDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public void setEndApplyDate(Date endApplyDate) {
		this.endApplyDate = endApplyDate;
	}

	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}
	
	@ExcelField(title="缴纳方式", dictType="", align=2, sort=13)
	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	
	@ExcelField(title="收款人名称", align=2, sort=14)
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	@ExcelField(title="收款人账号", align=2, sort=15)
	public String getReceiverAccount() {
		return receiverAccount;
	}

	public void setReceiverAccount(String receiverAccount) {
		this.receiverAccount = receiverAccount;
	}
	
	@ExcelField(title="汇款银行", align=2, sort=16)
	public String getRemittanceBank() {
		return remittanceBank;
	}

	public void setRemittanceBank(String remittanceBank) {
		this.remittanceBank = remittanceBank;
	}
	
	@ExcelField(title="收款银行", align=2, sort=17)
	public String getReceiverBank() {
		return receiverBank;
	}

	public void setReceiverBank(String receiverBank) {
		this.receiverBank = receiverBank;
	}
	
}