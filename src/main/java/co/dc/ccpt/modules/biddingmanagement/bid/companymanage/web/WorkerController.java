/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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

import co.dc.ccpt.common.config.Global;
import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.common.utils.DateUtils;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.common.utils.excel.ExportExcel;
import co.dc.ccpt.common.utils.excel.ImportExcel;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Worker;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service.WorkerService;

import com.google.common.collect.Lists;

/**
 * 人员信息Controller
 * @author lxh
 * @version 2018-02-08
 */
@Controller
@RequestMapping(value = "${adminPath}/companymanage/worker")
public class WorkerController extends BaseController {

	@Autowired
	private WorkerService workerService;
	
	@ModelAttribute
	public Worker get(@RequestParam(required=false) String id) {
		Worker entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workerService.get(id);
		}
		if (entity == null){
			entity = new Worker();
		}
		return entity;
	}
	
	/**
	 * 人员信息列表页面
	 */
	//@RequiresPermissions("companymanage:worker:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/biddingmanagement/bid/companymanage/workerList";
	}
	
		/**
	 * 人员信息列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("companymanage:worker:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Worker worker, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Worker> page = workerService.findPage(new Page<Worker>(request, response), worker); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑人员信息表单页面
	 */
	//@RequiresPermissions(value={"companymanage:worker:view","companymanage:worker:add","companymanage:worker:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Worker worker, Model model) {
		model.addAttribute("worker", worker);
		if(StringUtils.isBlank(worker.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/bid/companymanage/workerForm";
	}

	/**
	 * 保存人员信息
	 */
	//@RequiresPermissions(value={"companymanage:worker:add","companymanage:worker:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Worker worker, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, worker)){
			return form(worker, model);
		}
		//新增或编辑表单保存
		workerService.save(worker);//保存
		addMessage(redirectAttributes, "保存人员信息成功");
		return "redirect:"+Global.getAdminPath()+"/companymanage/worker/?repage";
	}
	
	/**
	 * 删除人员信息
	 */
	@ResponseBody
	//@RequiresPermissions("companymanage:worker:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Worker worker, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		workerService.delete(worker);
		j.setMsg("删除人员信息成功");
		return j;
	}
	
	/**
	 * 批量删除人员信息
	 */
	@ResponseBody
	//@RequiresPermissions("companymanage:worker:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			workerService.delete(workerService.get(id));
		}
		j.setMsg("删除人员信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	//@RequiresPermissions("companymanage:worker:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Worker worker, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "人员信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Worker> page = workerService.findPage(new Page<Worker>(request, response, -1), worker);
    		new ExportExcel("人员信息", Worker.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出人员信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	//@RequiresPermissions("companymanage:worker:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Worker> list = ei.getDataList(Worker.class);
			for (Worker worker : list){
				try{
					workerService.save(worker);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条人员信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条人员信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入人员信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/companymanage/worker/?repage";
    }
	
	/**
	 * 下载导入人员信息数据模板
	 */
    //@RequiresPermissions("companymanage:worker:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "人员信息数据导入模板.xlsx";
    		List<Worker> list = Lists.newArrayList(); 
    		new ExportExcel("人员信息数据", Worker.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/companymanage/worker/?repage";
    }

}