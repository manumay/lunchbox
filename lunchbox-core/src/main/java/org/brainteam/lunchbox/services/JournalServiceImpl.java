package org.brainteam.lunchbox.services;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.brainteam.lunchbox.core.Clock;
import org.brainteam.lunchbox.dao.JournalRepository;
import org.brainteam.lunchbox.domain.Journal;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.i18n.Translator;
import org.brainteam.lunchbox.json.JsonJournal;
import org.brainteam.lunchbox.json.JsonOption;
import org.brainteam.lunchbox.json.JsonPage;
import org.brainteam.lunchbox.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JournalServiceImpl implements JournalService {
	
	private static final char SEPARATOR = ';';
	
	@Autowired
	private Translator translator;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JournalRepository journalRepository;
	
	@Autowired
	private Clock clock;
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('user')")
	public void add(String key, String[] params) {
		Journal journal = new Journal();
		journal.setModificationDate(getClock().now());
		journal.setModifier(getUserService().currentModifier());
		journal.setParams(StringUtils.join(params, SEPARATOR));
		journal.setTextKey(key);
		getJournalRepository().save(journal);
	}

	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public JsonPage<JsonJournal> getJson(int page, int size, String sort, String order, Date date) {		
		Date from = DateUtils.getStartOfDay(date);
		Date till = DateUtils.getEndOfDay(date);
		JsonPage<JsonJournal> json = new JsonPage<>();
		PageRequest pr = new PageRequest(page, size, new Sort(new Order(Direction.fromString(order), sort)));
		Page<Journal> journals = getJournalRepository().findByFromAndTillOrderByModificationDate(from, till, pr);
		for (Journal journal : journals) {
			json.addToItems(toJsonJournalItem(journal));
		}
		json.setPage(journals.getNumber() + 1);
		json.setSize(journals.getSize());
		json.setNumberOfItems(journals.getNumberOfElements());
		json.setSort(sort);
		json.setOrder(order);
		json.setTotalPages(journals.getTotalPages());
		json.setTotalItems(journals.getTotalElements());
		return json;
	}
	
	protected JsonJournal toJsonJournalItem(Journal journal) {
		JsonJournal item = new JsonJournal();
		item.setTimestamp(journal.getModificationDate());
		item.setUser(toJsonOption(journal.getModifier()));
		item.setText(getTranslator().t(journal.getTextKey(), (Object[])StringUtils.split(journal.getParams(), ';')));
		return item;
	}
	
	protected JsonOption toJsonOption(User user) {
		JsonOption json = new JsonOption();
		json.setText(user.getFullName());
		json.setValue(user.getId());
		return json;
	}
	
	protected JournalRepository getJournalRepository() {
		return journalRepository;
	}
	
	protected Translator getTranslator() {
		return translator;
	}
	
	protected UserService getUserService() {
		return userService;
	}
	
	protected Clock getClock() {
		return clock;
	}
	
}
