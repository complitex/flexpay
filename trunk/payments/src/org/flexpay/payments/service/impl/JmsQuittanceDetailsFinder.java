package org.flexpay.payments.service.impl;

import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;
import org.flexpay.payments.action.outerrequest.request.response.Status;
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
	@Override
	public SearchResponse findQuittance(final SearchRequest<?> request) {

        log.debug("FindQuittance started. Sending JMS message");
		request.setJmsRequestId(ApplicationConfig.getInstanceId() + System.currentTimeMillis());

		jmsTemplate.send(requestQueue, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage msg = session.createObjectMessage(request);
				msg.setJMSReplyTo(responseQueue);
				msg.setStringProperty("requestId", request.getJmsRequestId());
				return msg;
			}
		});

        SearchResponse response = (SearchResponse) jmsTemplate.receiveSelectedAndConvert(
                responseQueue, String.format("requestId = '%s'", request.getJmsRequestId()));

		if (response == null) {
			request.getResponse().setStatus(Status.RECIEVE_TIMEOUT);
            return request.getResponse();
		}

		return response;
	}

	@Required
	public void setConnectionFactory(ConnectionFactory cf) {
		jmsTemplate = new JmsTemplate(cf);
		jmsTemplate.setReceiveTimeout(30000);
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
