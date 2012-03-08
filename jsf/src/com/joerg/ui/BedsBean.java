package com.joerg.ui;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.joerg.model.BedPlace;
import com.joerg.model.Participant;
import com.joerg.persistance.PMF;

public class BedsBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	List<BedPlace> beds;
	
	@SuppressWarnings("unchecked")
	public List<BedPlace> getBeds() {
		if (beds == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
		    Query query = pm.newQuery(BedPlace.class);
		    query.setOrdering("key asc");
	
		    try {
		    	Query query2 = pm.newQuery(Participant.class);
		    	List<Participant> participants = (List<Participant>) query2.execute();
		    	
		    	
		    	beds = (List<BedPlace>) query.execute();
		    	
		    	for (BedPlace bed : beds) {
					if (bed.getParticipantKey() != null) {
						bed.setParticipant(pm.getObjectById(Participant.class, bed.getParticipantKey()));
					}
				}
		    } finally {
		        query.closeAll();
		    }
		}
		return beds;
	}
	
}
