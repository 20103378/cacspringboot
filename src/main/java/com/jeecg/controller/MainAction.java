package com.jeecg.controller;

import com.alibaba.fastjson.JSON;
import com.base.annotation.Auth;
import com.base.entity.BaseEntity.DELETED;
import com.base.entity.TreeNode;
import com.base.entity.vo.RequestUserVO;
import com.base.util.Constant.SuperAdmin;
import com.base.util.*;
import com.base.web.BaseAction;
import com.jeecg.entity.SysMenu;
import com.jeecg.entity.SysMenuBtn;
import com.jeecg.entity.SysUser;
import com.jeecg.service.SysMenuBtnService;
import com.jeecg.service.SysMenuService;
import com.jeecg.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class MainAction extends BaseAction {
	private final static Logger logger = LoggerFactory.getLogger(MainAction.class);

	@Autowired(required = false)
	private SysMenuService<SysMenu> sysMenuService;

	@Autowired(required = false)
	private SysUserService<SysUser> sysUserService;

	@Autowired(required = false)
	private SysMenuBtnService<SysMenuBtn> sysMenuBtnService;

	/**
	 * 登录页面 1.
	 * @return
	 */
	@Auth(verifyLogin = false, verifyURL = false)
	@RequestMapping("/")
	public String login(){
		logger.debug("记录debug日志");
		logger.info("访问了index方法");
		logger.error("记录了error错误日志");
		return "login";
	}

	/**
	 * 检查用户名称2.-->main
	 * 
	 * @param user
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Auth(verifyLogin = false, verifyURL = false)
	@RequestMapping("/checkuser")
	@ResponseBody
	public Boolean checkuser(RequestUserVO user, HttpServletRequest req) throws Exception {
		SysUser u = sysUserService.queryLogin(user.getEmail(),
				MethodUtil.MD5(user.getPwd()));
		if (u != null) {
			// -------------------------------------------------------
			// 登录次数加1 修改登录时间
			int loginCount = 0;
			if (u.getLoginCount() != null) {
				loginCount = u.getLoginCount();
			}
			u.setLoginCount(loginCount + 1);
			u.setLoginTime(DateUtil.getDateByString(""));
			sysUserService.update(u);
			// 设置User到Session
			SessionUtils.setUser(req, u);
			// 记录成功登录日志
			return true;
			// -------------------------------------------------------
		} else {
			return false;
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Auth(verifyLogin = false, verifyURL = false)
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		SessionUtils.removeUser(request);
		return "redirect:/";
	}

	/**
	 * 获取Action下的按钮
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Auth(verifyURL = false)
	@RequestMapping("/getActionBtn")
	public void getActionBtn(String url, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> actionTypes = new ArrayList<String>();
		// 判断是否超级管理员
		if (SessionUtils.isAdmin(request)) {
			result.put("allType", true);
		} else {
			String menuUrl = URLUtils.getReqUri(url);
			menuUrl = StringUtils.remove(menuUrl, request.getContextPath());
			// 获取权限按钮
			actionTypes = SessionUtils.getMemuBtnListVal(request,
					StringUtils.trim(menuUrl));
			result.put("allType", false);
			result.put("types", actionTypes);
		}
		result.put(SUCCESS, true);
		HtmlUtil.writerJson(response, result);
	}

	/**
	 * 修改密码
	 * 
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception
	 */
	@Auth(verifyURL = false)
	@RequestMapping("/modifyPwd")
	public void modifyPwd(String oldPwd, String newPwd,
                          HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SysUser user = SessionUtils.getUser(request);
		if (user == null) {
			sendFailureMessage(response, "对不起,登录超时.");
			return;
		}
		SysUser bean = sysUserService.queryById(user.getId());
		if (bean.getId() == null || DELETED.YES.key == bean.getDeleted()) {
			sendFailureMessage(response, "对不起,用户不存在.");
			return;
		}
		if (StringUtils.isBlank(newPwd)) {
			sendFailureMessage(response, "密码不能为空.");
			return;
		}
		// 不是超级管理员，匹配旧密码
		if (!MethodUtil.ecompareMD5(oldPwd, bean.getPwd())) {
			sendFailureMessage(response, "旧密码输入不匹配.");
			return;
		}
		bean.setPwd(MethodUtil.MD5(newPwd));
		sysUserService.update(bean);
		sendSuccessMessage(response, "Save success.");
	}

	/**
	 * ilook 首页
	 * 
	 * @param url
	 * @param classifyId
	 * @return
	 */
	@Auth(verifyURL = false)
	@RequestMapping("/main") //3.
	public String main(HttpServletRequest request, Map<String, Object> map) {
		SysUser user = SessionUtils.getUser(request);
		List<SysMenu> rootMenus = null;
		List<SysMenu> childMenus = null;
		List<SysMenuBtn> childBtns = null;
		// 超级管理员
		if (user != null && SuperAdmin.YES.key == user.getSuperAdmin()) {
			rootMenus = sysMenuService.getRootMenu(null);// 查询所有根节点
			childMenus = sysMenuService.getChildMenu();// 查询所有子节点
		} else {
			rootMenus = sysMenuService.getRootMenuByUser(user.getId());// 根节点
			childMenus = sysMenuService.getChildMenuByUser(user.getId());// 子节点
			childBtns = sysMenuBtnService.getMenuBtnByUser(user.getId());// 按钮操作
			buildData(childMenus, childBtns, request); // 构建必要的数据
		}
		map.put("user", user);
		map.put("menuList", treeMenu(rootMenus, childMenus));
		return "main/main";
	}
	/**
	 * 构建树形数据
	 * 
	 * @return
	 */
	private List<TreeNode> treeMenu(List<SysMenu> rootMenus,
                                    List<SysMenu> childMenus) {
		TreeUtil util = new TreeUtil(rootMenus, childMenus);
		System.out.println("--------"+ JSON.toJSONString(util.getTreeNode()));
		return util.getTreeNode();
	}

	/**
	 * 构建树形数据
	 * 
	 * @return
	 */
	private void buildData(List<SysMenu> childMenus,
                           List<SysMenuBtn> childBtns, HttpServletRequest request) {
		// 能够访问的url列表
		List<String> accessUrls = new ArrayList<String>();
		// 菜单对应的按钮
		Map<String, List> menuBtnMap = new HashMap<String, List>();
		for (SysMenu menu : childMenus) {
			// 判断URL是否为空
			if (StringUtils.isNotBlank(menu.getUrl())) {
				List<String> btnTypes = new ArrayList<String>();
				for (SysMenuBtn btn : childBtns) {
					if (menu.getId().equals(btn.getMenuid())) {
						btnTypes.add(btn.getBtnType());
						URLUtils.getBtnAccessUrls(menu.getUrl(),
								btn.getActionUrls(), accessUrls);
					}
				}
				menuBtnMap.put(menu.getUrl(), btnTypes);
				URLUtils.getBtnAccessUrls(menu.getUrl(), menu.getActions(),
						accessUrls);
				accessUrls.add(menu.getUrl());
			}
		}
		SessionUtils.setAccessUrl(request, accessUrls);// 设置可访问的URL
		SessionUtils.setMemuBtnMap(request, menuBtnMap); // 设置可用的按钮
	}
}
