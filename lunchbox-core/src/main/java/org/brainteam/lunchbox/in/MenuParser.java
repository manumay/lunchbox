package org.brainteam.lunchbox.in;

import java.io.File;
import java.io.IOException;

public interface MenuParser {

	OffersDefinition parse(File file) throws IOException;
	
}
