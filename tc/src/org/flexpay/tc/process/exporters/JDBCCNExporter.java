package org.flexpay.tc.process.exporters;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.util.JDBCUtils;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang.time.FastDateFormat;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class JDBCCNExporter implements Exporter {

	private Connection conn;
	private String jdbcDriverClassName;
	private String jdbcUrl;
	private String jdbcUsername;
	private String jdbcPassword;
	private String procedure;
	private Logger log = LoggerFactory.getLogger(getClass());

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
//		Logger pLog = ProcessLogger.getLogger(getClass());

		try {
			TariffCalculationResult tariffCalculationResult = (TariffCalculationResult) params[0];
			Integer externalId = Integer.parseInt((String) params[1]);

			CallableStatement cs = conn.prepareCall(procedure);
			try {
				cs.registerOutParameter(1, Types.INTEGER);
				cs.setInt(2, externalId);
				cs.setString(3, tariffCalculationResult.getTariff().getSubServiceCode());
				cs.setBigDecimal(4, tariffCalculationResult.getValue());
//				cs.setDate(5, new java.sql.Date(tariffCalculationResult.getCalculationDate().getTime()));
				cs.setDate(5, new java.sql.Date((new SimpleDateFormat("yyyy/MM/dd")).parse("2009/02/01").getTime()));
				cs.executeUpdate();

				int exportResult = cs.getInt(1);

				if (exportResult == 0) {
					log.warn("Tariff {} for building with id={} not exists", tariffCalculationResult.getTariff(), tariffCalculationResult.getBuilding().getId());
				} else if (exportResult == -1) {
					log.warn("Building with id={} for caluculation result {} not found", tariffCalculationResult.getBuilding().getId(), tariffCalculationResult);
				} else if (exportResult == -2) {
					log.warn("Error: Can't create record in history for calculation result {}", tariffCalculationResult);
				} else if (exportResult == -3) {
					log.warn("Locking exception: Can't export calculcation result {}", tariffCalculationResult);
				} else {
					log.debug("Tariff calculation result {} exported succesfully", tariffCalculationResult);
				}
			} catch(ParseException e){
				FlexPayException fe = new FlexPayException("Can't parse export date",e);
				log.error(fe.getMessage(),e);
				throw fe;
			} finally {
				cs.close();
			}
		} catch (SQLException e) {
			FlexPayException fe = new FlexPayException("Exception accured",e);
			log.error(fe.getMessage(),e);
			throw fe;
		}

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
	 * @throws FlexPayException throws flexpay exception when filed to end export process
	 */
	public void endExport() {
		JDBCUtils.closeQuitly(conn);
	}

	public void setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
}
