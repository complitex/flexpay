package org.flexpay.tc.process;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.flexpay.tc.service.TariffServiceExt;
import org.flexpay.tc.util.config.ApplicationConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TariffCalculationJob extends Job {

	public static final String RULES_ID = "RULE_ID";
	public static final String CALC_DATE = "CALC_DATE";

	private LockManager lockManager;
	private BtiBuildingService btiBuildingService;
	private TariffCalculationResultService tariffCalculationResultService;
	private TariffCalculationRulesFileService tariffCalculationRulesFileService;
	private BuildingAttributeTypeService buildingAttributeTypeService;
	private TariffServiceExt tariffServiceExt;

	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Logger pLogger = ProcessLogger.getLogger(getClass());

		if (lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
			//@todo clear all calculated tariff to CALC_DATE
			Date date = new Date();
			Date calculationDate = (Date)parameters.get(CALC_DATE);
			//get rules file
			try {
				TariffCalculationRulesFile rulesFile = tariffCalculationRulesFileService.read(new Stub<TariffCalculationRulesFile>((Long) parameters.get(RULES_ID)));
				if (rulesFile == null) {
					pLogger.error("Tariff calculation rules for id {} not found. Exiting.", parameters.get(RULES_ID));
					return RESULT_ERROR;
				}

				//initialize rules
				RuleBase ruleBase;
				Reader source = null;
				InputStream fis = null;
				try {
					fis = new FileInputStream(rulesFile.getFile().getFile());
					source = new InputStreamReader(fis);
					PackageBuilder builder = new PackageBuilder();
					builder.addPackageFromDrl(source);
					org.drools.rule.Package pkg = builder.getPackage();
					ruleBase = RuleBaseFactory.newRuleBase();
					ruleBase.addPackage(pkg);
					List<BtiBuilding> btiBuildingList = btiBuildingService.findByTown(ApplicationConfig.getDefaultTownStub());
					pLogger.info("Found {} buildings", btiBuildingList.size());
					int cnt = 0;

					for (BtiBuilding btiBuilding : btiBuildingList) {

						WorkingMemory workingMemory = ruleBase.newStatefulSession();
						workingMemory.setGlobal("log", log);
						workingMemory.setGlobal("creationDate", date);
						workingMemory.setGlobal("calculationDate", calculationDate);
						workingMemory.setGlobal("buildingAttributeTypeService", buildingAttributeTypeService);
						workingMemory.setGlobal("tariffCalculationResultService", tariffCalculationResultService);
						workingMemory.setGlobal("tariffServiceExt", tariffServiceExt);

						workingMemory.insert(btiBuildingService.readWithAttributes(new Stub<BtiBuilding>(btiBuilding)));
						workingMemory.fireAllRules();
						workingMemory.clearAgenda();
						cnt++;
						if (cnt % 100 == 0) {
							pLogger.info("{} buildings processed.", cnt);
						}
					}
				} catch (FileNotFoundException ex) {
					log.error("Rules file not found.", ex);
					return RESULT_ERROR;
				} catch (IOException ex) {
					log.error("Can't read rules file.", ex);
					return RESULT_ERROR;
				} catch (DroolsParserException ex) {
					log.error("Can't parse rules.", ex);
					return RESULT_ERROR;
				} catch (Exception ex) {
					log.error("Drools internal error.", ex);
					return RESULT_ERROR;
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
						if (source != null) {
							source.close();
						}
					} catch (IOException e) {
						// do nothing
					}
				}
			} finally {
				lockManager.releaseLock(Resources.BUILDING_ATTRIBUTES);
			}
		} else {
			pLogger.error("Can't calculate. Resource Locked. Exiting.");
		}
		return RESULT_NEXT;
	}

	@Required
	public void setTariffCalculationResultService(TariffCalculationResultService tariffCalculationResultService) {
		this.tariffCalculationResultService = tariffCalculationResultService;
	}

	@Required
	public void setBtiBuildingService(BtiBuildingService btiBuildingService) {
		this.btiBuildingService = btiBuildingService;
	}

	@Required
	public void setTariffCalculationRulesFileService(TariffCalculationRulesFileService tariffCalculationRulesFileService) {
		this.tariffCalculationRulesFileService = tariffCalculationRulesFileService;
	}

	@Required
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	@Required
	public void setTariffServiceExt(TariffServiceExt tariffServiceExt) {
		this.tariffServiceExt = tariffServiceExt;
	}

	@Required
	public void setBuildingAttributeTypeService(BuildingAttributeTypeService buildingAttributeTypeService) {
		this.buildingAttributeTypeService = buildingAttributeTypeService;
	}

}
