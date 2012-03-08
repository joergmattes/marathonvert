package com.joerg.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Applicant {
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
    private Date dateCreated = new Date();
	
	@Persistent
	private String stateCd;
	

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
		appendLine(b, "First name", firstName);
		appendLine(b, "Last name", lastName);
		appendLine(b, "Email", email);
		appendLine(b, "Sex", sex);
		appendLine(b, "Country", country);
		appendLine(b, "City", city);
		appendLine(b, "Name of partner", partnerName);
		appendLine(b, "Email of partner", partnerEmail);
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
	
}