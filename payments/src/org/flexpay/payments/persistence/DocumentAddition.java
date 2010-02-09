package org.flexpay.payments.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.ValueObject;

public class DocumentAddition extends ValueObject {

	private DocumentAdditionType additionType;
	private Document document;

	public DocumentAdditionType getAdditionType() {
		return additionType;
	}

	public void setAdditionType(DocumentAdditionType additionType) {
		this.additionType = additionType;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	protected ToStringBuilder buildToString(ToStringBuilder builder) {
		return builder;
	}
}
