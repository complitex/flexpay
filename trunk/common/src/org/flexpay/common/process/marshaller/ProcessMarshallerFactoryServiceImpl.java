package org.flexpay.common.process.marshaller;

import org.drools.marshalling.impl.MarshallerReaderContext;
import org.drools.marshalling.impl.ProcessMarshaller;
import org.drools.marshalling.impl.ProcessMarshallerFactoryService;
import org.jbpm.marshalling.impl.ProcessMarshallerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ProcessMarshallerFactoryServiceImpl implements ProcessMarshallerFactoryService {
	private static final Logger log = LoggerFactory.getLogger(ProcessMarshallerFactoryServiceImpl.class);

	@Override
	public ProcessMarshaller newProcessMarshaller() {
		return new ProcessMarshallerImpl() {
			@Override
			public void readWorkItems(MarshallerReaderContext context) throws IOException {
				log.debug("read work items: {}", context.marshalWorkItems);
				super.readWorkItems(context);
			}
		};
	}
}
