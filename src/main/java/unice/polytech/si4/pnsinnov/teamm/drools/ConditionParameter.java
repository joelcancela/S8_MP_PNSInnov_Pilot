package unice.polytech.si4.pnsinnov.teamm.drools;

public enum ConditionParameter {
    EXTENSION("extension"),
    MIME_TYPE("mimeType"),
    REGEX("regex");

    public String parameterString;

    ConditionParameter(String parameterString) {
        this.parameterString = parameterString;
    }
}
