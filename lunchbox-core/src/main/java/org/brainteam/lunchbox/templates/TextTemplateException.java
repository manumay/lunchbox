package org.brainteam.lunchbox.templates;

public class TextTemplateException extends Exception {

	private static final long serialVersionUID = 3378549353983742912L;
	
	public TextTemplateException(String msg) {
		super(msg);
	}
	
	public TextTemplateException(Throwable t) {
		super(t);
	}

}
