package co.dc.ccpt.modules.coreperson.basicinfo.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.dc.ccpt.common.config.Global;
import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.CorePerson;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.PersonCertificate;
import co.dc.ccpt.modules.coreperson.basicinfo.service.CorePersonService;
import co.dc.ccpt.modules.sys.entity.DictValue;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.DictTypeService;
import co.dc.ccpt.modules.sys.service.SystemService;

/**
 * 人员Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/basicinformation")
public class CorePersonController extends BaseController {
	@Autowired
	private CorePersonService corePersonService;
	
	@Autowired
	private DictTypeService dictTypeService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@Autowired
	private SystemService userService;
	
	@ModelAttribute
	public CorePerson get(@RequestParam(required=false) String id) {
		CorePerson entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = corePersonService.get(id);
		}
		if (entity == null){
			entity = new CorePerson();
		}
		return entity;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return "modules/coreperson/basicinformation/corepersonList";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "getPersonCertificate")
	public Map<String, Object> getPersonCertificate(String corePersonId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(corePersonId == null || "".equals(corePersonId)){
			map.put("rows","[]");
			map.put("total",0);
		}else{
			List<PersonCertificate> list1 = corePersonService.get(corePersonId).getPersonCertificateList();
			//刷新后获取某个人员的证书：
			List<PersonCertificate> list2 = corePersonService.compareInvalidDateAndNowByList(list1);//设置证书状态
			map.put("rows",list2);
			map.put("total", list2.size());
		}
		return map;
	}
	
	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(CorePerson corePerson, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CorePerson> page = corePersonService.findPage(new Page<CorePerson>(request, response), corePerson); 
		return getBootstrapData(page);
	}

	
	/**
	 * 查看，增加，编辑管理表单页面
	 */
	@RequestMapping(value = "form")
	public String form(CorePerson corePerson, Model model) {
		if(StringUtils.isBlank(corePerson.getId())){//新增
			model.addAttribute("isAdd", true);
			model.addAttribute("corePerson", corePerson);
		}else{//修改
			model.addAttribute("edit", true);
			model.addAttribute("corePerson", corePerson);
		}
		return "modules/coreperson/basicinformation/corepersonForm";
	}

	@RequestMapping(value = "personCertificateForm")
	public String personCertificateForm(String personCertificateId, String corePersonId, Model model) {
		PersonCertificate personCertificate;
		if(personCertificateId == null || "".equals(personCertificateId)){
			personCertificate =  new PersonCertificate();
			model.addAttribute("isAdd", true);
		}else{
			personCertificate = corePersonService.getPersonCertificate(personCertificateId);
			model.addAttribute("edit", true);
		}
		
		personCertificate.setCorePerson(new CorePerson(corePersonId));
		model.addAttribute("personCertificate", personCertificate);
		return "modules/coreperson/basicinformation/personcertificateForm";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(CorePerson corePerson, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, corePerson)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		if (!"true".equals(checkIdentityNum(corePerson.getIdentityOldNum(), corePerson.getIdentityNum()))){
			j.setSuccess(false);
			j.setMsg( "保存人员'" + corePerson.getUser().getName()+ "'失败, 身份证号已存在");
			return j;
		}
		//新增或编辑表单保存
		corePersonService.save(corePerson);//保存
		j.setSuccess(true);
		j.setMsg("保存成功");
		return j;
		//保存前判断对应证书不可为空
//		List<StaffCertificate> staffCertificateList = new ArrayList<StaffCertificate>();
//		staffCertificateList = CorePerson.getStaffCertificateList();
//		if(StringUtils.isBlank(CorePerson.getId())){//新增
////			保存前判断人员不可重复
//			if(CorePersonService.getCorePersonByUserId(CorePerson)!=null){//如果人员重复不可新增
//				j.setSuccess(false);
//				j.setMsg("保存失败，人员重复！");
//				return j;
//			}
//		}
//		if(staffCertificateList!=null && staffCertificateList.size()>0){
//			CorePersonService.save(CorePerson);//保存
//			j.setSuccess(true);
//			j.setMsg("保存成功");
//			return j;
//		}else{
//			j.setSuccess(false);
//			j.setMsg("保存失败，请至少维护一个证书信息！");
//			return j;
//		}
	}
	
	@ResponseBody
	@RequestMapping(value = "savePersonCertificate")
	public AjaxJson savePersonCertificate(String personCertificateId, PersonCertificate personCertificate, Model model) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		if (!beanValidator(model, personCertificate)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//注册时间不能晚于失效时间和当前时间
		Date regisDate = personCertificate.getRegisDate();
		Date invalidDate = personCertificate.getInvalidDate();
		Date date = new Date();
		if(regisDate.after(date)){
			j.setSuccess(false);
			j.setMsg("注册时间不得晚于当前时间！");
			return j;
		}else if(regisDate.after(invalidDate)){
			j.setSuccess(false);
			j.setMsg("注册时间不得晚于失效时间！");
			return j;
		}
		//若为新增,失效时间不能早于当前时间
		if(StringUtils.isBlank(personCertificateId)){
			if(date.after(invalidDate)){
				j.setSuccess(false);
				j.setMsg("新增信息，失效时间不得早于当前时间！");
				return j;
			}
		}else{//修改时
			if(date.after(invalidDate)){//如果失效时间早于当前时间将状态设为失效
				Integer i = 1;
				personCertificate.setIsInvalid(i);
				personCertificate.setInvalidReason("证件过期");
			}else{//如果失效时间晚于当前时间将状态设为生效
				Integer i = 0;
				personCertificate.setIsInvalid(i);
			}
		}
		
		personCertificate.setId(personCertificateId);
		corePersonService.savePersonCertificate(personCertificate);
		j.setSuccess(true);
		j.setMsg("保存成功！");
		return j;
	}
	
	//变更状态
	@ResponseBody
	@RequestMapping(value = "change")
	public AjaxJson change(String personCertificateId, PersonCertificate personCertificate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, personCertificate)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//将状态做变更0--》1 or 1-->0
		personCertificate = corePersonService.getPersonCertificate(personCertificateId);
		Integer isInvalid = 0;
		if(personCertificate != null){
			String corePersonId = corePersonService.getCorePersonIdByCertificateId(personCertificate);//通过证书id查出一条对应的人员id
			CorePerson corePerson = corePersonService.get(corePersonId);
			if(corePerson != null){
				if(corePerson.getIsBuild().equals("1")){
					j.setSuccess(false);
					j.setMsg("保存失败！人员处于在建项目中不可切换证书状态！");
					return j;
				}else{
					isInvalid = personCertificate.getIsInvalid();
					if(isInvalid != null){
						if(isInvalid == 0){
							personCertificate.setIsInvalid(1);
						}else if(isInvalid == 1){
							personCertificate.setIsInvalid(0);
						}
						personCertificate.setId(personCertificateId);
						corePersonService.savePersonCertificate(personCertificate);
						j.setSuccess(true);
						j.setMsg("保存证书信息成功！");
						return j;
					}
				}
			}
		}
		return j;
	}
	
	/**
	 * 检验证件号是否重复
	 */
	@ResponseBody
	@RequestMapping(value = "checkIdentityNum")
	public String checkIdentityNum(String identityOldNum, String identityNum) {
		if (identityNum!=null && identityNum.equals(identityOldNum)) {
			return "true";
		} else if (identityNum!=null && corePersonService.getCorePersonByIdNum(identityNum) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 通过前台传回的证书名称字典值判断具体的字典类型id，而后查询出等级集合
	 * */
	@ResponseBody
	@RequestMapping(value="getDictValueListById",method=RequestMethod.POST)
	public List<DictValue> getDictValueListById(@RequestBody DictValue dictValue){
		List<DictValue> dictValueList = new ArrayList<DictValue>();
		dictValueList = dictTypeService.getDictValueListById(dictValue);
		return dictValueList;
	};
	
	@ResponseBody
	@RequestMapping(value = "detail")
	public CorePerson detail(String id) {
		return corePersonService.get(id);
	}
	
	/**
	 * 查询所有用户名称（模糊匹配）
	 */
	@ResponseBody
	@RequestMapping(value="getAllUserList")
	public List<User> getAllUserList (@RequestParam String name){
		User user = new User();
		user.setName(name);
		return userService.getAllUserList(user);
	}
	
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(CorePerson corePerson, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		corePersonService.delete(corePerson);
		j.setMsg("删除成功!");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "deletePersonCertificate")
	public AjaxJson deletePersonCertificate(String personCertificateId, Model model) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		corePersonService.deletePersonCertificate(new PersonCertificate(personCertificateId));
		enclosuretabService.deleteEnclosureByForeginId(personCertificateId);//同步删除对应附件
		j.setSuccess(true);
		j.setMsg("删除成功！");
		return j;
	}
	
	/**
	 * 批量删除投标管理
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		String idArray[] =ids.split(",");
		for(String id : idArray){
			//删除前判断：若已在人员在建项目信息中登记不可删
			CorePerson corePerson = corePersonService.get(id);
			String isBuild = "";
			if(corePerson!=null){
				isBuild = corePerson.getIsBuild();
				if(isBuild.equals("1")){//已经处于在建状态
					j.setSuccess(false);
					j.setMsg("当前人员存在在建项目不可删除！");
					return j;
				}else{
					corePersonService.delete(corePerson);
//					enclosuretabService.deleteEnclosureByForeginId(corePerson.getId());//同步删除对应附件
					j.setSuccess(true);
					j.setMsg("删除成功!");
					return j;
				}
			}
		}
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public List<CorePerson> listData(@RequestParam(required=false) String personName) {
		CorePerson corePerson = new CorePerson();
		User user = new User();
		user.setName(personName);
		corePerson.setUser(user);
		return corePersonService.findList(corePerson);
	}
    
}
