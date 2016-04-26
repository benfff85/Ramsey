package com.benajaminleephoto.ramsey.common;

public enum MUTATION_TYPE {
	RANDOM("R"), TARGETED("T"), BALANCED("B");

	private MUTATION_TYPE(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
