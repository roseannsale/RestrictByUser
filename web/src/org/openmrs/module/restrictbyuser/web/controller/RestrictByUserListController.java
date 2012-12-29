/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.restrictbyuser.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyuser.utils.ModConstants;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.web.WebConstants;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * This class configured as controller using annotation and mapped with the URL
 * of 'module/restrictbyuser/restrictbyuserLink.form'.
 */
public class RestrictByUserListController extends SimpleFormController{

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Allows for Integers to be used as values in input tags. Normally, only strings and lists are
	 * expected
	 * 
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.bind.ServletRequestDataBinder)
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
	}

	/**
	 * The onSubmit function receives the form/command object that was modified by the input form
	 * and saves it to the db
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	        BindException errors) throws Exception {
		
		HttpSession httpSession = request.getSession();
		String userId = request.getParameter("userId");
		String view = getSuccessView();
		
//		if (Context.isAuthenticated() && Context.getAuthenticatedUser()
//				.hasPrivilege(ModConstants.MANAGE_USER_RESTRICTIONS)) {
//			
//			/* Assign proxy privileges needed for the operations */
//			try {
//				Context.addProxyPrivilege(PrivilegeConstants.VIEW_USERS);
//				Context.addProxyPrivilege(PrivilegeConstants.EDIT_USERS);
//				
//				if (StringUtils.hasText(userId)) {
//					
//				} else {
//					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Error.");
//				}	
//			} finally {
//				Context.removeProxyPrivilege(PrivilegeConstants.VIEW_USERS);
//				Context.removeProxyPrivilege(PrivilegeConstants.EDIT_USERS);
//			}
//			
//		}
		
		return new ModelAndView(new RedirectView(view));
	}

	/**
	 * This class returns the form backing object. This can be a string, a
	 * boolean, or a normal java pojo. The bean name defined in the
	 * ModelAttribute annotation and the type can be just defined by the return
	 * type of this method
	 */
	protected List<User> formBackingObject(HttpServletRequest request)
			throws Exception {

		UserService us = Context.getService(UserService.class);
		
//		if (Context.isAuthenticated() && Context.getAuthenticatedUser().hasPrivilege(ModConstants.MANAGE_USER_RESTRICTIONS)) {
//		}
		
		return new ArrayList<User>();
	}

	protected Map<String, Object> referenceData(HttpServletRequest request,
			Object object, Errors errors) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String action = request.getParameter("action");
		String name = request.getParameter("name");
		String roleStr = request.getParameter("role");
		String includeDisabledStr = request.getParameter("includeDisabled");
		

		map.put("name", name);
		map.put("action", action);
		
		if (Context.isAuthenticated() && Context.getAuthenticatedUser().hasPrivilege(ModConstants.MANAGE_USER_RESTRICTIONS)) {
			try {
				Context.addProxyPrivilege(PrivilegeConstants.VIEW_USERS);
				Role role = null;
				Boolean includeDisabled = null;
				if(roleStr != null) {
					role = Context.getUserService().getRole(roleStr);
					map.put("role", role);
				}
				if(includeDisabledStr != null) {
					includeDisabled = Boolean.parseBoolean(includeDisabledStr);
					map.put("includeDisabled", includeDisabled);
				}
				List<User> users = getUsers(action, name, role, includeDisabled);
				map.put("users", users);
			} finally {
				Context.removeProxyPrivilege(PrivilegeConstants.VIEW_USERS);
			}
		}
		
		return map;
	}
	
	protected List<org.openmrs.User> getUsers(String action, String name, Role role, Boolean includeDisabled) {
		// only do the search if there are search parameters or 
		if (action != null || StringUtils.hasText(name) || role != null) {
			if (includeDisabled == null)
				includeDisabled = false;
			List<Role> roles = null;
			if (role != null && StringUtils.hasText(role.getRole()))
				roles = Collections.singletonList(role);
			
			if (!StringUtils.hasText(name))
				name = null;
			
			return Context.getUserService().getUsers(name, roles, includeDisabled);
		}
		
		return new ArrayList<User>();
		
	}

}
