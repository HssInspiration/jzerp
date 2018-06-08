/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.depositreturn.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.modules.depositmanagement.depositstatement.entity.DepositStatement;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 保证金催退Entity
 * @author lxh
 * @version 2018-04-20
 */
public class DepositReturn extends DataEntity<DepositReturn> {
	
	private static final long serialVersionUID = 1L;
	private String returnNum;		// 催退编号
	private String depositId;		// 保证金id
	private Date returnDate;		// 催退时间
	private String returnName;		// 催退人名称
	private String returnNumber;	// 催退人号码
	private String returnCont;		// 催退详情描述
	private Date beginReturnDate;	// 开始 催退时间
	private Date endReturnDate;		// 结束 催退时间
	private DepositStatement depositStatement; //保证金出账对象
	private Integer isReturn;       // 是否退回
	
	public DepositReturn() {
		super();
	}

	public DepositReturn(String id){
		super(id);
	}

	@ExcelField(title="催退编号", align=2, sort=7)
	public String getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(String returnNum) {
		this.returnNum = returnNum;
	}
	
	public DepositStatement getDepositStatement() {
		return depositStatement;
	}

	public Integer getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}

	public void setDepositStatement(DepositStatement depositStatement) {
		this.depositStatement = depositStatement;
	}

	@ExcelField(title="保证金id", align=2, sort=8)
	public String getDepositId() {
		return depositId;
	}

	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@NotNull(message="催退时间不能为空")
	@ExcelField(title="催退时间", align=2, sort=9)
	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	@ExcelField(title="催退人名称", align=2, sort=10)
	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}
	
	@ExcelField(title="催退人号码", align=2, sort=11)
	public String getReturnNumber() {
		return returnNumber;
	}

	public void setReturnNumber(String returnNumber) {
		this.returnNumber = returnNumber;
	}
	
	@ExcelField(title="催退详情描述", align=2, sort=12)
	public String getReturnCont() {
		return returnCont;
	}

	public void setReturnCont(String returnCont) {
		this.returnCont = returnCont;
	}
	
	public Date getBeginReturnDate() {
		return beginReturnDate;
	}

	public void setBeginReturnDate(Date beginReturnDate) {
		this.beginReturnDate = beginReturnDate;
	}
	
	public Date getEndReturnDate() {
		return endReturnDate;
	}

	public void setEndReturnDate(Date endReturnDate) {
		this.endReturnDate = endReturnDate;
	}
		
}