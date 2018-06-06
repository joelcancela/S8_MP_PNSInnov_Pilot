package unice.polytech.si4.pnsinnov.teamm.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    private String userId;
    private List<String> rules;

    public User() {
    }

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
}