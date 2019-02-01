package com.jfinalshop.controller.admin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Message;
import com.jfinalshop.Pageable;
import com.jfinalshop.model.Permission;
import com.jfinalshop.model.Role;
import com.jfinalshop.service.PermissionService;
import com.jfinalshop.service.RoleService;
import com.jfinalshop.template.method.MessageMethod;

import freemarker.template.TemplateModelException;

/**
 * Controller - 角色
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/role")
public class RoleController extends BaseController {

	private RoleService roleService = enhance(RoleService.class);
	private PermissionService permissionService = enhance(PermissionService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("modules", permissionService.getModules());
		setAttr("permissions", permissionService.findAll());
		render("/admin/role/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Role role = getModel(Role.class);
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		role.setIsEnabled(isEnabled);

		Long[] ids = getParaValuesToLong("permissions");
		List<Permission> permissions = permissionService.findList(ids);
		role.setPermissions(permissions);
		role.setIsSystem(false);
		role.setAdmins(null);
		roleService.save(role);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		
		setAttr("role", roleService.find(id));
		List<String> modules=permissionService.getModules();
		MessageMethod mm=new MessageMethod();
		for(String s:modules) {
		    try {
		    	List ls=new ArrayList<String>();
		    	ls.add("admin.role."+s);
				String ss=(String) mm.exec(ls );
				System.out.println("---"+ss);
			} catch (TemplateModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setAttr("modules", permissionService.getModules());
		setAttr("permissions", permissionService.findAll());
		render("/admin/role/edit.ftl");
		System.out.println();
	}

	/**
	 * 更新
	 */
	public void update() {
		Role role = getModel(Role.class);
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		role.setIsEnabled(isEnabled);
		
		Role pRole = roleService.find(role.getId());
		if (pRole == null || pRole.getIsSystem()) {
			redirect(ERROR_VIEW);
			return;
		}
		
		Long[] ids = getParaValuesToLong("permissions");
		List<Permission> permissions = permissionService.findList(ids);
		role.setPermissions(permissions);
		role.remove("is_system");
		roleService.update(role);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", roleService.findPage(pageable));
		render("/admin/role/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		if (ids != null) {
			for (Long id : ids) {
				Role role = roleService.find(id);
				if (role != null && (role.getIsSystem() || (role.getAdmins() != null && !role.getAdmins().isEmpty()))) {
					renderJson(Message.error("admin.role.deleteExistNotAllowed", role.getName()));
					return;
				}
			}
			roleService.delete(ids);
		}
		renderJson(SUCCESS_MESSAGE);
	}

}