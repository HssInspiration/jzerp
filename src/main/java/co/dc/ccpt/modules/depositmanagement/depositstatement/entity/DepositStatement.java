package co.dc.ccpt.modules.depositmanagement.depositstatement.entity;

import java.util.Date;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.depositmanagement.depositapproval.entity.DepositApproval;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 保证金出账记录Entity
 * @author lxh
 * @version 2018-04-20
 */
public class DepositStatement extends DataEntity<DepositStatement> {
	
	private static final long serialVersionUID = 1L;
	private String statementNum;		// 出账编号
	private String depositApprovalId;	// 保证金审批id
	private String depositTabId;		// 保证金申请id
	private Date statementDate;			// 汇票时间
	private String ticketHolder;		// 领票人
	private Date beginStatementDate;	// 开始 汇票时间
	private Date endStatementDate;	    // 结束 汇票时间
	private DepositApproval depositApproval;       // 保证金审批
	
	public DepositStatement() {
		super();
	}

	public DepositStatement(String id){
		super(id);
	}

	@ExcelField(title="出账编号", align=2, sort=6)
	public String getStatementNum() {
		return statementNum;
	}

	public void setStatementNum(String statementNum) {
		this.statementNum = statementNum;
	}
	
	@ExcelField(title="保证金审批id", align=2, sort=7)
	public String getDepositApprovalId() {
		return depositApprovalId;
	}


	public DepositApproval getDepositApproval() {
		return depositApproval;
	}

	public void setDepositApproval(DepositApproval depositApproval) {
		this.depositApproval = depositApproval;
	}


	public void setDepositApprovalId(String depositApprovalId) {
		this.depositApprovalId = depositApprovalId;
	}
	
	@ExcelField(title="保证金申请id", align=2, sort=8)
	public String getDepositTabId() {
		return depositTabId;
	}

	public void setDepositTabId(String depositTabId) {
		this.depositTabId = depositTabId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStatementDate() {
		return statementDate;
	}

	public void setStatementDate(Date statementDate) {
		this.statementDate = statementDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginStatementDate() {
		return beginStatementDate;
	}

	public void setBeginStatementDate(Date beginStatementDate) {
		this.beginStatementDate = beginStatementDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndStatementDate() {
		return endStatementDate;
	}

	public void setEndStatementDate(Date endStatementDate) {
		this.endStatementDate = endStatementDate;
	}

	@ExcelField(title="领票人", align=2, sort=10)
	public String getTicketHolder() {
		return ticketHolder;
	}

	public void setTicketHolder(String ticketHolder) {
		this.ticketHolder = ticketHolder;
	}

}