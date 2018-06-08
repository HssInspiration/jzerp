/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.entity.grid;


import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.common.utils.excel.annotation.ExcelField;

/**
 * 洲Entity
 * @author lgf
 * @version 2017-06-12
 */
public class TestContinent extends DataEntity<TestContinent> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 洲名
	
	public TestContinent() {
		super();
	}

	public TestContinent(String id){
		super(id);
	}

	@ExcelField(title="洲名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}