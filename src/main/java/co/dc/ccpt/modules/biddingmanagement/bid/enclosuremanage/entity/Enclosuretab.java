package co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.entity;

import javax.validation.constraints.NotNull;
import co.dc.ccpt.common.utils.excel.annotation.ExcelField;
import co.dc.ccpt.core.persistence.DataEntity;



/**
 * 附件信息管理Entity
 * @author lxh
 * @version 2018-03-25
 */
public class Enclosuretab extends DataEntity<Enclosuretab> {
	
	private static final long serialVersionUID = 1L;
	private String enclosureNum;		// 附件编号
	private String enclosureOldNum;		// 附件编号
	private Integer enclosureType;		// 附件类型
	private String enclosureCont;		// 附件内容
	private String foreginId;		// 主体id
	
	public Enclosuretab() {
		super();
	}

	public Enclosuretab(String id){
		super(id);
	}

	public String getEnclosureOldNum() {
		return enclosureOldNum;
	}

	public void setEnclosureOldNum(String enclosureOldNum) {
		this.enclosureOldNum = enclosureOldNum;
	}

	@ExcelField(title="附件编号", align=2, sort=7)
	public String getEnclosureNum() {
		return enclosureNum;
	}

	public void setEnclosureNum(String enclosureNum) {
		this.enclosureNum = enclosureNum;
	}
	
	@NotNull(message="附件类型不能为空")
	@ExcelField(title="附件类型", align=2, sort=8)
	public Integer getEnclosureType() {
		return enclosureType;
	}

	public void setEnclosureType(Integer enclosureType) {
		this.enclosureType = enclosureType;
	}
	
	@ExcelField(title="附件内容", align=2, sort=9)
	public String getEnclosureCont() {
		return enclosureCont;
	}

	public void setEnclosureCont(String enclosureCont) {
		this.enclosureCont = enclosureCont;
	}
	
	@ExcelField(title="主体id", align=2, sort=10)
	public String getForeginId() {
		return foreginId;
	}

	public void setForeginId(String foreginId) {
		this.foreginId = foreginId;
	}
	
}