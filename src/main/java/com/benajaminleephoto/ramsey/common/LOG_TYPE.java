package com.benajaminleephoto.ramsey.common;

public enum LOG_TYPE {
	FILE("F"), CONSOLE("C"), BOTH("B");

	private LOG_TYPE(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
