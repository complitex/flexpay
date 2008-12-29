package org.flexpay.ab.persistence;

public enum SyncAction {

	Unknown(-1), Create(0), Change(1), Delete(2);

	private int code;

	private SyncAction(int code) {
		this.code = code;
	}

	public static SyncAction getByCode(int code) {
		if (code == Create.code) {
			return Create;
		}
		if (code == Change.code) {
			return Change;
		}
		if (code == Delete.code) {
			return Delete;
		}

		throw new IllegalArgumentException("Unsupported sync operation type code: " + code);
	}

	public int getCode() {
		return code;
	}
}
