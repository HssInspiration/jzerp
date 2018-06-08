package co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.entity;

import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;



/**
 * 开标信息管理Entity
 * @author lxh
 * @version 2018-03-27
 */
public class OpenBid extends DataEntity<OpenBid> {
	
	private static final long serialVersionUID = 1L;
	private String tenderId;		// 招标信息表id
	private String openBidNum;		// 开标编号
	private String subpackageProgramId;		// 子项目工程id
	
	public OpenBid() {
		super();
	}

	public OpenBid(String id){
		super(id);
	}

	@ExcelField(title="招标信息表id", align=2, sort=7)
	public String getTenderId() {
		return tenderId;
	}

	public void setTenderId(String tenderId) {
		this.tenderId = tenderId;
	}
	
	@ExcelField(title="开标编号", align=2, sort=8)
	public String getOpenBidNum() {
		return openBidNum;
	}

	public void setOpenBidNum(String openBidNum) {
		this.openBidNum = openBidNum;
	}
	
	@ExcelField(title="子项目工程id", align=2, sort=9)
	public String getSubpackageProgramId() {
		return subpackageProgramId;
	}

	public void setSubpackageProgramId(String subpackageProgramId) {
		this.subpackageProgramId = subpackageProgramId;
	}
	
}