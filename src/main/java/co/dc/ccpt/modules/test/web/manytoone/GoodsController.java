/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.web.manytoone;

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
import co.dc.ccpt.modules.test.entity.manytoone.Goods;
import co.dc.ccpt.modules.test.service.manytoone.GoodsService;

/**
 * 商品Controller
 * @author liugf
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/test/manytoone/goods")
public class GoodsController extends BaseController {

	@Autowired
	private GoodsService goodsService;
	
	@ModelAttribute
	public Goods get(@RequestParam(required=false) String id) {
		Goods entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = goodsService.get(id);
		}
		if (entity == null){
			entity = new Goods();
		}
		return entity;
	}
	
	/**
	 * 商品列表页面
	 */
	@RequiresPermissions("test:manytoone:goods:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/test/manytoone/goodsList";
	}
	
		/**
	 * 商品列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:manytoone:goods:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Goods goods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Goods> page = goodsService.findPage(new Page<Goods>(request, response), goods); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商品表单页面
	 */
	@RequiresPermissions(value={"test:manytoone:goods:view","test:manytoone:goods:add","test:manytoone:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Goods goods, Model model) {
		model.addAttribute("goods", goods);
		return "modules/test/manytoone/goodsForm";
	}

	/**
	 * 保存商品
	 */
	@ResponseBody
	@RequiresPermissions(value={"test:manytoone:goods:add","test:manytoone:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Goods goods, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, goods)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		goodsService.save(goods);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存商品成功");
		return j;
	}
	
	/**
	 * 删除商品
	 */
	@ResponseBody
	@RequiresPermissions("test:manytoone:goods:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Goods goods, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		goodsService.delete(goods);
		j.setMsg("删除商品成功");
		return j;
	}
	
	/**
	 * 批量删除商品
	 */
	@ResponseBody
	@RequiresPermissions("test:manytoone:goods:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			goodsService.delete(goodsService.get(id));
		}
		j.setMsg("删除商品成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:manytoone:goods:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Goods goods, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Goods> page = goodsService.findPage(new Page<Goods>(request, response, -1), goods);
    		new ExportExcel("商品", Goods.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商品记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:manytoone:goods:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Goods> list = ei.getDataList(Goods.class);
			for (Goods goods : list){
				try{
					goodsService.save(goods);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商品记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条商品记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入商品失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/manytoone/goods/?repage";
    }
	
	/**
	 * 下载导入商品数据模板
	 */
	@RequiresPermissions("test:manytoone:goods:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "商品数据导入模板.xlsx";
    		List<Goods> list = Lists.newArrayList(); 
    		new ExportExcel("商品数据", Goods.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/manytoone/goods/?repage";
    }

}