package com.joerg.ui;

import javax.jdo.PersistenceManager;

import org.apache.commons.lang.StringUtils;

import com.joerg.model.Registration;
import com.joerg.persistance.PMF;
import com.joerg.service.MailService;
import com.joerg.ui.util.MessageHelper;

public class RegistrationBean {
	
	private String antiSpamAnswer;

	private Registration applicant = new Registration();
	
	public String send() {
		
		if (!"FranciscoCanaro".equals(antiSpamAnswer)) {
			MessageHelper.addErrorMessage("ATTENTION: Wrong Anti-Spam answer! You have NOT been registered. Try again.");
			return null;
		}
		
        try {
        	String subject = "Registration Tango Marathon Vert (" + applicant.getFullname() + ")";
        	StringBuffer body = new StringBuffer();
        	appendLine(body, "Hi " + applicant.getFirstName() + ", thank you for registering to MARATHON VERT!");
        	appendLine(body, "");
        	
//        	appendLine(body, "ENGLISH - ATTENTION: THE MARATHON IS ALREADY FULL. YOU WILL BE PUT ON THE WAITING LIST. SORRY!");
        	appendLine(body, "This email is to acknowledge receipt of your registration details. It is not a confirmation of your participation in the Marathon Vert. You will receive an email shortly to confirm your participation in the marathon.");
        	appendLine(body, "In the confirmation email, you will receive a link to the website, so you can book your accommodation.");
        	appendLine(body, "Please note that accommodation at the country hotel \"La Balmondière\" must be booked through our website and not through the hotel itself.");
        	appendLine(body, "");
//        	appendLine(body, "FRANCAIS - ATTENTION: LE MARATHON EST COMPLET. VOTRE DEMANDE SERA MISE SUR LISTE D'ATTENTE. MERCI DE VOTRE COMPRÉHENSION!");
        	appendLine(body, "Cet Email vous informe que votre inscription a été prise en compte. Vous recevrez bientôt une confirmation de votre participation au marathon Vert.");
        	appendLine(body, "Dans le mail de confirmation, un lien vous sera indiqué pour vous permettre de choisir votre hébergement.");
        	appendLine(body, "Pour le gîte La Balmondière vous devez impérativement passer par le lien pour vous inscrire, aucune inscription ne sera pris en compte par le gîte.");
        	appendLine(body, "");
        	appendLine(body, "Merci et a bientôt,");
        	appendLine(body, "Faustine.");
        	appendLine(body, "");
        	appendLine(body, "---");
        	appendLine(body, "Your data as entered by yourself:");
        	appendLine(body, applicant.toString());
        	appendLine(body, "_______________________");
        	appendLine(body, "Athos Productions");
        	appendLine(body, "9, rue Carnot 69500 BRON");
        	
        	System.out.println(body.toString());
        	MailService.sendMail(applicant.getEmail(), applicant.getFullname(), subject, body.toString(), true);
		} catch (Exception e) {
			MessageHelper.addErrorMessage("Email cannot be send. Either you have misspelled your email address, or the mail server is down. Please verify the email address that you've entered. If it is correct, try again later");
			return null;
		}
		
		applicant.setLastName(StringUtils.capitalize(applicant.getLastName()));
		applicant.setFirstName(StringUtils.capitalize(applicant.getFirstName()));
		applicant.setPartnerName(StringUtils.capitalize(applicant.getPartnerName()));
		applicant.setEmail(StringUtils.trim(applicant.getEmail()));
		applicant.setPartnerEmail(StringUtils.trim(applicant.getPartnerEmail()));
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(applicant);
        } finally {
            pm.close();
        }

		return "confirmRegistration";
	}

	public Registration getApplicant() {
		return applicant;
	}
	
	public void appendLine(StringBuffer buffer, String line) {
		buffer.append(line + "\n");
	}

	public String getAntiSpamAnswer() {
		return antiSpamAnswer;
	}

	public void setAntiSpamAnswer(String antiSpamAnswer) {
		this.antiSpamAnswer = antiSpamAnswer;
	}
}
