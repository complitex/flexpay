package org.flexpay.common.test;

import org.springframework.mock.jndi.SimpleNamingContext;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class MockInitialContextFactory implements InitialContextFactory {

    @Override
	public Context getInitialContext(Hashtable<?, ?> env) {
        return new SimpleNamingContext();
    }

}
