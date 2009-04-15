package org.flexpay.payments.service.impl;

import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

public class JmsQuittanceDetailsFinder implements QuittanceDetailsFinder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private JmsTemplate jmsTemplate;
	private Queue requestQueue;
	private Queue responseQueue;

	/**
	 * Find quittance details
	 *
	 * @param request Request for quittance details
	 * @return Details response
	 */
	@NotNull
	public QuittanceDetailsResponse findQuittance(final QuittanceDetailsRequest request) {
		request.setRequestId(ApplicationConfig.getInstanceId() + System.currentTimeMillis());

		jmsTemplate.send(requestQueue, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage msg = session.createObjectMessage(request);
				msg.setStringProperty("requestId", request.getRequestId());
				return msg;
			}
		});

		QuittanceDetailsResponse response = (QuittanceDetailsResponse) jmsTemplate.receiveSelectedAndConvert(
				responseQueue, String.format("requestId = '%s'", request.getRequestId()));

		log.debug("Response recieved: ");

		return response;
	}

	@Required
	public void setConnectionFactory(ConnectionFactory cf) {
		jmsTemplate = new JmsTemplate(cf);
	}

	@Required
	public void setRequestQueue(Queue requestQueue) {
		this.requestQueue = requestQueue;
	}

	@Required
	public void setResponseQueue(Queue responseQueue) {
		this.responseQueue = responseQueue;
	}
}
