package com.joerg.ui;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.joerg.model.Registration;
import com.joerg.persistance.PMF;

public class NewRegistrationsBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	List<Registration> newRegistrations;

	@SuppressWarnings("unchecked")
	public List<Registration> getNewRegistrations() {
		if (newRegistrations == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
		    Query query = pm.newQuery(Registration.class);
			    query.setFilter("stateCd == 'NEW'");
			    query.setOrdering("dateCreated desc");
		    try {
		    	newRegistrations = (List<Registration>) query.execute();
		    } finally {
		        query.closeAll();
		    }
		}
		return newRegistrations;
	}

}
