package org.openmrs.module.restrictbyuser.advice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.restrictbyuser.User;
import org.openmrs.module.restrictbyuser.utils.ModConstants;

public class UserRestrictionAdvisor implements MethodInterceptor {
	
	private User user;
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		String method = invocation.getMethod().getName();
		Object ret = invocation.proceed();

		if (Context.getAuthenticatedUser().isSuperUser() || 
				Context.getAuthenticatedUser().hasPrivilege(ModConstants.MANAGE_USER_RESTRICTIONS)) {
			return ret;
		} 
		
		this.user = (User) Context.getAuthenticatedUser();
		
		if (method.startsWith("getPatients") || method.startsWith("getAllPatients")) {
			if (ret == null) {
				return Collections.EMPTY_LIST;
			}
			
			@SuppressWarnings("unchecked")
			List<Patient> patientList = (List<Patient>) ret;
			List<Patient> userPatientList = new ArrayList<Patient>();
			for (Patient p: patientList) {
				if (user.getPatients().contains(p)) {
					userPatientList.add(p);
				}
			}
			return userPatientList;
		}
		
		if (method.equals("getPatient") || method.equals("getPatientByUuid")) {
			Patient patient = (Patient) ret;
			if (user.getPatients().contains(patient)) {
				return invocation.proceed();
			} else
				throw new APIException("Patient not found.");
		}
		
		return invocation.proceed();
		
	}
	

}
