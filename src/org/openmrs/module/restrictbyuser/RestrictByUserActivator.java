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
package org.openmrs.module.restrictbyuser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;

/**
 * This class contains the logic that is run every time this module
 * is either started or shutdown
 */
public class RestrictByUserActivator extends BaseModuleActivator {

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * @see org.openmrs.module.Activator#startup()
	 */
	public void startup() {
		log.info("Starting Restrict By User Module");
	}
	
	/**
	 *  @see org.openmrs.module.Activator#shutdown()
	 */
	public void shutdown() {
		log.info("Shutting down Restrict By User Module");
	}
	public void contextRefreshed() {
		log.info("contextRefreshed");
	}
	
	/**
	 * @see org.openmrs.module.ModuleActivator#started()
	 */
	public void started() {
		log.info("started");
	}
	
	/**
	 * @see org.openmrs.module.ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("stopped");
	}
	
	/**
	 * @see org.openmrs.module.ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("will refresh context");
	}
	
	/**
	 * @see org.openmrs.module.ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("will start");
	}
	
	/**
	 * @see org.openmrs.module.ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("will stop");
	}
	
}
