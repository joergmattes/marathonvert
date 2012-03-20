package com.joerg.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.lang.StringUtils;

import com.joerg.model.Participant;
import com.joerg.persistance.PMF;
import com.joerg.service.MailService;

public class ParticipantsBean extends BaseBean {

	String mailSubject;
	String mailBody;

	private static final long serialVersionUID = 1L;

	private String whatToShow = "ALL";

	private int total = 0;
	private int totalVisitors = 0;
	private int total3Days = 0;
	private int total4Days = 0;
	private int totalBudgetExp = 0;
	private int totalBudgetPaid = 0;

	private List<Participant> participants;

	public int getTotal() {
		return total;
	}

	@SuppressWarnings("unchecked")
	public List<Participant> getParticipants() {
		if (participants == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(Participant.class);
			String filterString = "";
			if (StringUtils.equals("Man", whatToShow)) {
				filterString += "sex == 'Man'";
			} else if (StringUtils.equals("Woman", whatToShow)) {
				filterString += "sex == 'Woman'";
			}
			if (StringUtils.isNotEmpty(filterString)) {
				query.setFilter(filterString);
			}
			query.setOrdering("fullname asc");

			try {
				participants = (List<Participant>) query.execute();
				totalVisitors = 0;
				for (Participant participant : participants) {
					totalBudgetExp += participant.getAmountToPayEuro();
					if (participant.isPaymentOk()) {
						totalBudgetPaid += participant.getAmountToPayEuro();
					}
					if ("VISITOR".equals(participant.getStateCd())) {
						totalVisitors++;
					}
					if (participant.getNumberOfDays() == 3) {
						total3Days++;
					} else {
						total4Days++;
					}
				}
				total = participants.size();

			} finally {
				query.closeAll();
			}
		}

		return participants;
	}

	@SuppressWarnings("unchecked")
	public List<Participant> getParticipantsSortedByCountry() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Participant.class);
		query.setOrdering("country asc, city asc");

		try {
			List<Participant> results = (List<Participant>) query.execute();
			List<Participant> participants = new ArrayList<Participant>();
			for (Participant participant : results) {
				// if (!"VISITOR".equals(participant.getStateCd())) {
				participants.add(participant);
				// }
			}
			return participants;
		} finally {
			query.closeAll();
		}
	}

	public void showAll() {
		whatToShow = "ALL";
		participants = null;
	}

	public void showMen() {
		whatToShow = "Man";
		participants = null;
	}

	public void showWomen() {
		whatToShow = "Woman";
		participants = null;
	}

	public void writeMail() throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Participant.class);
		List<Participant> participants = null;
		try {
			participants = (List<Participant>) query.execute();
		} finally {
			query.closeAll();
		}

		for (Participant participant : getParticipants()) {
			try {
				System.err.println(participant.getEmail());
				// if ("joerg.mattes@gmx.net".equals(participant.getEmail())) {
				// System.err.println("sending mail to joerg...");
				MailService
						.sendMail(participant.getEmail(),
								participant.getFullname(), mailSubject,
								mailBody, false);
				// }
			} catch (Exception e) {
				System.err.println("Cannot send email to: "
						+ participant.getEmail() + "; " + e.getMessage());
				e.printStackTrace();
			}

		}
		// try {
		// MailService.sendMail("bla@blablajoerg.comjoerg", "bla", mailSubject,
		// mailBody, false);
		// } catch(Exception e) {
		// System.err.println("Cannot send email to: " +
		// "bla@blablajoerg.comjoerg");
		// }
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public String getAllMails() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Participant.class);
		List<Participant> participants = null;
		try {
			participants = (List<Participant>) query.execute();
		} finally {
			query.closeAll();
		}

		StringBuffer all = new StringBuffer();
		for (Participant participant : getParticipants()) {
			all.append(participant.getEmail() + ", ");
		}
		all.delete(all.length() - 2, all.length() - 1);
		return all.toString();
	}

	public void setAllMails(String ignore) {
	}

	public int getTotalVisitors() {
		return totalVisitors;
	}

	public int getTotalBudgetExp() {
		return totalBudgetExp;
	}

	public int getTotalBudgetPaid() {
		return totalBudgetPaid;
	}

	public int getTotal3Days() {
		return total3Days;
	}

	public int getTotal4Days() {
		return total4Days;
	}

}
