/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.web.treetable;

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
import co.dc.ccpt.modules.test.entity.treetable.Car;
import co.dc.ccpt.modules.test.service.treetable.CarService;

/**
 * 车辆Controller
 * @author lgf
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/test/treetable/car")
public class CarController extends BaseController {

	@Autowired
	private CarService carService;
	
	@ModelAttribute
	public Car get(@RequestParam(required=false) String id) {
		Car entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = carService.get(id);
		}
		if (entity == null){
			entity = new Car();
		}
		return entity;
	}
	
	/**
	 * 车辆列表页面
	 */
	@RequiresPermissions("test:treetable:car:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/test/treetable/carList";
	}
	
		/**
	 * 车辆列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:treetable:car:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Car car, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Car> page = carService.findPage(new Page<Car>(request, response), car); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑车辆表单页面
	 */
	@RequiresPermissions(value={"test:treetable:car:view","test:treetable:car:add","test:treetable:car:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Car car, Model model) {
		model.addAttribute("car", car);
		return "modules/test/treetable/carForm";
	}

	/**
	 * 保存车辆
	 */
	@ResponseBody
	@RequiresPermissions(value={"test:treetable:car:add","test:treetable:car:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Car car, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, car)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		carService.save(car);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存车辆成功");
		return j;
	}
	
	/**
	 * 删除车辆
	 */
	@ResponseBody
	@RequiresPermissions("test:treetable:car:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Car car, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		carService.delete(car);
		j.setMsg("删除车辆成功");
		return j;
	}
	
	/**
	 * 批量删除车辆
	 */
	@ResponseBody
	@RequiresPermissions("test:treetable:car:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			carService.delete(carService.get(id));
		}
		j.setMsg("删除车辆成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:treetable:car:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Car car, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "车辆"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Car> page = carService.findPage(new Page<Car>(request, response, -1), car);
    		new ExportExcel("车辆", Car.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出车辆记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:treetable:car:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Car> list = ei.getDataList(Car.class);
			for (Car car : list){
				try{
					carService.save(car);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条车辆记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条车辆记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入车辆失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/treetable/car/?repage";
    }
	
	/**
	 * 下载导入车辆数据模板
	 */
	@RequiresPermissions("test:treetable:car:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "车辆数据导入模板.xlsx";
    		List<Car> list = Lists.newArrayList(); 
    		new ExportExcel("车辆数据", Car.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/treetable/car/?repage";
    }

}