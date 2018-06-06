package unice.polytech.si4.pnsinnov.teamm.drools;

/**
 * Different types of rules, depending on the filtering criteria
 */
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
