package org.flexpay.payments.service.impl;

import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class RequestIdSetMessageConverter implements MessageConverter {

	private MessageConverter delegate;

	/**
	 * Convert a Java object to a JMS Message using the supplied session to create the message object.
	 *
	 * @param object  the object to convert
	 * @param session the Session to use for creating a JMS Message
	 * @return the JMS Message
	 * @throws javax.jms.JMSException if thrown by JMS API methods
	 * @throws org.springframework.jms.support.converter.MessageConversionException
	 *                                in case of conversion failure
	 */
    @Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		Message msg = delegate.toMessage(object, session);
		setRequestIdProperty(object, msg);
		return msg;
	}

	private void setRequestIdProperty(Object object, Message msg) throws JMSException {
		if (object instanceof SearchResponse) {
			SearchResponse response = (SearchResponse) object;
			msg.setStringProperty("requestId", response.getJmsRequestId());
		}
	}

	/**
	 * Convert from a JMS Message to a Java object.
	 *
	 * @param message the message to convert
	 * @return the converted Java object
	 * @throws javax.jms.JMSException if thrown by JMS API methods
	 * @throws org.springframework.jms.support.converter.MessageConversionException
	 *                                in case of conversion failure
	 */
    @Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		return delegate.fromMessage(message);
	}

	@Required
	public void setDelegate(MessageConverter delegate) {
		this.delegate = delegate;
	}
}
