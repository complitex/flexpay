package org.flexpay.tc.process;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffCalcRegulationService;
import org.flexpay.tc.persistence.TariffCalcRegulation;
import org.flexpay.ab.service.BuildingService;
import org.slf4j.Logger;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.DroolsParserException;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.rule.Package;

import java.io.*;
import java.util.Map;

public class ProceessTariffCalculationJob extends Job {

	private TariffCalculationResultService tariffCalculationResultService;
	private BuildingService buildingService;
	private TariffCalcRegulationService tariffCalcRegulationService;

	public static final String RULES_ID= "RULE_ID";

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		Logger processLogger = ProcessLogger.getLogger(getClass());

		//get rules file

		TariffCalcRegulation tariffCalcRegulation =  tariffCalcRegulationService.read(new Stub<TariffCalcRegulation>(Long.valueOf((String)parameters.get(RULES_ID))));

		if (tariffCalcRegulation == null){
			processLogger.error("Tariff calculation rules for id {} not found. Exiting.", parameters.get(RULES_ID));
			return RESULT_ERROR;
		}

		//initialize rules
		RuleBase ruleBase;
		try {
			Reader source = new InputStreamReader(new FileInputStream(tariffCalcRegulation.getFile().getFile()));
			PackageBuilder builder = new PackageBuilder();
			builder.addPackageFromDrl( source );
			org.drools.rule.Package pkg = builder.getPackage();
			ruleBase = RuleBaseFactory.newRuleBase();
			ruleBase.addPackage( pkg );
		} catch (FileNotFoundException ex){
			processLogger.error("Rules file not found.", ex);
			return RESULT_ERROR;
		} catch (IOException ex){
			processLogger.error("Can't read rules file.", ex);
			return RESULT_ERROR;
		} catch (DroolsParserException ex){
			processLogger.error("Can't parse rules.", ex);
			return RESULT_ERROR;
		} catch (Exception ex){
			processLogger.error("Drools internal error.", ex);
			return RESULT_ERROR;
		}


		//get building list
//		buildingService.
//		for (){
//			WorkingMemory workingMemory = ruleBase.newStatefulSession();
//			//process calculations for each building
//			//log % to log file
//		}
		//log errors
		return RESULT_NEXT;
	}

	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	public void setTariffCalcRegulationService(TariffCalcRegulationService tariffCalcRegulationService) {
		this.tariffCalcRegulationService = tariffCalcRegulationService;
	}
}
