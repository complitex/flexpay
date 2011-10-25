package org.flexpay.common.process.dao;

import org.drools.KnowledgeBase;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.process.Process;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Pavel Sknar
 *         Date: 04.10.11 15:56
 */
public class ProcessDefinitionJbpmImpl implements ProcessDefinitionJbpmDao {

	private KnowledgeBase kbase;

	@Override
	public List<org.drools.definition.process.Process> getProcesses() {
		List<org.drools.definition.process.Process> result = new ArrayList<Process>();
		for (KnowledgePackage kpackage: kbase.getKnowledgePackages()) {
			result.addAll(kpackage.getProcesses());
		}
		return result;
	}

	@Override
	public org.drools.definition.process.Process getProcess(String processId) {
		return kbase.getProcess(processId);
		/*
		for (KnowledgePackage kpackage: getSession().getKnowledgeBase().getKnowledgePackages()) {
			for (org.drools.definition.process.Process process: kpackage.getProcesses()) {
				if (processId.equals(process.getId())) {
					return process;
				}
			}
		}
		return null;
		*/
	}

	@Override
	public void addPackages(Collection<KnowledgePackage> packages) {
		kbase.addKnowledgePackages(packages);
	}

	@Required
	public void setKbase(KnowledgeBase kbase) {
		this.kbase = kbase;
	}
}
