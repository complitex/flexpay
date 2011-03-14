package org.flexpay.common.process;

import org.springmodules.workflow.jbpm31.JbpmObjectFactory;
import org.jbpm.configuration.ObjectFactoryImpl;
import org.jbpm.configuration.ObjectFactoryParser;
import org.jbpm.JbpmConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Workaround http://www.jboss.org/index.html?module=bb&op=viewtopic&p=4181668#4181668
 * If you are already using spring module for jBPM, you are probably using the LocalJbpmConfigurationFactoryBean class and
 * you are more than likely specifying the path to a jbpm.xml config file. That's a problem, since the presence of a config file
 * causes a fallback to the original object factory even if you specify your own object factory.
 */

public class FlexPayJBPMObjectFactory extends JbpmObjectFactory implements InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(FlexPayJBPMObjectFactory.class);

	protected ObjectFactoryImpl flexpayObjectFactory;

	protected Resource configurationFile;

	/**
	 * do workaround
	 * @throws Exception
	 */
    @Override
	public void afterPropertiesSet() throws Exception {
		if (configurationFile != null) {
			LOG.info("creating JbpmConfiguration from resource ", configurationFile.getDescription());
			InputStream stream = configurationFile.getInputStream();
			flexpayObjectFactory = ObjectFactoryParser.parseInputStream(stream);
			stream.close();
		}
		JbpmConfiguration.Configs.setDefaultObjectFactory(this);
	}

	/**
	 * Create Object
	 *
	 * @param name object name
	 * @return created object
	 */
	@Override
	public Object createObject(final String name) {
		Object result;
		try {
			result = super.createObject(name);
		} catch (NoSuchBeanDefinitionException e) {
			result = flexpayObjectFactory.createObject(name);
		}
		return result;
	}

	/**
	 * Check if object factory already has object
	 *
	 * @param name object to check
	 * @return true if factory has object, false otherwise
	 */
	@Override
	public boolean hasObject(final String name) {
		boolean result;
		try {
			result = super.hasObject(name);
		} catch (NoSuchBeanDefinitionException e) {
			result = flexpayObjectFactory.hasObject(name);
		}
		return result;
	}

	@Required
	public void setConfiguration(final Resource configuration) {
		configurationFile = configuration;
	}

}
