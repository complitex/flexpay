package org.flexpay.eirc.persistence.exchange.conditions;

public class ConditionsFactory {

	private AccountPersonChangeCondition accountPersonChangeCondition;

	public AccountPersonChangeCondition getAccountPersonChangeCondition() {
		return accountPersonChangeCondition;
	}

	public void setAccountPersonChangeCondition(AccountPersonChangeCondition accountPersonChangeCondition) {
		this.accountPersonChangeCondition = accountPersonChangeCondition;
	}
}
