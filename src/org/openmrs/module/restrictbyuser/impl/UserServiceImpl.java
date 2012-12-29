package org.openmrs.module.restrictbyuser.impl;

import org.openmrs.module.restrictbyuser.db.UserDAO;
import org.openmrs.module.restrictbyuser.service.UserService;

public class UserServiceImpl extends org.openmrs.api.impl.UserServiceImpl implements UserService {
	private UserDAO dao;

	/** Default constructor. */
	public UserServiceImpl() {
	}
	
	public void setUserDAO(org.openmrs.module.restrictbyuser.db.UserDAO dao)  { 
		this.dao = dao; 
		super.setUserDAO(this.dao); 
	} 
	
	
}
