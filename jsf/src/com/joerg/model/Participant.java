package com.joerg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.Key;
import com.joerg.persistance.PMF;
import com.joerg.service.MailService;
import com.joerg.ui.Util;
import com.joerg.ui.util.MessageHelper;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Participant implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Long DEFAULT_AMOUNT_3 = 95l;
	public static Long DEFAULT_AMOUNT_4 = 130l;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

	@Persistent
    private Integer numberOfDays;
	
    @Persistent
    private String fullname;

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
    private Date dateCreated = new Date();
	
	@Persistent
	private Long amountToPayEuro = null;
	
	@Persistent
	private String payedCd = "NOTYET";
	
	@Persistent
    private Date dateAccepted = new Date();

	@Persistent
    private String stateCd;
	
	@Persistent
	private boolean accMailSent = false;

	@Persistent
	private String roomType;

	@Persistent
    private String comment;
	
	@Persistent(mappedBy="participant")
	private Key bedKey;

	public Participant() {}
	
	public Participant(Registration registration) {
		try {
			BeanUtils.copyProperties(this, registration);
			stateCd = "NEW";
			key = null;
			if (numberOfDays == 3) {
				amountToPayEuro = DEFAULT_AMOUNT_3;
			} else {
				amountToPayEuro = DEFAULT_AMOUNT_4;
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getStateCd() {
		return stateCd;
	}

	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}


	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public Long getAmountToPayEuro() {
		return amountToPayEuro;
	}

	public void setAmountToPayEuro(Long amountToPayEuro) {
		this.amountToPayEuro = amountToPayEuro;
	}

	public String getPayedCd() {
		return payedCd;
	}

	public void setPayedCd(String payedCd) {
		this.payedCd = payedCd;
	}

	public Date getDateAccepted() {
		return dateAccepted;
	}

	public void setDateAccepted(Date dateAccepted) {
		this.dateAccepted = dateAccepted;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPartnerEmail() {
		return partnerEmail;
	}

	public void setPartnerEmail(String partnerEmail) {
		this.partnerEmail = partnerEmail;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	
	public String sendAcceptanceMail() {
        try {
        	String subject = "Tango Marathon Vert (" + getFullname() + ") --> you're in!";
        	StringBuffer body = new StringBuffer();
        	Util.appendLine(body, "Dear " + getFullname() + ",");
        	Util.appendLine(body, "YOU ARE IN!");
        	Util.appendLine(body, "");
        	Util.appendLine(body, "ENGLISH");
        	Util.appendLine(body, "To choose your accommodation, please use this link:");
        	Util.appendLine(body, "http://marathonvert.appspot.com/site/book/start.jsf");
        	Util.appendLine(body, "If you wish to organize your accomodation yourself, please use the link in any case, and click 'I will organize my own accomodation'.");
        	Util.appendLine(body, "");
        	Util.appendLine(body, "FRANCAIS");
        	Util.appendLine(body, "Pour choisir votre h�bergement cliquez sur ce lien:");
        	Util.appendLine(body, "http://marathonvert.appspot.com/site/book/start.jsf");
        	Util.appendLine(body, "Si vous �tes en autonomie pour l�h�bergement, merci d�utiliser ce lien pour nous en informer. Apr�s quoi vous recevrez un mail avec le montant � r�gler.");
        	Util.appendLine(body, "");
        	Util.appendLine(body, "Merci et a bient�t,");
        	Util.appendLine(body, "Faustine.");
        	System.out.println(body.toString());
        	MailService.sendMail(getEmail(), getFullname(), subject, body.toString(), false);
		} catch (Exception e) {
			MessageHelper.addErrorMessage("Email cannot be send: " + e.getMessage());
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Participant myself = (Participant) pm.getObjectById(getClass(), getKey());
		myself.setAccMailSent(true);
		pm.close();
		return "participants";
	}
	
	public String delete() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Participant myself = (Participant) pm.getObjectById(getClass(), getKey());
		if (bedKey != null) {
			BedPlace bed = (BedPlace) pm.getObjectById(BedPlace.class, bedKey);
			bed.setFree(true);
			bed.setParticipantKey(null);
		}
		pm.deletePersistent(myself);
		pm.close();
		return "participants";
	}

	public boolean isAccMailSent() {
		return accMailSent;
	}
	
	public String paymentOk() {
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Participant myself = (Participant) pm.getObjectById(getClass(), getKey());
			myself.setPayedCd("OK");
			pm.close();
			

			String subject = "Tango Marathon Vert (" + getFullname() + ") --> payment received!";
			StringBuffer body = new StringBuffer();
			Util.appendLine(body, "Dear " + getFullname() + ",");
			Util.appendLine(body, "we have received your payment for the marathon vert!");
			Util.appendLine(body, "");
			Util.appendLine(body, "Merci et a bient�t,");
			Util.appendLine(body, "Faustine.");
			System.out.println(body.toString());
			MailService.sendMail(getEmail(), getFullname(), subject, body.toString(), false);
		} catch (Exception e) {
			MessageHelper.addErrorMessage("Email cannot be send: " + e.getMessage());
		}
		return "participants";
	}
	
	public String cancelBooking() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Participant myself = (Participant) pm.getObjectById(getClass(), getKey());
		Key bedKey = myself.getBedKey();
		myself.setRoomType(null);
		myself.setBedKey(null);
		if (myself.getNumberOfDays() == 3) {
			myself.setAmountToPayEuro(DEFAULT_AMOUNT_3);
		} else {
			myself.setAmountToPayEuro(DEFAULT_AMOUNT_4);
		}
		if (bedKey != null) {
			BedPlace bed = (BedPlace) pm.getObjectById(BedPlace.class, bedKey);
			bed.setFree(true);
			bed.setParticipantKey(null);
		}
		
		pm.close();
		return "participants";
	}

	public void setAccMailSent(boolean accMailSent) {
		this.accMailSent = accMailSent;
	}

	public boolean isPaymentOk() {
		return StringUtils.equals("OK", payedCd);
	}

	public void addAmountToPay(Long costPerPerson) {
		amountToPayEuro += costPerPerson;
	}

	public Key getBedKey() {
		return bedKey;
	}

	public void setBedKey(Key bedKey) {
		this.bedKey = bedKey;
	}
	
	public Long getAmountToPayChf() {
//		Long result = 
		BigDecimal chf = new BigDecimal("1.46");
		chf = chf.multiply(new BigDecimal(amountToPayEuro));
		chf = chf.setScale(0, RoundingMode.HALF_UP);
		return chf.longValue();
	}
	
	public boolean isBooked() {
		return roomType != null;
	}

	public Integer getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
}