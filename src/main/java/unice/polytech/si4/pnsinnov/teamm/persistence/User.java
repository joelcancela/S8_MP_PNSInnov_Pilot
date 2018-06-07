package unice.polytech.si4.pnsinnov.teamm.persistence;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userId;
    private List<String> rules;

    private User(UserBuilder builder) {
        userId = builder.userId;
        rules = builder.rules;
    }

    public static class UserBuilder {

        String userId;
        List <String> rules;

        public UserBuilder() {
            userId = "";
            rules = new ArrayList <>();
        }

        public UserBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder setRules(List <String> rules) {
            this.rules = rules;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }

    public String getUserId() {
        return userId;
    }

    public List<String> getRules() {
        return rules;
    }
}