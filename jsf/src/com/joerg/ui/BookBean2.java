package com.joerg.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.Key;
import com.joerg.model.BedPlace;
import com.joerg.model.Participant;
import com.joerg.service.MailService;
import com.joerg.ui.util.MessageHelper;

public class BookBean2 extends BaseBean {

	private static final long serialVersionUID = 1L;

	private String emailSearch1;
	private String emailSearch2;
	private Key part1Key;
	private Key part2Key;
	private String bookingFor;

	private String roomType;

	private int counter;

	private boolean stays3Nights;

	private static final Logger log = Logger.getLogger(BookBean2.class
			.getName());

	public String getRoomType() {
		return roomType;
	}

	private List<BedPlace> bedPlaces;

	private BedPlace bed1;
	private BedPlace bed2;

	public static BookBean2 getBookBean() {
		return (BookBean2) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("bookBean2");
	}

	public List<BedPlace> getBedPlaces() {
		return bedPlaces;
	}

	public void setBedPlaces(List<BedPlace> bedPlaces) {
		this.bedPlaces = bedPlaces;
	}

	public String getBedType() {
		return roomType;
	}

	public void setBedType(String bedType) {
		this.roomType = bedType;
	}

	public String getEmailSearch1() {
		return emailSearch1;
	}

	public void setEmailSearch1(String emailSearch1) {
		this.emailSearch1 = emailSearch1;
	}

	public String getEmailSearch2() {
		return emailSearch2;
	}

	public void setEmailSearch2(String emailSearch2) {
		this.emailSearch2 = emailSearch2;
	}

	public String cancel() {
		emailSearch1 = null;
		emailSearch2 = null;
		part1Key = null;
		part2Key = null;
		return "cancelBook";
	}

	@SuppressWarnings("unchecked")
	public String step1() {
		log.info("start step1");
		PersistenceManager pm = getPm();
		Participant part1 = null;
		Participant part2 = null;
		part1Key = null;
		part2Key = null;
		bed1 = null;
		bed2 = null;
		bookingFor = null;

		if (StringUtils.isEmpty(emailSearch1)) {
			MessageHelper.addErrorMessage("You must enter an email address.");
			return null;
		} else {
			Query query = pm.newQuery(Participant.class);
			query.setFilter("email == '" + emailSearch1 + "'");
			// query.declareParameters("String emailSearch");
			// List<Participant> partList = (List<Participant>)
			// query.execute(emailSearch1);
			List<Participant> partList = (List<Participant>) query.execute();
			if (partList.isEmpty()) {
				MessageHelper
						.addErrorMessage("No participant for email "
								+ emailSearch1
								+ " found - please enter the email address that you specified during registration.");
				return null;
			}
			if (partList.size() > 1) {
				MessageHelper
						.addErrorMessage("More than one participant for email "
								+ emailSearch1
								+ " found - I cannot identify for which you want to book. Please contact Faustine to clean up the registration.");
				return null;
			}
			part1 = partList.get(0);
			stays3Nights = (part1.getNumberOfDays() == 4);

			part1Key = part1.getKey();
			if (StringUtils.isNotBlank(part1.getRoomType())) {
				MessageHelper
						.addErrorMessage("The participant "
								+ emailSearch1
								+ " has already booked. Please contact Faustine if you want to change your booking.");
				return null;
			}
		}
		log.info("Found part1: " + part1.getFullname());

		if (StringUtils.isNotEmpty(emailSearch2)) {
			Query query = pm.newQuery(Participant.class);
			query.setFilter("email == emailSearch");
			query.declareParameters("String emailSearch");
			List<Participant> partList = (List<Participant>) query
					.execute(emailSearch2);
			if (partList.isEmpty()) {
				MessageHelper
						.addErrorMessage("No participant for email "
								+ emailSearch2
								+ " found - please enter the email address that you specified during registration.");
				return null;
			}
			if (partList.size() > 1) {
				MessageHelper
						.addErrorMessage("More than one participant for email "
								+ emailSearch2
								+ " found - I cannot identify for which you want to book. Please contact Faustine to clean up the registration.");
				return null;
			}
			part2 = partList.get(0);
			part2Key = part2.getKey();
			if (StringUtils.isNotBlank(part2.getRoomType())) {
				MessageHelper
						.addErrorMessage("The participant "
								+ emailSearch2
								+ " has already booked. Please contact Faustine if you want to change your booking.");
				return null;
			}
		}

		bookingFor = part1.getFullname();
		if (part2 != null) {
			bookingFor += ", " + part2.getFullname();
		}
		log.info("bookingFor: " + bookingFor);

		pm.close();
		increment();

		return "step1";
	}

	public String getBookingFor() {
		return bookingFor;
	}

	public String choosePrivateRoom() {
		roomType = BedPlace.TYPE_PRIVATE;
		selectBedPlaces();
		return "step2";
	}

	public String chooseDormitoryBed() {
		roomType = BedPlace.TYPE_DORM_BED;
		selectBedPlaces();
		return "step2";
	}

	public String chooseMansard() {
		roomType = BedPlace.TYPE_MANSARD;
		selectBedPlaces();
		return "step2";
	}

	public String chooseTipi() {
		roomType = BedPlace.TYPE_TIPI;
		selectBedPlaces();
		return "step2";
	}

	public String chooseDormitoryCarpet() {
		roomType = BedPlace.TYPE_DORM_CARPET;
		selectBedPlaces();
		return "step2";
	}

	public String chooseHotel() {
		roomType = BedPlace.TYPE_HOTEL;
		selectBedPlaces();
		return "step2";
	}

	public String chooseCamping() {
		roomType = BedPlace.TYPE_CAMPING;
		return finishParticipantsWithoutRoom(20L);
	}

	public String chooseIndependent() {
		roomType = BedPlace.TYPE_INDEPENDENT;
		return finishParticipantsWithoutRoom(0L);
	}

	private String finishParticipantsWithoutRoom(Long amountToAdd) {
		PersistenceManager pm = getPm();
		Participant part1 = pm.getObjectById(Participant.class, part1Key);
		Long correctedAmountToAdd = amountToAdd;
		if (part1.getNumberOfDays() == 4) {
			correctedAmountToAdd = 30l;
		}
		Participant part2 = null;
		part1.setRoomType(roomType);
		part1.addAmountToPay(correctedAmountToAdd);
		if (part2Key != null) {
			correctedAmountToAdd = amountToAdd;
			part2 = pm.getObjectById(Participant.class, part2Key);
			if (part2.getNumberOfDays() == 4) {
				correctedAmountToAdd = 30l;
			}
			part2.setRoomType(roomType);
			part2.addAmountToPay(correctedAmountToAdd);
		}
		pm.close();

		sendMail(part1, null);
		if (part2 != null) {
			sendMail(part2, null);
		}
		return "end";
	}

	private void selectBedPlaces() {
		Query query = getPm().newQuery(BedPlace.class);
		query.setFilter("roomType == '" + roomType + "'");
		List<BedPlace> bedPlacesResult = (List<BedPlace>) query.execute();
		for (BedPlace bedPlace : bedPlacesResult) {
			Key participantKey = bedPlace.getParticipantKey();
			if (participantKey != null) {
				Participant participant = getPm().getObjectById(Participant.class, participantKey);
				bedPlace.setParticipant(participant);
			}
		}
		bedPlaces = new ArrayList<BedPlace>(bedPlacesResult);
	}

	public void chooseBed1(BedPlace bed) {
		bed1 = bed;
	}

	public void chooseBed2(BedPlace bed) {
		bed2 = bed;
	}

	public boolean isBed1Chosen() {
		return bed1 != null;
	}

	public boolean isBed2Chosen() {
		return bed2 != null;
	}

	public void resetChoices() {
		bed1 = null;
		bed2 = null;
		selectBedPlaces();
	}

	public boolean isPart2() {
		return part2Key != null;
	}

	public boolean isFinishable() {
		return (bed1 != null && (!isPart2() || bed2 != null));
	}

	public String end() {
		PersistenceManager pm = getPm();
		Participant part1 = pm.getObjectById(Participant.class, part1Key);
		Participant part2 = null;
		bed1 = pm.getObjectById(BedPlace.class, bed1.getKey());
		bed1.setParticipantKey(part1.getKey());
		bed1.setFree(false);
		part1.setRoomType(roomType);
		part1.setBedKey(bed1.getKey());
		part1.addAmountToPay(bed1.getCostPerPerson());
		if (part2Key != null) {
			part2 = pm.getObjectById(Participant.class, part2Key);
			bed2 = pm.getObjectById(BedPlace.class, bed2.getKey());
			bed2.setParticipantKey(part2.getKey());
			bed2.setFree(false);
			part2.setRoomType(roomType);
			part2.setBedKey(bed2.getKey());
			part2.addAmountToPay(bed2.getCostPerPerson());
		}
		pm.close();

		sendMail(part1, bed1);
		if (part2 != null) {
			sendMail(part2, bed2);
		}
		return "end";
	}

	public void sendMail(Participant part, BedPlace bed) {
		try {
			String subject = "Tango Marathon Vert (" + part.getFullname()
					+ ") --> you've booked your accommodation!";
			StringBuffer body = new StringBuffer();
			Util.appendLine(body, "Dear " + part.getFullname() + ",");
			Util.appendLine(
					body,
					"thanks for booking your accommodation. / Merci d'avoir r�serv� votre h�bergement.");
			Util.appendLine(body, "");
			Util.appendLine(body,
					"You chose the following: / Vous avez choisi:");
			Util.appendLine(body, part.getRoomType());
			Long euro = part.getAmountToPayEuro();
			if (StringUtils.equals(BedPlace.TYPE_INDEPENDENT,
					part.getRoomType())) {
				Util.appendLine(body, "");
				Util.appendLine(
						body,
						"The price (marathon only, no accommodation) is "
								+ euro
								+ " EUR per person. / Le prix du marathon est de "
								+ euro + " EUR par personne.");
			} else if (StringUtils.equals(BedPlace.TYPE_CAMPING,
					part.getRoomType())) {
				Util.appendLine(body, "");
				Util.appendLine(
						body,
						"The price (marathon and camping) is "
								+ euro
								+ " EUR per person. / The prix (marathon et camping) est de "
								+ euro + " EUR par personne.");
				Util.appendLine(body,
						"Don't forget to bring your tent etc. / N'oubliez pas tente, duvet...");
			} else {
				String bedDetail = bed.getBedDescription() + " place number "
						+ bed.getBedId();
				if (bed.isDoubleBed()) {
					bedDetail += " (" + bed.getKey().getName() + ")";
				}
				Util.appendLine(body, bedDetail);
				Util.appendLine(body, "");
				Util.appendLine(
						body,
						"The price (marathon and accommodation) is "
								+ euro
								+ " EUR per person. / Le prix (marathon et h�bergement) est de "
								+ euro + " euros par personne.");
				if (bed.isSleepingBag()) {
					Util.appendLine(
							body,
							"Don't forget to bring your camping mat and sleeping bag. / N'oubliez pas votre matelas et votre duvet.");
				}
			}

			Util.appendLine(body, "");
			Util.appendLine(body, getPaymentInformation(part));
			Util.appendLine(body, "");
			Util.appendLine(
					body,
					"Included in the marathon price are two breakfasts, two dinners, as well as fruit and snacks. / Le prix du marathon inclue 2 petits d�jeuners, deux diners, ainsi que des fruits et boissons.");
			Util.appendLine(body,
					"AUCUN REMBOURSEMENT NE SERA EFFECTUE EN CAS DE NON PRESENCE DE VOTRE PART.");
			Util.appendLine(body, "Merci et a bient�t,");
			Util.appendLine(body, "Faustine.");
			System.out.println(body.toString());
			MailService.sendMail(part.getEmail(), part.getFullname(), subject,
					body.toString(), true);
		} catch (Exception e) {
			MessageHelper.addErrorMessage("Email cannot be send: "
					+ e.getMessage());
		}

	}

	public String getPaymentInformation(Participant part) {
		Long chf = part.getAmountToPayChf();
		StringBuffer payment = new StringBuffer();
		Util.appendLine(
				payment,
				"You have time until the 31th may 2012 to pay / Date limite accept�e du paiement le 31 mai 2012.");
		Util.appendLine(
				payment,
				"Please put your name as payment reference (and your partner's name, if you pay together). / Merci d'indiquer votre nom sur la reference de paiement (ainsi que celui de votre partenaire si vous payez ensemble).");
		Util.appendLine(payment, "");
		Util.appendLine(payment, "French account / Compte fran�ais:");
		Util.appendLine(payment,
				"Bank: Soci�t� G�n�rale (33 Av Foch, 69006 Lyon)");
		Util.appendLine(payment, "IBAN: FR76 3000 3020 7000 0502 3336 126");
		Util.appendLine(payment, "BIC: SOGEFRPP");
		Util.appendLine(payment,
				"Account Holder: Faustine Boyard (4 Rue Flachet, 69100 Villeurbanne)");
		return payment.toString();
	}

	public String backToStep1() {
		resetChoices();
		return "step1";
	}

	public void increment() {
		counter++;
	}

	public int getCounter() {
		return counter;
	}

	public boolean isStays3Nights() {
		return stays3Nights;
	}

	public void setStays3Nights(boolean stays3Nights) {
		this.stays3Nights = stays3Nights;
	}
}
