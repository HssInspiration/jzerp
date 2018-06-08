/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.entity.grid;

import co.dc.ccpt.modules.test.entity.grid.TestContinent;

import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.common.utils.excel.annotation.ExcelField;

/**
 * 国家Entity
 * @author lgf
 * @version 2017-06-12
 */
public class TestCountry extends DataEntity<TestCountry> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 国名
	private String sum;		// 人口
	private TestContinent continent;		// 所属洲
	
	public TestCountry() {
		super();
	}

	public TestCountry(String id){
		super(id);
	}

	@ExcelField(title="国名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="人口", align=2, sort=2)
	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}
	
	@ExcelField(title="所属洲", fieldType=TestContinent.class, value="continent.name", align=2, sort=3)
	public TestContinent getContinent() {
		return continent;
	}

	public void setContinent(TestContinent continent) {
		this.continent = continent;
	}
	
}