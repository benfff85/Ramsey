package com.benajaminleephoto.ramsey.common;

public enum DIRECTION {
    LEFT("L"), RIGHT("R");

    private DIRECTION(final String text) {
        this.text = text;
    }

    private final String text;


    @Override
    public String toString() {
        return text;
    }
}
