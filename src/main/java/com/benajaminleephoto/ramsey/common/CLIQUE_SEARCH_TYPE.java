package com.benajaminleephoto.ramsey.common;

public enum CLIQUE_SEARCH_TYPE {
	ALL("A"), FIRST("F"),;

	private CLIQUE_SEARCH_TYPE(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
