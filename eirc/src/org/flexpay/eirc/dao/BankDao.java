package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.BankDescription;

import java.util.List;

public interface BankDao extends OrganisationInstanceDao<BankDescription, Bank> {

}
