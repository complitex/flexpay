package org.flexpay.tc.process;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TariffCalcResultExportJob extends Job {

	@NonNls
	private Logger pLog = ProcessLogger.getLogger(getClass());

	private String jdbcDriverClassName;
	private String jdbcUrl;
	private String jdbcUsername;
	private String jdbcPassword;
	private String procedure; 
	private Long dataSourceDescriptionId;
	private LockManager lockManager;
	private ClassToTypeRegistry classToTypeRegistry;
	private CorrectionsService correctionsService;
	private TariffCalculationResultService tariffCalculationResultService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		pLog.debug("Tariff calculation result export procces started");

		if (!lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
			log.info("Another process has already requested a lock and is working");
			return RESULT_NEXT;
		}

		Connection conn = null;
		try {
			conn = getConnection();
			pLog.info("Connection with DB (url - {}) create successfully", jdbcUrl);
			conn.setAutoCommit(false);

			Date calcDate = (Date) parameters.get("date");
			java.sql.Date sqlCalcDate = new java.sql.Date(calcDate.getTime());

			List<Long> addressIds = tariffCalculationResultService.getAddressIds(calcDate);

			for (Long addressId : addressIds) {

				String externalId = correctionsService.getExternalId(addressId, classToTypeRegistry.getType(BuildingAddress.class), dataSourceDescriptionId);
				if (externalId == null) {
					pLog.debug("ExternalId for building address with id={} not found", addressId);
					continue;
				}

				Integer intExtId = Integer.parseInt(externalId);

				List<TariffCalculationResult> tcrs = tariffCalculationResultService.getTariffCalcResultsByCalcDateAndAddressId(calcDate, addressId);

				try {

					for (TariffCalculationResult tcr : tcrs) {

						CallableStatement cs = conn.prepareCall(procedure);
						try {
							cs.registerOutParameter(1, Types.INTEGER);
							cs.setInt(2, intExtId);
							cs.setString(3, tcr.getTariff().getSubServiceCode());
							cs.setBigDecimal(4, tcr.getValue());
							cs.setDate(5, sqlCalcDate);
							cs.executeUpdate();

							int exportResult = cs.getInt(1);

							if (exportResult == 0) {
								pLog.warn("Tariff {} for building with id={} not exists", tcr.getTariff(), tcr.getBuilding().getId());
							} else if (exportResult == -1) {
								pLog.warn("Building with id={} for caluculation result {} not found", tcr.getBuilding().getId(), tcr);
							} else if (exportResult == -2) {
								pLog.warn("Error: Can't create record in history for calculation result {}", tcr);
							} else if (exportResult == -3) {
								pLog.warn("Locking exception: Can't export calculcation result {}", tcr);
							} else {
								pLog.debug("Tariff calculation result {} exported succesfully", tcr);
							}
						} finally {
							cs.close();
						}
					}


					conn.commit();
				} catch (Exception e) {
					pLog.error("SQL error for adressId=" + addressId, e);
					try {
						conn.rollback();
					} catch (SQLException e1) {
						// do nothing
					}
				}

			}

		} catch (Exception e) {
			pLog.error("SQL error", e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// do nothing
			}
		}

		lockManager.releaseLock(Resources.BUILDING_ATTRIBUTES);

		pLog.debug("Tariff calculation result export procces finished");

		return RESULT_NEXT;
	}

	private Connection getConnection() throws Exception {
		Class.forName(jdbcDriverClassName);
		return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
	}

	@Required
	public void setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}

	@Required
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	@Required
	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	@Required
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	@Required
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	@Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

	@Required
	public void setDataSourceDescriptionId(Long dataSourceDescriptionId) {
		this.dataSourceDescriptionId = dataSourceDescriptionId;
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

}
