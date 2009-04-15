package org.flexpay.common.util.jms.openmq;

import javax.jms.ConnectionFactory;
import java.util.Enumeration;
import java.util.Properties;

public class MQConnectionFactoryFactory {
	private Properties props;

	public void setProperties(Properties props) {
		this.props = props;
	}

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
			throw new RuntimeException("MQConnectionFactoryFactory.createConnectionFactory() failed: ", e);
		}
		return cf;
	}
}
