package co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.web;


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
import co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.entity.OpenBid;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.openbid.service.OpenBidService;

import com.google.common.collect.Lists;


/**
 * 开标信息管理Controller
 * @author lxh
 * @version 2018-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/tendermanage/openBid")
public class OpenBidController extends BaseController {

	@Autowired
	private OpenBidService openBidService;
	
	@ModelAttribute
	public OpenBid get(@RequestParam(required=false) String id) {
		OpenBid entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = openBidService.get(id);
		}
		if (entity == null){
			entity = new OpenBid();
		}
		return entity;
	}
	
	/**
	 * 开标信息管理列表页面
	 */
//	@RequiresPermissions("openbid:openBid:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/biddingmanagement/tendermanage/openbid/openBidList";
	}
	
		/**
	 * 开标信息管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("openbid:openBid:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(OpenBid openBid, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OpenBid> page = openBidService.findPage(new Page<OpenBid>(request, response), openBid); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑开标信息管理表单页面
	 */
//	@RequiresPermissions(value={"openbid:openBid:view","openbid:openBid:add","openbid:openBid:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OpenBid openBid, Model model) {
		model.addAttribute("openBid", openBid);
		return "modules/biddingmanagement/tendermanage/openbid/openBidForm";
	}

	/**
	 * 保存开标信息管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"openbid:openBid:add","openbid:openBid:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(OpenBid openBid, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, openBid)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		openBidService.save(openBid);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存开标信息管理成功");
		return j;
	}
	
	/**
	 * 删除开标信息管理
	 */
	@ResponseBody
//	@RequiresPermissions("openbid:openBid:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(OpenBid openBid, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		openBidService.delete(openBid);
		j.setMsg("删除开标信息管理成功");
		return j;
	}
	
	/**
	 * 批量删除开标信息管理
	 */
	@ResponseBody
//	@RequiresPermissions("openbid:openBid:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			openBidService.delete(openBidService.get(id));
		}
		j.setMsg("删除开标信息管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("openbid:openBid:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(OpenBid openBid, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "开标信息管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OpenBid> page = openBidService.findPage(new Page<OpenBid>(request, response, -1), openBid);
    		new ExportExcel("开标信息管理", OpenBid.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出开标信息管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("openbid:openBid:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OpenBid> list = ei.getDataList(OpenBid.class);
			for (OpenBid openBid : list){
				try{
					openBidService.save(openBid);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条开标信息管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条开标信息管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入开标信息管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tendermanage/openBid/?repage";
    }
	
	/**
	 * 下载导入开标信息管理数据模板
	 */
//	@RequiresPermissions("openbid:openBid:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "开标信息管理数据导入模板.xlsx";
    		List<OpenBid> list = Lists.newArrayList(); 
    		new ExportExcel("开标信息管理数据", OpenBid.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tendermanage/openBid/?repage";
    }

}