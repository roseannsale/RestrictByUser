package org.openmrs.module.restrictbyuser;

import java.util.HashSet;
import java.util.Set;

import org.openmrs.Patient;

public class User extends org.openmrs.User {

	private static final long serialVersionUID = 1L;
	private Set<Patient> patients;

	/** Default constructor. */

	public User() {
	}

	public User(Integer userId) {
		this.setUserId(userId);
	}

	/**
	 * @param patients
	 *            the patients to set
	 */
	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}

	/**
	 * @return the patients
	 */
	public Set<Patient> getPatients() {
		return this.patients;
	}

	/**
	 * Add a patient to the list
	 * 
	 * @param patient
	 */
	public void addPatient(Patient patient) {
		if (this.patients == null) {
			this.patients = new HashSet<Patient>();
		}
		if (!this.patients.contains(patient) && patient != null) {
			this.patients.add(patient);
		}
	}

	/**
	 * Remove this patient to the list
	 * 
	 * @param patient
	 */
	public void removePatient(Patient patient) {
		if (this.patients != null) {
			this.patients.remove(patient);
		}
	}

	/**
	 * Compares two User objects for similarity.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 *            User object to compare to.
	 * @return boolean true/false whether or not they are the same objects.
	 * @should equal userounter with same userounter id
	 * @should not equal userounter with different userounter id
	 * @should fail if obj is null
	 */
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User user = (User) obj;
			if (this.getUserId() != null && user.getUserId() != null)
				return (this.getUserId().equals(user.getUserId()));
		}
		return this == obj;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * @return int
	 * @should have same hashcode when equal
	 * @should have different hash code when not equal
	 * @should get hash code with null attributes
	 */
	public int hashCode() {
		if (this.getUserId() == null)
			return super.hashCode();
		return this.getUserId().hashCode();
	}

}
