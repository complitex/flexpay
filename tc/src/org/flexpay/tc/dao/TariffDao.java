package org.flexpay.tc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.tc.persistence.Tariff;

import java.util.List;

public interface TariffDao extends GenericDao<Tariff, Long> {

	List<Tariff> listTariffs();
}
