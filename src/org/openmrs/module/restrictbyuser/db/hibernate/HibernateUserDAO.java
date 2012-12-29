package org.openmrs.module.restrictbyuser.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.Person;
import org.openmrs.Role;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.restrictbyuser.User;
import org.openmrs.module.restrictbyuser.db.UserDAO;

public class HibernateUserDAO extends
		org.openmrs.api.db.hibernate.HibernateUserDAO implements UserDAO {

	private SessionFactory sessionFactory;

	/** Default constructor. */
	public HibernateUserDAO() {
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		super.setSessionFactory(this.sessionFactory);
	}

	@Override
	public org.openmrs.User getUser(Integer userId) {
		return (org.openmrs.User) this.sessionFactory.getCurrentSession().get(
				User.class, userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.openmrs.User getUserByUsername(String username) {
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from User u where u.retired = '0' and (u.username = ? or u.systemId = ?)");
		query.setString(0, username);
		query.setString(1, username);
		List<org.openmrs.User> users = query.list();

		if (users == null || users.size() == 0) {
			log.warn("request for username '" + username + "' not found");
			return null;
		}

		return convertOpenmrsUsersToModuleUsers(users).get(0);
	}

	@Override
	public List<org.openmrs.User> getAllUsers() throws DAOException {
		List<org.openmrs.User> openmrsUsers = super.getAllUsers();
		return convertOpenmrsUsersToModuleUsers(openmrsUsers);
	}

	@Override
	public List<org.openmrs.User> getUsersByRole(Role role) {
		return convertOpenmrsUsersToModuleUsers(super.getUsersByRole(role));
	}

	@Override
	public List<org.openmrs.User> getUsersByPerson(Person person,
			boolean includeRetired) {
		return convertOpenmrsUsersToModuleUsers(super.getUsersByPerson(person,
				includeRetired));
	}

	/**
	 * @see org.openmrs.api.db.UserDAO#getUserByUuid(java.lang.String)
	 */
	@Override
	public org.openmrs.User getUserByUuid(String uuid) {
		User ret = null;

		if (uuid != null) {
			uuid = uuid.trim();
			ret = (User) sessionFactory.getCurrentSession()
					.createQuery("from User u where u.uuid = :uuid")
					.setString("uuid", uuid).uniqueResult();
		}

		return ret;
	}

	private List<org.openmrs.User> convertOpenmrsUsersToModuleUsers(
			List<org.openmrs.User> openmrsUsers) {
		List<org.openmrs.User> moduleUsers = new ArrayList<org.openmrs.User>();

		for (org.openmrs.User currOpenmrsUser : openmrsUsers) {
			moduleUsers.add(this.getUser(currOpenmrsUser.getUserId()));
		}
		return moduleUsers;
	}

}
