package unice.polytech.si4.pnsinnov.teamm.drools;

public enum ConditionParameter {
    EXTENSION("extension"),
    MIME_TYPE("mimeType"),
    REGEX_START("regex"),
    REGEX_END("regex"),
    REGEX_CONTAINS("regex");

    public String parameterString;

    ConditionParameter(String parameterString) {
        this.parameterString = parameterString;
    }
}
