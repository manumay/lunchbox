package org.brainteam.lunchbox.services;

import java.util.Date;

import org.brainteam.lunchbox.json.JsonJournal;
import org.brainteam.lunchbox.json.JsonPage;

public interface JournalService {
	
	void add(String key, String[] params);
	
	JsonPage<JsonJournal> getJson(int page, int size, String sort, String order, Date date);

}
