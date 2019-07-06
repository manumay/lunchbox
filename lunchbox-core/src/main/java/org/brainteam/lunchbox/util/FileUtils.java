package org.brainteam.lunchbox.util;

import java.io.File;
import java.io.IOException;

public final class FileUtils {

	private FileUtils() {
	}
	
	public static void createEmptyFile(File file) {
		if (file.exists()) {
			return;
		}
		try {
			org.apache.commons.io.FileUtils.writeStringToFile(file, "");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
