package com.joerg.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class MailService {

	public static void sendMail(String recipientEmail, String recipientFullname, String subject, String body, boolean sendCC) throws Exception {
		Properties props = new Properties();
//		props.setProperty( "mail.mime.charset", "iso-8859-1");
		String encodedSubject = MimeUtility.encodeText(subject, "utf-8", "Q");
//		, this.charset
		Session session = Session.getDefaultInstance(props, null);

		InternetAddress faustine = new InternetAddress("faustine.boyard@gmail.com", "Faustine Boyard");
//		InternetAddress joerg = new InternetAddress("joerg.mattes@gmx.net", "Jörg Mattes");
		Message msg = new MimeMessage(session);
		msg.setFrom(faustine);
//		msg.setFrom(joerg);
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail, recipientFullname));
		if (sendCC) {
			msg.addRecipient(Message.RecipientType.CC, faustine);
//			msg.addRecipient(Message.RecipientType.CC, joerg);
		}

		msg.setSubject(encodedSubject);
		msg.setText(body);
		Transport.send(msg);

	}
}
