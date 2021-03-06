/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.TreeMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.sys.entity.Office;

/**
 * 机构MAPPER接口
 * 
 * @author dckj
 * @version 2017-05-16
 */
@MyBatisMapper
public interface OfficeMapper extends TreeMapper<Office> {
	
	public Office getByCode(String code);
	
	List<Office> listAllOfficeByName(@Param("name") String name);
	
	/**
	 * 通过当前机构Id获取父级机构Id
	 */
	public String getParentId(@Param("officeId") String officeId);
	
	/**
	 * 通过officeId(此时为parentId)获取分公司负责人id
	 * @param parentId
	 */
	public String getPrimaryPersonId(@Param("parentId") String parentId);
	/**
	 * 通过officeId(此时为parentId)获取分公司技术负责人id
	 * @param parentId
	 */
	public String getTecPersonId(@Param("parentId") String parentId);
	/**
	 * 通过officeId(此时为parentId)获取分公司财务负责人id
	 * @param parentId
	 */
	public String getAccPersonId(@Param("parentId") String parentId);

	public String getByName(@Param("officeName") String officeName);
	
}
