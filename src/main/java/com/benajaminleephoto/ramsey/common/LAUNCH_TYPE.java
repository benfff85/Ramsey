package com.benajaminleephoto.ramsey.common;

public enum LAUNCH_TYPE {
	OPEN_FROM_FILE("F"), GENERATE_RANDOM("R");

	private LAUNCH_TYPE(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
