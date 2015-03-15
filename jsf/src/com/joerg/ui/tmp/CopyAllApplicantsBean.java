package com.joerg.ui.tmp;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.joerg.model.Applicant;
import com.joerg.model.BedPlace;
import com.joerg.model.Registration;
import com.joerg.persistance.PMF;
import com.joerg.ui.BaseBean;
import com.joerg.ui.util.MessageHelper;

public class CopyAllApplicantsBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	private int clicked = 0;
	
	@SuppressWarnings("unchecked")
	public void copy() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    Query query = pm.newQuery(Applicant.class);
//		    query.setFilter("stateCd == null");
//		    query.setOrdering("dateCreated asc");

	    try {
	    	List<Applicant> applicants = (List<Applicant>) query.execute();
	    	for (Applicant applicant : applicants) {
				Registration registration = new Registration();
				BeanUtils.copyProperties(registration, applicant);
				registration.setStateCd("NEW");
				pm.makePersistent(registration);
			}
	    } catch(Exception e) {
	    	throw new RuntimeException(e);
	    } finally {
	        query.closeAll();
	    }
	}
	
	public void create212() {
		List<BedPlace> beds = new ArrayList<BedPlace>();
		createBed(beds, "212", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 14, false, 40, false);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		for (BedPlace bedPlace : beds) {
			pm.makePersistent(bedPlace);
		}
	}
	
	public void createBeds() {
		List<BedPlace> beds = new ArrayList<BedPlace>();
//		createBed(beds, "101a", BedPlace.TYPE_PRIVATE, "Marathon House Room 'MM'", 2, true, 80, false);
//		createBed(beds, "101b", BedPlace.TYPE_PRIVATE, "Marathon House Room 'MM'", 2, true, 80, false);
//		createBed(beds, "102a", BedPlace.TYPE_PRIVATE, "Marathon House Room 'Philippine'", 2, true, 80, false);
//		createBed(beds, "102b", BedPlace.TYPE_PRIVATE, "Marathon House Room 'Philippine'", 2, true, 80, false);		
//		createBed(beds, "103a", BedPlace.TYPE_PRIVATE, "Marathon House Room 'Jeremie'", 2, true, 80, false);
//		createBed(beds, "103b", BedPlace.TYPE_PRIVATE, "Marathon House Room 'Jeremie'", 2, true, 80, false);		
//		createBed(beds, "104a", BedPlace.TYPE_PRIVATE, "Marathon House Room 'Faustine'", 2, true, 80, false);
//		createBed(beds, "104b", BedPlace.TYPE_PRIVATE, "Marathon House Room 'Faustine'", 2, true, 80, false);

//		createBed(beds, "107a", BedPlace.TYPE_TIPI, "Marathon House Tipi (beds)", 3, true, 80, false);
//		createBed(beds, "107b", BedPlace.TYPE_TIPI, "Marathon House Tipi (beds)", 3, true, 80, false);
//		createBed(beds, "108",  BedPlace.TYPE_TIPI, "Marathon House Tipi (beds)", 3, false, 80, false);

		createBed(beds, "201a", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 65, false);
		createBed(beds, "201b", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 65, false);		
		createBed(beds, "202a", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 65, false);
		createBed(beds, "202b", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 65, false);		
		createBed(beds, "207", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, false, 65, false);
		createBed(beds, "208", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, false, 65, false);		
		createBed(beds, "209", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, false, 65, false);
		createBed(beds, "210", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, false, 65, false);
		createBed(beds, "211", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, false, 65, false);

		createBed(beds, "114", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 7, false, 45, true);
		createBed(beds, "115", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 7, false, 45, true);
		createBed(beds, "116", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 7, false, 45, true);
		createBed(beds, "117", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 7, false, 45, true);
		createBed(beds, "118", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 7, false, 45, true);
		createBed(beds, "119", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 7, false, 45, true);
		createBed(beds, "120", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 7, false, 45, true);

		createBed(beds, "121", BedPlace.TYPE_DORM_CARPET_GROUND, "Marathon House Dormitory (carpet, ground floor)", 6, false, 40, true);
		createBed(beds, "122", BedPlace.TYPE_DORM_CARPET_GROUND, "Marathon House Dormitory (carpet, ground floor)", 6, false, 40, true);
		createBed(beds, "123", BedPlace.TYPE_DORM_CARPET_GROUND, "Marathon House Dormitory (carpet, ground floor)", 6, false, 40, true);
		createBed(beds, "124", BedPlace.TYPE_DORM_CARPET_GROUND, "Marathon House Dormitory (carpet, ground floor)", 6, false, 40, true);
		createBed(beds, "125", BedPlace.TYPE_DORM_CARPET_GROUND, "Marathon House Dormitory (carpet, ground floor)", 6, false, 40, true);
		createBed(beds, "126", BedPlace.TYPE_DORM_CARPET_GROUND, "Marathon House Dormitory (carpet, ground floor)", 6, false, 40, true);

		createBed(beds, "109a", BedPlace.TYPE_TENT, "Marathon House Cottage (beds)", 6, true, 80, false);
		createBed(beds, "109b", BedPlace.TYPE_TENT, "Marathon House Cottage (beds)", 6, true, 80, false);
		createBed(beds, "110a", BedPlace.TYPE_TENT, "Marathon House Cottage (beds)", 6, true, 80, false);
		createBed(beds, "110b", BedPlace.TYPE_TENT, "Marathon House Cottage (beds)", 6, true, 80, false);
		createBed(beds, "111", BedPlace.TYPE_TENT, "Marathon House Cottage (beds)", 6, false, 80, false);
		createBed(beds, "112", BedPlace.TYPE_TENT, "Marathon House Cottage (beds)", 6, false, 80, false);
		createBed(beds, "113", BedPlace.TYPE_TENT, "Marathon House Cottage (beds)", 6, false, 80, false);


		createBed(beds, "105a", BedPlace.TYPE_MANSARD, "Marathon House Mansard Room", 3, true, 80, false);
		createBed(beds, "105b", BedPlace.TYPE_MANSARD, "Marathon House Mansard Room", 3, true, 80, false);
		createBed(beds, "106",  BedPlace.TYPE_MANSARD, "Marathon House Mansard Room", 3, false, 80, false);



//		createBed(beds, "203a", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 60, false);
//		createBed(beds, "203b", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 60, false);		
//		createBed(beds, "204a", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 60, false);
//		createBed(beds, "204b", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 60, false);	
//		createBed(beds, "205a", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 60, false);
//		createBed(beds, "205b", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 60, false);	
//		createBed(beds, "206a", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 60, false);
//		createBed(beds, "206b", BedPlace.TYPE_DORM_BED, "Marathon House Dormitory (beds)", 17, true, 60, false);	
//		createBed(beds, "113a", BedPlace.TYPE_TENT, "Marathon House Tent (beds)", 6, true, 80, false);
//		createBed(beds, "113b", BedPlace.TYPE_TENT, "Marathon House Tent (beds)", 6, true, 80, false);
//		createBed(beds, "114a", BedPlace.TYPE_TENT, "Marathon House Tent (beds)", 6, true, 80, false);
//		createBed(beds, "114b", BedPlace.TYPE_TENT, "Marathon House Tent (beds)", 6, true, 80, false);
//		createBed(beds, "115", BedPlace.TYPE_TENT, "Marathon House Tent (beds)", 6, false, 80, false);
//		createBed(beds, "116", BedPlace.TYPE_TENT, "Marathon House Tent (beds)", 6, false, 80, false);

		
//		createBed(beds, "301", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "302", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);		
//		createBed(beds, "303", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "304", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);		
//		createBed(beds, "305", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "306", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);		
//		createBed(beds, "307", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "308", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);	
//		createBed(beds, "309", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "310", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);		
//		createBed(beds, "311", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "312", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);		
//		createBed(beds, "313", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "314", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);		
//		createBed(beds, "315", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "316", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);	
//		createBed(beds, "317", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "318", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);		
//		createBed(beds, "319", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);
//		createBed(beds, "320", BedPlace.TYPE_DORM_CARPET, "Marathon House Dormitory (carpet)", 14, false, 24, true);		
	
		createBed(beds, "451a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 1", 5, true, 80, false);
		createBed(beds, "451b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 1", 5, true, 80, false);
		createBed(beds, "452a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 1", 5, true, 80, false);
		createBed(beds, "452b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 1", 5, true, 80, false);
		createBed(beds, "453", BedPlace.TYPE_HOTEL, "La Balmondiere Room 1", 5, false, 80, false);
		createBed(beds, "454a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 2", 5, true, 80, false);
		createBed(beds, "454b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 2", 5, true, 80, false);
		createBed(beds, "455a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 2", 5, true, 80, false);
		createBed(beds, "455b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 2", 5, true, 80, false);
		createBed(beds, "456", BedPlace.TYPE_HOTEL, "La Balmondiere Room 2", 5, false, 80, false);
		createBed(beds, "457a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 3", 5, true, 80, false);
		createBed(beds, "457b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 3", 5, true, 80, false);
		createBed(beds, "458a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 3", 5, true, 80, false);
		createBed(beds, "458b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 3", 5, true, 80, false);
		createBed(beds, "459", BedPlace.TYPE_HOTEL, "La Balmondiere Room 3", 5, false, 80, false);
		createBed(beds, "410a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 4", 5, true, 80, false);
		createBed(beds, "410b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 4", 5, true, 80, false);
		createBed(beds, "411a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 4", 5, true, 80, false);
		createBed(beds, "411b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 4", 5, true, 80, false);
		createBed(beds, "412", BedPlace.TYPE_HOTEL, "La Balmondiere Room 4", 5, false, 80, false);
		createBed(beds, "413a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 5", 5, true, 80, false);
		createBed(beds, "413b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 5", 5, true, 80, false);
		createBed(beds, "414a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 5", 5, true, 80, false);
		createBed(beds, "414b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 5", 5, true, 80, false);
		createBed(beds, "415", BedPlace.TYPE_HOTEL, "La Balmondiere Room 5", 5, false, 80, false);
		
		createBed(beds, "416a", BedPlace.TYPE_HOTEL, "La Balmondiere Room 6", 5, true, 80, false);
		createBed(beds, "416b", BedPlace.TYPE_HOTEL, "La Balmondiere Room 6", 5, true, 80, false);
		createBed(beds, "417", BedPlace.TYPE_HOTEL, "La Balmondiere Room 6", 5, false, 80, false);
		
//		createBed(beds, "418", BedPlace.TYPE_HOTEL, "La Balmondiere Dormitory", 5, false, 35, false);
//		createBed(beds, "419", BedPlace.TYPE_HOTEL, "La Balmondiere Dormitory", 5, false, 35, false);
//		createBed(beds, "420", BedPlace.TYPE_HOTEL, "La Balmondiere Dormitory", 5, false, 35, false);
//		createBed(beds, "421", BedPlace.TYPE_HOTEL, "La Balmondiere Dormitory", 5, false, 35, false);
//		createBed(beds, "422", BedPlace.TYPE_HOTEL, "La Balmondiere Dormitory", 5, false, 35, false);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		for (BedPlace bedPlace : beds) {
			pm.makePersistent(bedPlace);
		}
	}
	
	private void createBed(List<BedPlace> beds, String id, String roomType, String roomName, int roomCapacity, boolean doubleBed, int costPerPerson, boolean sleepingBag) {
		beds.add(new BedPlace(getKey(id), getBedId(id), roomType, roomName, (long)roomCapacity, doubleBed, (long)costPerPerson, sleepingBag));
	}
	
	private Key getKey(String id) {
		return KeyFactory.createKey("BedPlace", id);
	}
	
	private String getBedId(String id) {
		return StringUtils.substring(id, 0, 3);
	}
	
	public void increment() {
		clicked++;
	}
	
	public int getClicked() {
		return clicked;
	}
	
	public void invalidateSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		MessageHelper.addInfoMessage("Session invalidated.");
	}
}
