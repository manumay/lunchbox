package org.brainteam.lunchbox.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class DirectoriesImpl implements Directories  {

	@Override
	public File getConfigDir() {
		File file = new File(getBaseDir(), "config");
		if (file.mkdirs()) {
			System.out.println("created temp directory");
		}
		return file;
	}

	@Override
	public File getDataDir() {
		return getDatabaseDir();
	}
	
	@Override
	public File getTempDir() {
		File file = new File(getBaseDir(), "temp");
		if (file.mkdirs()) {
			System.out.println("created temp directory");
		}
		return file;
	}

	@Override
	public File getTemplatesDir() {
		File file = new File(getConfigDir(), "templates");
		if (file.mkdirs()) {
			System.out.println("created templates directory");
		}
		return file;
	}
	
	public File getBackupBaseDir() {
		File file = new File(getBaseDir(), "backups");
		if (file.mkdirs()) {
			System.out.println("created backups directory");
		}
		return file;
	}
	
	@Override
	public File getBackupDir() {
		File file = new File(getBackupBaseDir(), new SimpleDateFormat("yyyy-MM-dd-hh-mm-ssss").format(new Date()));
		if (file.mkdirs()) {
			System.out.println("created backup directory");
		}
		return file;
	}
	
	@Override
	public File getPluginDir() {
		File file = new File(getBaseDir(), "plugins");
		if (file.mkdirs()) {
			System.out.println("created plugins directory");
		}
		return file;
	}
	
	@Override
	public File getPluginCacheDir() {
		File file = new File(getPluginDir(), "cache");
		if (file.mkdirs()) {
			System.out.println("created plugincache directory");
		}
		return file;
	}
	
	public static File getDatabaseDir() {
		File file = new File(getBaseDir(), "data");
		if (file.mkdirs()) {
			System.out.println("created data directory");
		}
		return file;
	}
	
	public static File getBaseDir() {
		return new File(FileUtils.getUserDirectory(), ".lunchbox-" + getVersion());
	}
	
	private static String getVersion() {
		return Version.get();
	}

	
}
