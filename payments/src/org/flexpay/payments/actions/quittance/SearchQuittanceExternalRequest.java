package org.flexpay.payments.actions.quittance;

public class SearchQuittanceExternalRequest {

	private String login;
	private String signature;
	private GetDebtInfo debtInfoData;// = new GetDebtInfo();

	public static class GetDebtInfo {

		private String requestId;
		private String searchType; // TODO int?
		private String searchCriteria;

		public GetDebtInfo() {
		}

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		public String getSearchType() {
			return searchType;
		}

		public void setSearchType(String searchType) {
			this.searchType = searchType;
		}

		public String getSearchCriteria() {
			return searchCriteria;
		}

		public void setSearchCriteria(String searchCriteria) {
			this.searchCriteria = searchCriteria;
		}
	}

	public SearchQuittanceExternalRequest() {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public GetDebtInfo getDebtInfoData() {
		return debtInfoData;
	}

	public void setDebtInfoData(GetDebtInfo debtInfoData) {
		this.debtInfoData = debtInfoData;
	}
}
