package org.flexpay.tc.process.exporters;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.JDBCUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.TariffExportCode;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffExportCodeServiceExt;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
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

	/**
	 * Begin export procedure
	 * @throws FlexPayException throws flexpay exception when filed to begin export process
	 */
	public void beginExport() throws FlexPayException {
		try {
			Class.forName(jdbcDriverClassName);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
			conn.setAutoCommit(false);
		}catch(ClassNotFoundException e){
			FlexPayException fe = new FlexPayException("Can't load driver for JDBC connection",e);
			log.error(fe.getMessage(),e);
			throw fe;
		}catch(SQLException e){
			FlexPayException fe = new FlexPayException("Can't accure connection",e);
			log.error(fe.getMessage(),e);
			throw fe;
		}
	}

	/**
	 * Export parameters
	 * @param params params to export
	 * @throws FlexPayException throws flexpay exception when can't export data
	 */
	public void export(@NotNull Object[] params) throws FlexPayException {

		try {
			TariffCalculationResult tariffCalculationResult = (TariffCalculationResult) params[0];
			Integer externalId = Integer.parseInt(getExternalId(new Stub<Building>(tariffCalculationResult.getBuilding().getId())));
			Date periodBeginDate = new Date(((java.util.Date) params[2]).getTime());
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

				if (exportResult == 0) {
					log.warn("Tariff {} for building with id={} not exists", tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId());
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.EXPORTED);
				} else if (exportResult == -1) {
					log.warn("Building with id={} for caluculation result {} not found", tariffCalculationResult.getBuilding().getId(), tariffCalculationResult);
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.BUILDING_NOT_FOUND);
				} else if (exportResult == -2) {
					log.warn("Error: Can't create record in history for calculation result {}", tariffCalculationResult);
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.CANNOT_CREATE_HISTORY_RECORD);
				} else if (exportResult == -3) {
					log.warn("Locking exception: Can't export calculcation result {}", tariffCalculationResult);
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.LOCK_EXCEPTION);
				} else if (exportResult == -4) {
					log.warn("Tariff value is negative {}", tariffCalculationResult);
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.NEGATIVE_VALUE);
				} else if (exportResult == -5) {
					log.warn("Tariff begin date is null {}", tariffCalculationResult);
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.BEGIN_DATE_IS_NULL);
				} else {
					log.debug("Tariff calculation result {} exported succesfully", tariffCalculationResult);
					tariffExportCode = tariffExportCodeServiceExt.findByCode(TariffExportCode.UNKNOWN_RESULT_CODE);
				}
				if (tariffCalculationResult.getId() != null){
					tariffCalculationResult.setTariffExportCode(tariffExportCode);
					tariffCalculationResultService.update(tariffCalculationResult);
				}
			} finally {
				cs.close();
			}
		} catch (SQLException e) {
			FlexPayException fe = new FlexPayException("Exception accured",e);
			log.error(fe.getMessage(),e);
			throw fe;
		}

	}

	private String getExternalId(@NotNull Stub<Building> buildingStub) throws FlexPayException {

		Stub<DataSourceDescription> dataSourceDescriptionStub = new Stub<DataSourceDescription>(dataSourceDescriptionId);

		List<BuildingAddress> buildingAddressList = buildingService.getBuildingBuildings(buildingStub);
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
	 * @throws FlexPayException throws flexpay exception when filed to commit transaction
	 */
	public void commit() throws FlexPayException {
		try {
			conn.commit();
		} catch (SQLException e) {
			FlexPayException fe = new FlexPayException("Can't commit transaction",e);
			log.error(fe.getMessage(),e);
			throw fe;
		}
	}

	/**
	 * Rollback transaction
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          throws FlexPayException when can't rollback transaction
	 */
	public void rollback() throws FlexPayException {
		try{
			conn.rollback();
		}catch(SQLException e){
			FlexPayException fe = new FlexPayException("Can't rollback tarnsaction",e);
			log.error(fe.getMessage(),e);
			throw fe;
		}
	}

	/**
	 * End export procedure
	 */
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
}
