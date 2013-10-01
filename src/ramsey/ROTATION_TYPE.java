package ramsey;

public enum ROTATION_TYPE {
    SERIAL("S"),
    PARALLEL("P")
    ;
    
    private ROTATION_TYPE(final String text) {
        this.text = text;
    }

    private final String text;

    @Override
    public String toString() {
        return text;
    }
}
