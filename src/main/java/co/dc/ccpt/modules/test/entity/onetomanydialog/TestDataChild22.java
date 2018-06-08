/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.entity.onetomanydialog;

import co.dc.ccpt.modules.sys.entity.Area;
import javax.validation.constraints.NotNull;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.common.utils.excel.annotation.ExcelField;

/**
 * 飞机票Entity
 * @author liugf
 * @version 2017-06-11
 */
public class TestDataChild22 extends DataEntity<TestDataChild22> {
	
	private static final long serialVersionUID = 1L;
	private Area startArea;		// 出发地
	private Area endArea;		// 目的地
	private String startTime;		// 出发时间
	private Double price;		// 代理价格
	private TestDataMainDialog testDataMain;		// 外键 父类
	
	public TestDataChild22() {
		super();
	}

	public TestDataChild22(String id){
		super(id);
	}

	public TestDataChild22(TestDataMainDialog testDataMain){
		this.testDataMain = testDataMain;
	}

	@NotNull(message="出发地不能为空")
	@ExcelField(title="出发地", fieldType=Area.class, value="startArea.name", align=2, sort=1)
	public Area getStartArea() {
		return startArea;
	}

	public void setStartArea(Area startArea) {
		this.startArea = startArea;
	}
	
	@NotNull(message="目的地不能为空")
	@ExcelField(title="目的地", fieldType=Area.class, value="endArea.name", align=2, sort=2)
	public Area getEndArea() {
		return endArea;
	}

	public void setEndArea(Area endArea) {
		this.endArea = endArea;
	}
	
	@ExcelField(title="出发时间", align=2, sort=3)
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	@NotNull(message="代理价格不能为空")
	@ExcelField(title="代理价格", align=2, sort=4)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public TestDataMainDialog getTestDataMain() {
		return testDataMain;
	}

	public void setTestDataMain(TestDataMainDialog testDataMain) {
		this.testDataMain = testDataMain;
	}
	
}