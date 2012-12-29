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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyuser.User;
import org.openmrs.module.restrictbyuser.service.UserService;
import org.openmrs.module.restrictbyuser.utils.ModConstants;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * This class configured as controller using annotation and mapped with the URL
 * of 'module/restrictbyuser/restrictbyuserLink.form'.
 */
public class RestrictByUserFormController extends SimpleFormController {

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Allows for Integers to be used as values in input tags. Normally, only
	 * strings and lists are expected
	 * 
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.bind.ServletRequestDataBinder)
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
	}

	/**
	 * The onSubmit function receives the form/command object that was modified
	 * by the input form and saves it to the db
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object obj, BindException errors)
			throws Exception {

		HttpSession httpSession = request.getSession();
		MessageSourceAccessor msa = getMessageSourceAccessor();
		String userId = request.getParameter("userId");
		String view = getSuccessView();

		if (Context.isAuthenticated()
				&& Context.getAuthenticatedUser().hasPrivilege(
						ModConstants.MANAGE_USER_RESTRICTIONS)) {
			String patientId = ServletRequestUtils.getStringParameter(request,
					"patientId");

			/* Assign proxy privileges needed for the operations */
			try {
				Context.addProxyPrivilege(PrivilegeConstants.VIEW_USERS);
				Context.addProxyPrivilege(PrivilegeConstants.EDIT_USERS);

				if (StringUtils.hasText(userId)) {
					UserService us = Context.getService(UserService.class);
					PatientService ps = Context.getPatientService();
					String message = "";
					String success = "";
					String error = "";

					User u = (User) us.getUser(Integer.parseInt(userId));
					String remove = request.getParameter("remove");

					/* Remove patients in the list */
					if (remove != null) {
						String[] patientList = ServletRequestUtils
								.getStringParameters(request, "patientId");
						if (patientList.length > 0) {
							for (String p : patientList) {
								u.removePatient(ps.getPatient(Integer
										.parseInt(p)));
							}
							message = WebConstants.OPENMRS_MSG_ATTR;
							success = msa
									.getMessage("restrictbyuser.form.success.remove");
						} else {
							message = WebConstants.OPENMRS_ERROR_ATTR;
							error = msa
									.getMessage("restrictbyuser.form.error.noPatient");
						}
						/* If a valid patient is selected, add it to the object */
					} else if (patientId != "") {
						Patient p = ps.getPatient(Integer.parseInt(patientId));
						if (u.getPatients().contains(p)) {
							message = WebConstants.OPENMRS_ERROR_ATTR;
							error = msa
									.getMessage("restrictbyuser.form.error.duplicate");
						} else {
							u.addPatient(ps.getPatient(Integer
									.parseInt(patientId)));
							message = WebConstants.OPENMRS_MSG_ATTR;
							success = msa
									.getMessage("restrictbyuser.form.success.add");
						}
						/*
						 * If an invalide patient is selected, set display error
						 * message
						 */
					} else if (patientId == "") {
						error = msa
								.getMessage("restrictbyuser.form.error.invalidPatient");
					}

					/* Dispaly error message and save changes */
					if (message == WebConstants.OPENMRS_ERROR_ATTR) {
						httpSession.setAttribute(message, error);
					} else {
						u = (User) us.saveUser(u, null);
						httpSession.setAttribute(message, success);
					}
				} else {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
							msa.getMessage("restrictbyuser.form.error"));
				}
			} finally {
				Context.removeProxyPrivilege(PrivilegeConstants.VIEW_USERS);
				Context.removeProxyPrivilege(PrivilegeConstants.EDIT_USERS);
			}

		}

		return new ModelAndView(new RedirectView(view + "?userId="
				+ request.getParameter("userId")));
	}

	/**
	 * This class returns the form backing object. This can be a string, a
	 * boolean, or a normal java pojo. The bean name defined in the
	 * ModelAttribute annotation and the type can be just defined by the return
	 * type of this method
	 */
	protected User formBackingObject(HttpServletRequest request)
			throws Exception {

		String userId = request.getParameter("userId");
		UserService us = Context.getService(UserService.class);

		if (StringUtils.hasText(userId)
				&& Context.isAuthenticated()
				&& Context.getAuthenticatedUser().hasPrivilege(
						ModConstants.MANAGE_USER_RESTRICTIONS)) {
			try {
				Context.addProxyPrivilege(PrivilegeConstants.VIEW_USERS);

				return (User) us.getUser(Integer.parseInt(userId));
			} finally {
				Context.removeProxyPrivilege(PrivilegeConstants.VIEW_USERS);
			}
		}

		return null;
	}

	protected Map<String, Object> referenceData(HttpServletRequest request,
			Object object, Errors errors) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		Set<Role> roles;
		String userId = request.getParameter("userId");

		if (StringUtils.hasText(userId)
				&& Context.isAuthenticated()
				&& Context.getAuthenticatedUser().hasPrivilege(
						ModConstants.MANAGE_USER_RESTRICTIONS)) {
			try {
				Context.addProxyPrivilege(PrivilegeConstants.VIEW_USERS);
				roles = Context.getUserService()
						.getUser(Integer.parseInt(userId)).getRoles();
				map.put("roles", roles);
			} finally {
				Context.removeProxyPrivilege(PrivilegeConstants.VIEW_USERS);
			}
		}

		return map;
	}

}
