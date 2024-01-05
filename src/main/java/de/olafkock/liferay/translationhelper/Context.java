package de.olafkock.liferay.translationhelper;

public class Context {
	private String[] context;
	private StackTraceElement[] stacktrace;

	public Context(String [] context) {
		this.context = context;
		this.stacktrace = Thread.currentThread().getStackTrace();
	}
	
	public String[] getContext() {
		return context;
	}
	
	public StackTraceElement[] getStacktrace() {
		return stacktrace;
	}
}
