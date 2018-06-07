package unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence;

import java.util.ArrayList;
import java.util.List;

public class RuleSet {

	private String userId;
	private List<String> rules;

	private RuleSet(RuleSetBuilder builder) {
		userId = builder.userId;
		rules = builder.rules;
	}

	public static class RuleSetBuilder {

		String userId;
		List<String> rules;

		public RuleSetBuilder() {
			userId = "";
			rules = new ArrayList<>();
		}

		public RuleSetBuilder setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public RuleSetBuilder setRules(List<String> rules) {
			this.rules = rules;
			return this;
		}

		public RuleSet build() {
			return new RuleSet(this);
		}

	}

	public String getUserId() {
		return userId;
	}

	public List<String> getRules() {
		return rules;
	}
}