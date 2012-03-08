package com.joerg.test;

import com.joerg.model.Participant;

public class Tests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Participant p = new Participant();
		p.setAmountToPayEuro(65L);
		System.out.println(p.getAmountToPayChf());

	}

}
