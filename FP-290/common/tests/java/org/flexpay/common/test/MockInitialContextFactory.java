package org.flexpay.common.test;

import org.springframework.mock.jndi.SimpleNamingContext;

import javax.naming.spi.InitialContextFactory;
import javax.naming.Context;
import java.util.Hashtable;

public class MockInitialContextFactory implements InitialContextFactory {

	public Context getInitialContext(Hashtable env) {
        return new SimpleNamingContext();
    }

}
