/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.web.grid;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import co.dc.ccpt.common.utils.DateUtils;
import co.dc.ccpt.common.config.Global;
import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.common.utils.excel.ExportExcel;
import co.dc.ccpt.common.utils.excel.ImportExcel;
import co.dc.ccpt.modules.test.entity.grid.TestCountry;
import co.dc.ccpt.modules.test.service.grid.TestCountryService;

/**
 * 国家Controller
 * @author lgf
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/test/grid/testCountry")
public class TestCountryController extends BaseController {

	@Autowired
	private TestCountryService testCountryService;
	
	@ModelAttribute
	public TestCountry get(@RequestParam(required=false) String id) {
		TestCountry entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testCountryService.get(id);
		}
		if (entity == null){
			entity = new TestCountry();
		}
		return entity;
	}
	
	/**
	 * 国家列表页面
	 */
	@RequiresPermissions("test:grid:testCountry:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/test/grid/testCountryList";
	}
	
		/**
	 * 国家列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:grid:testCountry:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TestCountry testCountry, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TestCountry> page = testCountryService.findPage(new Page<TestCountry>(request, response), testCountry); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑国家表单页面
	 */
	@RequiresPermissions(value={"test:grid:testCountry:view","test:grid:testCountry:add","test:grid:testCountry:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TestCountry testCountry, Model model) {
		model.addAttribute("testCountry", testCountry);
		return "modules/test/grid/testCountryForm";
	}

	/**
	 * 保存国家
	 */
	@ResponseBody
	@RequiresPermissions(value={"test:grid:testCountry:add","test:grid:testCountry:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TestCountry testCountry, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, testCountry)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		testCountryService.save(testCountry);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存国家成功");
		return j;
	}
	
	/**
	 * 删除国家
	 */
	@ResponseBody
	@RequiresPermissions("test:grid:testCountry:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TestCountry testCountry, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		testCountryService.delete(testCountry);
		j.setMsg("删除国家成功");
		return j;
	}
	
	/**
	 * 批量删除国家
	 */
	@ResponseBody
	@RequiresPermissions("test:grid:testCountry:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testCountryService.delete(testCountryService.get(id));
		}
		j.setMsg("删除国家成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:grid:testCountry:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(TestCountry testCountry, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "国家"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestCountry> page = testCountryService.findPage(new Page<TestCountry>(request, response, -1), testCountry);
    		new ExportExcel("国家", TestCountry.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出国家记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:grid:testCountry:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestCountry> list = ei.getDataList(TestCountry.class);
			for (TestCountry testCountry : list){
				try{
					testCountryService.save(testCountry);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条国家记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条国家记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入国家失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/grid/testCountry/?repage";
    }
	
	/**
	 * 下载导入国家数据模板
	 */
	@RequiresPermissions("test:grid:testCountry:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "国家数据导入模板.xlsx";
    		List<TestCountry> list = Lists.newArrayList(); 
    		new ExportExcel("国家数据", TestCountry.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/grid/testCountry/?repage";
    }

}