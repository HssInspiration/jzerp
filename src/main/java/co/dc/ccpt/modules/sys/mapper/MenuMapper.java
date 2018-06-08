/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.sys.entity.Menu;

/**
 * 菜单MAPPER接口
 * @author dckj
 * @version 2017-05-16
 */
@MyBatisMapper
public interface MenuMapper extends BaseMapper<Menu> {

	public List<Menu> findByParentIdsLike(Menu menu);

	public List<Menu> findByUserId(Menu menu);
	
	public int updateParentIds(Menu menu);
	
	public int updateSort(Menu menu);
	
	public List<Menu> getChildren(String parentId);
	
	public void deleteMenuRole(@Param(value="menu_id")String menu_id);
	
	public void deleteMenuDataRule(@Param(value="menu_id")String menu_id);
	
	public List<Menu> findAllDataRuleList(Menu menu);


	
}
