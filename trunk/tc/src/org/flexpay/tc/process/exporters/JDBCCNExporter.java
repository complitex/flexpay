package org.flexpay.tc.process.exporters;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.JDBCUtils;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.TariffExportCode;
import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffExportCodeServiceExt;
import org.flexpay.tc.service.TariffExportLogRecordService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.sql.*;
import java.util.List;

public class JDBCCNExporter implements Exporter {

	private Connection conn;
	private String jdbcDriverClassName;
	private String jdbcUrl;
	private String jdbcUsername;
	private String jdbcPassword;
	private String procedure;
	private TariffCalculationResultService tariffCalculationResultService;
	private BuildingService buildingService;
	private ClassToTypeRegistry classToTypeRegistry;
	private CorrectionsService correctionsService;
	private Long dataSourceDescriptionId;
	private Logger log = LoggerFactory.getLogger(getClass());
	private TariffExportCodeServiceExt tariffExportCodeServiceExt;
	private TariffExportLogRecordService tariffExportLogRecordService;

	/**
	 * Begin export procedure
	 *
	 * @throws FlexPayException throws flexpay exception when filed to begin export process
	 */
	@Override
	public void beginExport() throws FlexPayException {
		try {
			Class.forName(jdbcDriverClassName);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			FlexPayException fe = new FlexPayException("Can't load driver for JDBC connection", e);
			log.error(fe.getMessage(), e);
			throw fe;
		} catch (SQLException e) {
			FlexPayException fe = new FlexPayException("Can't accure connection", e);
			log.error(fe.getMessage(), e);
			throw fe;
		}
	}

	/**
	 * Export parameters
	 *
	 * @param params params to export
	 * @throws FlexPayException throws flexpay exception when can't export data
	 */
	@Override
	public void export(@NotNull Object[] params) throws FlexPayException {
		Logger plog = ProcessLogger.getLogger(getClass());
		try {
			TariffCalculationResult tariffCalculationResult = (TariffCalculationResult) params[0];
			Integer externalId = Integer.parseInt(getExternalId(new Stub<Building>(tariffCalculationResult.getBuilding().getId())));
			Date periodBeginDate = new Date(((java.util.Date) params[1]).getTime());
			CallableStatement cs = conn.prepareCall(procedure);
			try {
				cs.registerOutParameter(1, Types.INTEGER);
				cs.setInt(2, externalId);
				cs.setString(3, tariffCalculationResult.getTariff().getSubServiceCode());
				cs.setBigDecimal(4, tariffCalculationResult.getValue());
				cs.setDate(5, periodBeginDate);
				cs.executeUpdate();

				int exportResult = cs.getInt(1);

				TariffExportCode tariffExportCode;

				if (exportResult == 1) {
                    plog.info("Tariff calculation result {} for building id = {} and external id = {} exported succesfully", new Object[]{tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId(), externalId});
                    tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.EXPORTED);
				} else if (exportResult == 0) {
					plog.info("Tariff {} for building with id={} and external id ={} is not exists", new Object[]{tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId(), externalId});
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.TARIFF_NOT_FOUND_FOR_BUILDING);
				} else if (exportResult == -1) {
					plog.info("Building with id={} and external id = {} for caluculation result {} not found", new Object[]{tariffCalculationResult.getBuilding().getId(), externalId, tariffCalculationResult.getTariff()});
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.BUILDING_NOT_FOUND);
				} else if (exportResult == -2) {
					plog.info("Can't create record in history for calculation result {} building id = {} and external id = {}", new Object[]{tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId(), externalId});
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.CANNOT_CREATE_HISTORY_RECORD);
				} else if (exportResult == -3) {
					plog.info("Null not null value for tariff {} and building id ={} and external id ={}", new Object[]{tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId(), externalId});
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.NULL_NOT_NULL_TARIFF_VALUE);
				} else if (exportResult == -4) {
					plog.info("Tariff value is negative {} for building id = {} and external id = {}", new Object[]{tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId(), externalId});
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.NEGATIVE_VALUE);
				} else if (exportResult == -5) {
					plog.info("Tariff begin date is null {} for building id = {} and external id = {}", new Object[]{tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId(), externalId});
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.BEGIN_DATE_IS_NULL);
				} else if (exportResult == -6) {
					plog.info("Tariff  {} not found for building while adding not null tariff value for building id = {} and external id = {}", new Object[]{tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId(), externalId});
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.TARIFF_NOT_FOUND_FOR_BUILDING_WHILE_ADDING_NOT_NULL_TARIFF_VALUE);
				} else {
					plog.info("Tariff calculation result {} for building id = {} and external id = {} export return unknown code {}", new Object[]{tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId(), externalId, exportResult});
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.UNKNOWN_RESULT_CODE);
				}
				TariffExportLogRecord tariffExportLogRecord = new TariffExportLogRecord();
				tariffExportLogRecord.setBuilding(tariffCalculationResult.getBuilding());
				tariffExportLogRecord.setTariff(tariffCalculationResult.getTariff());
				tariffExportLogRecord.setTariffBeginDate(periodBeginDate);
				tariffExportLogRecord.setTariffExportCode(tariffExportCode);
				tariffExportLogRecord.setExportdate(new java.util.Date());
				tariffExportLogRecordService.create(tariffExportLogRecord);
				if (tariffCalculationResult.getId() != null) {
					tariffCalculationResult.setLastTariffExportLogRecord(tariffExportLogRecord);
					tariffCalculationResultService.update(tariffCalculationResult);
				}
			} finally {
				cs.close();
			}
		} catch (SQLException e) {
			FlexPayException fe = new FlexPayException("Exception accured", e);
			log.error(fe.getMessage(), e);
			throw fe;
		}

	}

	private String getExternalId(@NotNull Stub<Building> buildingStub) throws FlexPayException {

		Stub<DataSourceDescription> dataSourceDescriptionStub = new Stub<DataSourceDescription>(dataSourceDescriptionId);

		List<BuildingAddress> buildingAddressList = buildingService.findAddresesByBuilding(buildingStub);
		for (BuildingAddress buildingAddress : buildingAddressList) {
			String externalId = correctionsService.getExternalId(buildingAddress.getId(),
					classToTypeRegistry.getType(BuildingAddress.class), dataSourceDescriptionStub);
			if (externalId != null) {
				return externalId;
			}
		}
		throw new FlexPayException("Building address not found for building.id=" + buildingStub.getId());
	}

	/**
	 * Commit transaction
	 *
	 * @throws FlexPayException throws flexpay exception when filed to commit transaction
	 */
	@Override
	public void commit() throws FlexPayException {
		try {
			conn.commit();
		} catch (SQLException e) {
			FlexPayException fe = new FlexPayException("Can't commit transaction", e);
			log.error(fe.getMessage(), e);
			throw fe;
		}
	}

	/**
	 * Rollback transaction
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          throws FlexPayException when can't rollback transaction
	 */
	@Override
	public void rollback() throws FlexPayException {
		try {
			conn.rollback();
		} catch (SQLException e) {
			FlexPayException fe = new FlexPayException("Can't rollback tarnsaction", e);
			log.error(fe.getMessage(), e);
			throw fe;
		}
	}

	/**
	 * End export procedure
	 */
	@Override
	public void endExport() {
		JDBCUtils.closeQuitly(conn);
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
	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

	@Required
	public void setTariffExportCodeServiceExt(TariffExportCodeServiceExt tariffExportCodeServiceExt) {
		this.tariffExportCodeServiceExt = tariffExportCodeServiceExt;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setDataSourceDescriptionId(Long dataSourceDescriptionId) {
		this.dataSourceDescriptionId = dataSourceDescriptionId;
	}

	@Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Required
	public void setTariffExportLogRecordService(TariffExportLogRecordService tariffExportLogRecordService) {
		this.tariffExportLogRecordService = tariffExportLogRecordService;
	}

}
