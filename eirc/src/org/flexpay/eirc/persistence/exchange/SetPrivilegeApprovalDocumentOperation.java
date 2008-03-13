package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 */
public abstract class SetPrivilegeApprovalDocumentOperation extends AbstractChangePersonalAccountOperation {

	public SetPrivilegeApprovalDocumentOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}