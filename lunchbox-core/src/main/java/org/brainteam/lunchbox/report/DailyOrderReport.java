package org.brainteam.lunchbox.report;

import java.io.OutputStream;

import org.brainteam.lunchbox.json.JsonOrdersDaily;

public interface DailyOrderReport {

	void write(OutputStream out, JsonOrdersDaily json);
	
}
