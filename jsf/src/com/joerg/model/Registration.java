package com.joerg.model;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.Key;
import com.joerg.persistance.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Registration {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String firstName;

    @Persistent
    private String lastName;
    
    @Persistent
    private String sex;
    
	@Persistent
    private String country;

	@Persistent
    private String city;
	
	@Persistent
    private String email;

    @Persistent
    private String partnerEmail;
    
    @Persistent
    private String partnerName;

    @Persistent
    private String partnerCity;

	@Persistent
    private String partnerCountry;
	
	@Persistent
    private Integer numberOfDays;


	@Persistent
    private Date dateCreated = new Date();
	
	@Persistent
	private String stateCd = "NEW";
	
	@Persistent
    private Date dateAccOrRej;
	
	@Persistent
    private String vegetarian;
	
	@Persistent
    private String partnerVegetarian;

	public Date getDateAccOrRej() {
		return dateAccOrRej;
	}

	public void setDateAccOrRej(Date dateAccOrRej) {
		this.dateAccOrRej = dateAccOrRej;
	}

	public String getFullname() {
		return firstName + " " + lastName;
	}
    
	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	
    public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}  
    
    public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
    public String getPartnerEmail() {
		return partnerEmail;
	}

	public void setPartnerEmail(String partnerEmail) {
		this.partnerEmail = partnerEmail;
	}



    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Key getKey() {
        return key;
    }

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	public String toString() {
		StringBuffer b = new StringBuffer();
		appendLine(b, "I want to stay", numberOfDays + " days");
		appendLine(b, "First name", firstName);
		appendLine(b, "Last name", lastName);
		appendLine(b, "Email", email);
		appendLine(b, "Sex", sex);
		appendLine(b, "Nationality", country);
		appendLine(b, "City", city);
		appendLine(b, "Vegetarian", vegetarian);
		appendLine(b, "Name of partner", partnerName);
		appendLine(b, "Email of partner", partnerEmail);
		appendLine(b, "City of partner", partnerCity);
		appendLine(b, "Nationality of partner", partnerCountry == null ? "" : partnerCountry);
		appendLine(b, "Partner Vegetarian", partnerVegetarian == null ? "" : partnerVegetarian);
//		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
		return b.toString();
	}

	public void appendLine(StringBuffer buffer, String key, String value) {
		buffer.append(StringUtils.rightPad(key + ": ", 30) + value + "\n");
	}

	public String getStateCd() {
		return stateCd;
	}

	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}
	
	public boolean isPartnerSpecified() {
		return StringUtils.isNotBlank(partnerEmail);
	}
	
	public String acceptWithPartner() {
		return accept(true);
	}
	
	public String acceptWithoutPartner() {
		return accept(false);
	}

	public String acceptVisitor() {
		PersistenceManager pm = getPm();
	    
        Participant participant = new Participant(this);
        participant.setAmountToPayEuro(70L);
        participant.setStateCd("VISITOR");
        pm.makePersistent(participant);
        
        updateStateCdInActiveTrx("VISITOR_ACCEPTED", pm);

		pm.close();
		return "newRegistrations";
	}

	private String accept(boolean withPartner) {
		PersistenceManager pm = getPm();
    
        Participant participant = new Participant(this);
        pm.makePersistent(participant);
        if (withPartner) {
    		Participant partner = new Participant();
    		partner.setNumberOfDays(numberOfDays);
    		partner.setCity(getPartnerCity());
    		partner.setCountry(getPartnerCountry());
    		partner.setEmail(getPartnerEmail());
    		partner.setFullname(getPartnerName());
    		partner.setSex(getOppositeSex());
    		partner.setStateCd("NEW");
    		partner.setPartnerEmail(getEmail());
    		partner.setPartnerName(getFullname());
    		partner.setVegetarian(getPartnerVegetarian());
			if (partner.getNumberOfDays() == 3) {
				partner.setAmountToPayEuro(Participant.DEFAULT_AMOUNT_3);
			} else {
				partner.setAmountToPayEuro(Participant.DEFAULT_AMOUNT_4);
			}
    		pm.makePersistent(partner);
        }
        
        updateStateCdInActiveTrx("ACCEPTED", pm);

		pm.close();
		return "newRegistrations";
	}

	public String alreadyAccepted() {
		updateStateCdInNewTrx("ALREADY_ACCEPTED");
		return "newRegistrations";
	}
	
	public String reject() {
		updateStateToRejected();
		return "newRegistrations";
	}
	
	private PersistenceManager getPm() {
		return PMF.get().getPersistenceManager();
	}
	
	private void updateStateToAccepted() {
		updateStateCdInNewTrx("ACCEPTED");
	}
	private void updateStateToRejected() {
		updateStateCdInNewTrx("REJECTED");
	}


	private void updateStateCdInActiveTrx(String stateCd, PersistenceManager pm) {
		Registration myself = (Registration) pm.getObjectById(getClass(), getKey());
		myself.setStateCd(stateCd);
	}
	
	private void updateStateCdInNewTrx(String stateCd) {
		PersistenceManager pm = getPm();
		Registration myself = (Registration) pm.getObjectById(getClass(), getKey());
		myself.setStateCd(stateCd);
		pm.close();
	}
	
	private String getOppositeSex() {
		if (StringUtils.equals("Man", sex)) {
			return "Woman";
		} else {
			return "Man";
		}
	}
	
	public String delete() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Registration myself = (Registration) pm.getObjectById(getClass(), getKey());
		pm.deletePersistent(myself);
		pm.close();
		return "newRegistrations";
	}
	
    
    public String getPartnerCity() {
		return partnerCity;
	}

	public void setPartnerCity(String partnerCity) {
		this.partnerCity = partnerCity;
	}

	public String getPartnerCountry() {
		return partnerCountry;
	}

	public void setPartnerCountry(String partnerCountry) {
		this.partnerCountry = partnerCountry;
	}

    
	public Integer getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public String getVegetarian() {
		return vegetarian;
	}

	public void setVegetarian(String vegetarian) {
		this.vegetarian = vegetarian;
	}

	public String getPartnerVegetarian() {
		return partnerVegetarian;
	}

	public void setPartnerVegetarian(String partnerVegetarian) {
		this.partnerVegetarian = partnerVegetarian;
	}
}