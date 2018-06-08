/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.tools.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import co.dc.ccpt.common.config.Global;
import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.common.utils.FileUtils;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.security.SystemAuthorizingRealm.Principal;
import co.dc.ccpt.modules.sys.service.SystemService;
import co.dc.ccpt.modules.sys.utils.UserUtils;
import co.dc.ccpt.modules.tools.utils.TwoDimensionCode;

/**
 * 二维码Controller
 * @author dckj
 * @version 2015-11-30
 */
@Controller
@RequestMapping(value = "${adminPath}/tools/TwoDimensionCodeController")
public class TwoDimensionCodeController extends BaseController {

	@Autowired
	private SystemService systemService;
	/**
	 * 二维码页面
	 */
	@RequestMapping(value = {"index", ""})
	public String index() throws Exception{
		return "modules/tools/qrcode/TwoDimensionCode";
	}
	
	/**
	 *	生成二维码
	 * @param args
	 * @throws Exception
	 */
	@RequestMapping(value="createTwoDimensionCode")
	@ResponseBody
	public AjaxJson createTwoDimensionCode(HttpServletRequest request, String encoderContent){
		AjaxJson j = new AjaxJson();
		Principal principal = (Principal) UserUtils.getPrincipal();
		User user = UserUtils.getUser();
		if (principal == null){
			j.setSuccess(false);
			j.setErrorCode("0");
			j.setMsg("没有登录");
		}
		String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL
							+ principal + "/qrcode/";
		FileUtils.createDirectory(realPath);
		String name="test.png"; //encoderImgId此处二维码的图片名
			try {
				String filePath = realPath + name;  //存放路径
				TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");//执行生成二维码
				user.setQrCode(request.getContextPath()+Global.USERFILES_BASE_URL
						+ principal + "/qrcode/"+name);
				systemService.updateUserInfo(user);
				j.setSuccess(true);
				j.setMsg("二维码生成成功");
				j.put("filePath", request.getContextPath()+Global.USERFILES_BASE_URL
						+ principal + "/qrcode/"+name);
			} catch (Exception e) {
				
			}
		return j;
	}

}