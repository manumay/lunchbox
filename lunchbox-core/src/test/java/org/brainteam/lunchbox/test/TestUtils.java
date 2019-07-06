package org.brainteam.lunchbox.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

public final class TestUtils {

	private static final String DEFAULT_PACKAGE = "org.brainteam.lunchbox.";
	private static final String PACKAGE_SEPARATOR = ".";
	private static final String PATH_PREFIX = "testdata/";
	
	private TestUtils() {	
	}
	
	public static File getTestFolder(Class<?> testClass) {
		String path = getPath(testClass);
		return getTestFile(path);
	}
	
	public static File getTestFile(String filename, Class<?> testClass) {
		String path = getPath(testClass) + filename;
		return getTestFile(path);
	}
	
	protected static String getPath(Class<?> testClass) {
		String testClassName = testClass.getName();
		String path = StringUtils.removeStart(testClassName, DEFAULT_PACKAGE);
		path = StringUtils.replace(path, PACKAGE_SEPARATOR, "/");
		path = PATH_PREFIX + path;
		path += "/";
		return path;
	}
	
	public static File getTestFile(String filename) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
		if (url == null) {
			throw new RuntimeException("illegal path " + filename);
		}
		return new File(url.getPath());
	}
	
	public static URL getURL(String spec) {
		try {
			return new URL(spec);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
