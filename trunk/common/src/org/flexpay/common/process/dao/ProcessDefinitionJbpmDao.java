package org.flexpay.common.process.dao;

import org.drools.definition.KnowledgePackage;

import java.util.Collection;
import java.util.List;

public interface ProcessDefinitionJbpmDao {

	List<org.drools.definition.process.Process> getProcesses();

	org.drools.definition.process.Process getProcess(String processId);

	void addPackages(Collection<KnowledgePackage> packages);

}
