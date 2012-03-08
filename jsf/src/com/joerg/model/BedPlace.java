package com.joerg.model;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.joerg.ui.BookBean2;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class BedPlace implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent
    private Key key;
    
    @Persistent
    private String bedId;
    
	@Persistent
    private String roomName;

	@Persistent
    private String roomType;
	
	@Persistent
    private Long roomCapacity;
  
    @Persistent
    private boolean doubleBed;
    
    @Persistent
    private Long costPerPerson;
    
    @Persistent
    private boolean free = true;
    
    @Persistent
    private boolean sleepingBag = false;
    

	@Persistent
    private Key participantKey;
    
	@NotPersistent
	private String tmpString;
	
	@NotPersistent
	private Participant participant;
	
    public boolean isSleepingBag() {
		return sleepingBag;
	}

	public void setSleepingBag(boolean sleepingBag) {
		this.sleepingBag = sleepingBag;
	}
    public String getTmpString() {
		return tmpString;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}


    public final static String TYPE_PRIVATE 	= "Marathon House Private Room";
    public final static String TYPE_MANSARD 	= "Marathon House Mansard Room";
    public final static String TYPE_TIPI	 	= "Marathon House Tipi";
    public final static String TYPE_DORM_BED 	= "Marathon House Dormitory (beds)";
    public final static String TYPE_DORM_CARPET = "Marathon House Dormitory (carpet)";
    public final static String TYPE_HOTEL 		= "Country Hotel La Balmondiere";
    public final static String TYPE_CAMPING 	= "Camping";
    public final static String TYPE_INDEPENDENT = "Independent";
    
	public BedPlace() {}
	
	public BedPlace(Key key, String bedId, String roomType, String roomName, Long roomCapacity, boolean doubleBed, Long costPerPerson, boolean sleepingBag) {
		super();
		this.key = key;
		this.bedId = bedId;
		this.roomType = roomType;
		this.roomName = roomName;
		this.roomCapacity = roomCapacity;
		this.doubleBed = doubleBed;
		this.costPerPerson = costPerPerson;
		this.sleepingBag = sleepingBag;
	}

	public BedPlace(BedPlace template) {
		super();
		this.key = template.getKey();
		this.bedId = template.getBedId();
		this.roomType = template.getRoomType();
		this.roomName = template.getRoomName();
		this.roomCapacity = template.getRoomCapacity();
		this.doubleBed = template.isDoubleBed();
		this.costPerPerson = template.getCostPerPerson();
		this.sleepingBag = template.isSleepingBag();
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Long getRoomCapacity() {
		return roomCapacity;
	}

	public void setRoomCapacity(Long roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	public boolean isDoubleBed() {
		return doubleBed;
	}

	public void setDoubleBed(boolean doubleBed) {
		this.doubleBed = doubleBed;
	}

	public Long getCostPerPerson() {
		return costPerPerson;
	}

	public void setCostPerPerson(Long costPerPerson) {
		this.costPerPerson = costPerPerson;
	}
	
    public String getBedId() {
		return bedId;
	}

	public void setBedId(String bedId) {
		this.bedId = bedId;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
	
	public void chooseForPart1() {
		free = false;
		tmpString = "(Chosen by you)";
		BookBean2.getBookBean().chooseBed1(this);
	}
	
	public void chooseForPart2() {
		free = false;
		tmpString = "(Chosen by you)";
		BookBean2.getBookBean().chooseBed2(this);
	}

	public String getBedDescription() {
		if (isDoubleBed()) {
			return "Double Bed";
		} else if (isSleepingBag()) {
			return "Sleeping Bag";
		} else {
			return "Single Bed";
		}
	}
	
	public String getDescription() {
		return roomName + " / " + getBedDescription() + " " + getBedId();
	}
	
	public String getChosenBy() {
		if (getTmpString() != null) {
			return getTmpString();
		} else if (!free) {
			return "(not available)";
		} else {
			return "";
		}
	}

	public Key getParticipantKey() {
		return participantKey;
	}

	public void setParticipantKey(Key participantKey) {
		this.participantKey = participantKey;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
}