package org.flexpay.tc.process;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.locking.LockManager;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.flexpay.tc.locking.Resources;
import org.flexpay.tc.util.config.ApplicationConfig;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.bti.persistence.BtiBuilding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NonNls;

import java.io.*;
import java.util.Map;
import java.util.List;

public class TariffCalculationJob extends Job {

	private TariffCalculationResultService tariffCalculationResultService;
	private BtiBuildingService btiBuildingService;
	private TariffCalculationRulesFileService tariffCalculationRulesFileService;
	private LockManager lockManager;

	public static final String RULES_ID = "RULE_ID";

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		if (lockManager.lock(Resources.BUILDING_ATTRIBUTES)) {
			//get rules file
			try {
				TariffCalculationRulesFile rulesFile = tariffCalculationRulesFileService.read(new Stub<TariffCalculationRulesFile>((Long) parameters.get(RULES_ID)));

				if (rulesFile == null) {
					log.error("Tariff calculation rules for id {} not found. Exiting.", parameters.get(RULES_ID));
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
					List<BtiBuilding> btiBuildingList = btiBuildingService.findByTown(ApplicationConfig.getDefaultTownStub());
					log.debug("Found " + btiBuildingList.size() + " buildings");
					int cnt = 0;
					for (BtiBuilding btiBuilding : btiBuildingList) {
						WorkingMemory workingMemory = ruleBase.newStatefulSession();
						workingMemory.setGlobal("btiBuildingService", btiBuildingService);
						workingMemory.setGlobal("tarifCalculationResultService", tariffCalculationResultService);
						workingMemory.setGlobal("logger", log);
						workingMemory.insert(btiBuildingService.readWithAttributes(new Stub<BtiBuilding>(btiBuilding)));
						log.debug("BtiBuilding with id = " + btiBuilding.getId());
						workingMemory.fireAllRules();
						workingMemory.clearAgenda();
						cnt++;
						if (cnt % 100 == 0) {
							log.info(cnt + " buildings processed.");
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
				}
			} finally {
				lockManager.releaseLock(Resources.BUILDING_ATTRIBUTES);
			}
		} else {
			log.error("Can't calculate. Resource Locked. Exiting.");
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
}
