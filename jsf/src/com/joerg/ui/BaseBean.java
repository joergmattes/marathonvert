package com.joerg.ui;

import java.io.Serializable;

import javax.jdo.PersistenceManager;

import com.joerg.persistance.PMF;

public class BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public PersistenceManager getPm() {
		return PMF.get().getPersistenceManager();
	}

}
