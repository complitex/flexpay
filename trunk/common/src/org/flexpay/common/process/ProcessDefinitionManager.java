package org.flexpay.common.process;

import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.persistence.ProcessDefinition;
import org.flexpay.common.service.Roles;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;

import java.io.File;
import java.util.List;

public interface ProcessDefinitionManager {

	/**
	 * Get process definitions from knowledge base
	 *
	 * @return Process definitions
	 */
	@Secured (Roles.PROCESS_DEFINITION_READ)
	List<ProcessDefinition> getProcessDefinitions();

	/**
	 * Get process definition from knowledge base by definition id
	 *
	 * @param definitionId Definition id
	 * @return Process definition
	 */
	@Secured (Roles.PROCESS_DEFINITION_READ)
	ProcessDefinition getProcessDefinition(@NotNull String definitionId);

	/**
	 * Deploy all process definitions.
	 * Deploy process definition to knowledge base from storage If process definition did not content in knowledge base.
	 * Get  process definition description from file system and add to storage it if definition did not deploy to storage.
	 *
	 * @throws ProcessDefinitionException If process definition failed deploy to knowledge base
	 */
	void deployProcessDefinitions() throws ProcessDefinitionException;

	/**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param name	name of process definition
	 * @throws org.flexpay.common.process.exception.ProcessDefinitionException when can't deploy process definition to knowledge base
	 */
	@Secured(Roles.PROCESS_DEFINITION_UPLOAD_NEW)
	void deployProcessDefinition(String name) throws ProcessDefinitionException;

	/**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param name	name of process definition
	 * @param replace if <code>true</code> replace process definition if exist otherwise nothing do
	 * @throws org.flexpay.common.process.exception.ProcessDefinitionException when can't deploy process definition to knowledge base
	 */
	@Secured(Roles.PROCESS_DEFINITION_UPLOAD_NEW)
	void deployProcessDefinition(String name, boolean replace) throws ProcessDefinitionException;

	/**
	 * Deploys process definition to jbpm by process definition name
	 *
	 * @param definitionFile file of process definition
	 * @throws ProcessDefinitionException when can't deploy process definition to knowledge base
	 */
	@Secured (Roles.PROCESS_DEFINITION_UPLOAD_NEW)
	void deployProcessDefinition(File definitionFile) throws ProcessDefinitionException;

	/**
	 * Deploy process definition to knowledge base from storage with version
	 *
	 * @param name name of process definition
	 * @param version version of process definition
	 * @throws ProcessDefinitionException when can't deploy process definition to knowledge base
	 */
	@Secured (Roles.PROCESS_DEFINITION_UPLOAD_NEW)
	void deployProcessDefinition(String name, long version) throws ProcessDefinitionException;

	void setDefinitionPaths(List<String> definitionPaths);
}
