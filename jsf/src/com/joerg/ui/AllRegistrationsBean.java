package com.joerg.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.lang.StringUtils;

import com.joerg.model.Registration;
import com.joerg.persistance.PMF;

public class AllRegistrationsBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	List<Registration> allRegistrations;

	@SuppressWarnings("unchecked")
	public List<Registration> getAllRegistrations() {
		if (allRegistrations == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
		    Query query = pm.newQuery(Registration.class);
	//		    query.setFilter("stateCd == 'NEW'");
	//		    query.setOrdering("dateCreated asc");
	
		    try {
		    	allRegistrations = (List<Registration>) query.execute();
	
		    } finally {
		        query.closeAll();
		    }
		}
		return allRegistrations;
	}

	public String getNewRegistrations() {
		Set<String> mails = new HashSet<String>();
		List<Registration> registrations = getAllRegistrations();
		for (Registration registration : registrations) {
			if ("NEW".equals(registration.getStateCd())) {
				mails.add(registration.getEmail());
				if (registration.isPartnerSpecified()) {
					mails.add(registration.getPartnerEmail());
				}
			}
		}
		return StringUtils.join(mails, ", ");
	}

	public String getRejectedRegistrations() {
		Set<String> mails = new HashSet<String>();
		List<Registration> registrations = getAllRegistrations();
		for (Registration registration : registrations) {
			if ("REJECTED".equals(registration.getStateCd())) {
				mails.add(registration.getEmail());
				if (registration.isPartnerSpecified()) {
					mails.add(registration.getPartnerEmail());
				}
			}
		}
		return StringUtils.join(mails, ", ");
	}

}
