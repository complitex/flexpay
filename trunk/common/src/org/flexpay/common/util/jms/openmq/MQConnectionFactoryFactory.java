package org.flexpay.common.util.jms.openmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.jms.ConnectionFactory;
import java.util.Enumeration;
import java.util.Properties;

public class MQConnectionFactoryFactory {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Properties props;

	public ConnectionFactory createConnectionFactory() {
		com.sun.messaging.ConnectionFactory cf = new com.sun.messaging.ConnectionFactory();
		try {
			Enumeration<?> keys = props.propertyNames();
			while (keys.hasMoreElements()) {
				String name = (String) keys.nextElement();
				String value = props.getProperty(name);
				cf.setProperty(name, value);
			}
		} catch (Exception e) {
			log.info("MQConnectionFactoryFactory.createConnectionFactory() failed", e);
			throw new RuntimeException("MQConnectionFactoryFactory.createConnectionFactory() failed: ", e);
		}
		return cf;
	}

    @Required
    public void setProperties(Properties props) {
        this.props = props;
    }

}
