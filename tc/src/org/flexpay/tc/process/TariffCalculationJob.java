package org.flexpay.tc.process;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NonNls;

import java.io.*;
import java.util.Map;

public class TariffCalculationJob extends Job {

	@NonNls
	private Logger pLog = ProcessLogger.getLogger(getClass());

	private TariffCalculationResultService tariffCalculationResultService;
	private BuildingService buildingService;
	private TariffCalculationRulesFileService tariffCalculationRulesFileService;

	public static final String RULES_ID = "RULE_ID";

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		pLog.debug("Tariff calculation procces started");

		//get rules file

		TariffCalculationRulesFile rulesFile =
				tariffCalculationRulesFileService.read(new Stub<TariffCalculationRulesFile>(Long.valueOf((String) parameters.get(RULES_ID))));

		if (rulesFile == null){
			pLog.error("Tariff calculation rules for id {} not found. Exiting.", parameters.get(RULES_ID));
			return RESULT_ERROR;
		}

		//initialize rules
		RuleBase ruleBase;
		try {
			Reader source = new InputStreamReader(new FileInputStream(rulesFile.getFile().getFile()));
			PackageBuilder builder = new PackageBuilder();
			builder.addPackageFromDrl(source);
			org.drools.rule.Package pkg = builder.getPackage();
			ruleBase = RuleBaseFactory.newRuleBase();
			ruleBase.addPackage(pkg);
		} catch (FileNotFoundException ex){
			pLog.error("Rules file not found", ex);
			return RESULT_ERROR;
		} catch (IOException ex){
			pLog.error("Can't read rules file", ex);
			return RESULT_ERROR;
		} catch (DroolsParserException ex){
			pLog.error("Can't parse rules", ex);
			return RESULT_ERROR;
		} catch (Exception ex){
			pLog.error("Drools internal error", ex);
			return RESULT_ERROR;
		}


		pLog.debug("Tariff calculation procces finished");

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

	@Required
	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setTariffCalculationRulesFileService(TariffCalculationRulesFileService tariffCalculationRulesFileService) {
		this.tariffCalculationRulesFileService = tariffCalculationRulesFileService;
	}

}
