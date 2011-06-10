package org.flexpay.payments.action.tradingday;

import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

import static junit.framework.Assert.*;
import static org.flexpay.common.util.CollectionUtils.map;

public class TestConfirmationTradingDayServlet extends SpringBeanAwareTestCase {

	@Autowired
    private ProcessManager processManager;

	@Autowired
	private ProcessDefinitionManager processDefinitionManager;

	private Long tradingDayProcessInstanceId;

	@Before
	public void createTradingDayProcess() throws ProcessInstanceException, ProcessDefinitionException {

		processDefinitionManager.deployProcessDefinition("TradingDaySchedulingJob", true);

		Map<String, Object> parameters = map();
		ProcessInstance processInstance = processManager.startProcess("TradingDaySchedulingJob", parameters);
		assertNotNull("Process did not start: Object is null", processInstance);
		assertNotNull("Process did not start: Process instance id is null", processInstance.getId());
		assertTrue("Process state is not running", processInstance.getState() == ProcessInstance.STATE.RUNNING);

		tradingDayProcessInstanceId = processInstance.getId();
		assertTrue("Error", tradingDayProcessInstanceId > 0);

		//Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
		//String  currentStatus = (String) process.getParameters().get(TradingDaySchedulingJob.PROCESS_STATUS);
	}

	@After
	public void deleteTradingDayProcessInstance() {

		ProcessInstance process = processManager.getProcessInstance(tradingDayProcessInstanceId);
		if (process != null) {
			processManager.deleteProcessInstance(process);
		}
	}

	@Test
	public void testMissingPaymentPointId() throws IOException, ServletException {

		ConfirmationTradingDayServlet servlet = new ConfirmationTradingDayServlet();
		ServletRequest request = new HttpServletRequestStub();
		HttpServletResponseStub response = new HttpServletResponseStub();
		servlet.service(request, response);

		assertEquals("Bad error code for missing payment point id", 420, response.getError());
	}

	@Test
	public void testMissingDate() throws IOException, ServletException {

		ConfirmationTradingDayServlet servlet = new ConfirmationTradingDayServlet();
		HttpServletRequestStub request = new HttpServletRequestStub();
		request.setParameter("paymentPointId", "1");
		HttpServletResponseStub response = new HttpServletResponseStub();
		servlet.service(request, response);

		assertEquals("Bad error code for missing date parameter", 420, response.getError());
	}

	private class HttpServletRequestStub implements HttpServletRequest {

		private Map<String, String> parameters = new HashMap<String, String>();

		@Override
		public String getParameter(String s) {
			return parameters.get(s);
		}

		public void setParameter(String name, String value) {
			parameters.put(name, value);
		}

		@Override
		public String getMethod() {
			return "GET";
		}

		@Override
		public String getAuthType() {
			return null;
		}

		@Override
		public Cookie[] getCookies() {
			return new Cookie[0];
		}

		@Override
		public long getDateHeader(String s) {
			return 0;
		}

		@Override
		public String getHeader(String s) {
			return null;
		}

		@Override
		public Enumeration<String> getHeaders(String s) {
			return null;
		}

		@Override
		public Enumeration<String> getHeaderNames() {
			return null;
		}

		@Override
		public int getIntHeader(String s) {
			return 0;
		}

		@Override
		public String getPathInfo() {
			return null;
		}

		@Override
		public String getPathTranslated() {
			return null;
		}

		@Override
		public String getContextPath() {
			return null;
		}

		@Override
		public String getQueryString() {
			return null;
		}

		@Override
		public String getRemoteUser() {
			return null;
		}

		@Override
		public boolean isUserInRole(String s) {
			return false;
		}

		@Override
		public Principal getUserPrincipal() {
			return null;
		}

		@Override
		public String getRequestedSessionId() {
			return null;
		}

		@Override
		public String getRequestURI() {
			return null;
		}

		@Override
		public StringBuffer getRequestURL() {
			return null;
		}

		@Override
		public String getServletPath() {
			return null;
		}

		@Override
		public HttpSession getSession(boolean b) {
			return null;
		}

		@Override
		public HttpSession getSession() {
			return null;
		}

		@Override
		public boolean isRequestedSessionIdValid() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromCookie() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromURL() {
			return false;
		}

		@Override
        @Deprecated
		public boolean isRequestedSessionIdFromUrl() {
			return false;
		}

        @Override
        public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
            return false;
        }

        @Override
        public void login(String s, String s1) throws ServletException {
        }

        @Override
        public void logout() throws ServletException {
        }

        @Override
        public Collection<Part> getParts() throws IOException, ServletException {
            return null;
        }

        @Override
        public Part getPart(String s) throws IOException, ServletException {
            return null;
        }

        @Override
		public Object getAttribute(String s) {
			return null;
		}

		@Override
		public Enumeration<String> getAttributeNames() {
			return null;
		}

		@Override
		public String getCharacterEncoding() {
			return null;
		}

		@Override
		public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
		}

		@Override
		public int getContentLength() {
			return 0;
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public Enumeration<String> getParameterNames() {
			return null;
		}

		@Override
		public String[] getParameterValues(String s) {
			return new String[0];
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			return null;
		}

		@Override
		public String getProtocol() {
			return null;
		}

		@Override
		public String getScheme() {
			return null;
		}

		@Override
		public String getServerName() {
			return null;
		}

		@Override
		public int getServerPort() {
			return 0;
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return null;
		}

		@Override
		public String getRemoteAddr() {
			return null;
		}

		@Override
		public String getRemoteHost() {
			return null;
		}

		@Override
		public void setAttribute(String s, Object o) {
		}

		@Override
		public void removeAttribute(String s) {
		}

		@Override
		public Locale getLocale() {
			return null;
		}

		@Override
		public Enumeration<Locale> getLocales() {
			return null;
		}

		@Override
		public boolean isSecure() {
			return false;
		}

		@Override
		public RequestDispatcher getRequestDispatcher(String s) {
			return null;
		}

        @Deprecated
		@Override
		public String getRealPath(String s) {
			return null;
		}

		@Override
		public int getRemotePort() {
			return 0;
		}

		@Override
		public String getLocalName() {
			return null;
		}

		@Override
		public String getLocalAddr() {
			return null;
		}

		@Override
		public int getLocalPort() {
			return 0;
		}

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }
    }

	private class HttpServletResponseStub implements HttpServletResponse {

		private int error;

		@Override
		public void sendError(int i, String s) throws IOException {
			error = i;
		}

		public int getError() {
			return error;
		}

		@Override
		public void addCookie(Cookie cookie) {
		}

		@Override
		public boolean containsHeader(String s) {
			return false;
		}

		@Override
		public String encodeURL(String s) {
			return null;
		}

		@Override
		public String encodeRedirectURL(String s) {
			return null;
		}

        @Deprecated
		@Override
		public String encodeUrl(String s) {
			return null;
		}

        @Deprecated
		@Override
		public String encodeRedirectUrl(String s) {
			return null;
		}

		@Override
		public void sendError(int i) throws IOException {
		}

		@Override
		public void sendRedirect(String s) throws IOException {
		}

		@Override
		public void setDateHeader(String s, long l) {
		}

		@Override
		public void addDateHeader(String s, long l) {
		}

		@Override
		public void setHeader(String s, String s1) {
		}

		@Override
		public void addHeader(String s, String s1) {
		}

		@Override
		public void setIntHeader(String s, int i) {
		}

		@Override
		public void addIntHeader(String s, int i) {
		}

		@Override
		public void setStatus(int i) {
		}

        @Deprecated
		@Override
		public void setStatus(int i, String s) {
		}

        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public String getHeader(String s) {
            return null;
        }

        @Override
        public Collection<String> getHeaders(String s) {
            return null;
        }

        @Override
        public Collection<String> getHeaderNames() {
            return null;
        }

        @Override
		public String getCharacterEncoding() {
			return null;
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return null;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return null;
		}

		@Override
		public void setCharacterEncoding(String s) {
		}

		@Override
		public void setContentLength(int i) {
		}

		@Override
		public void setContentType(String s) {
		}

		@Override
		public void setBufferSize(int i) {
		}

		@Override
		public int getBufferSize() {
			return 0;
		}

		@Override
		public void flushBuffer() throws IOException {
		}

		@Override
		public void resetBuffer() {
		}

		@Override
		public boolean isCommitted() {
			return false;
		}

		@Override
		public void reset() {
		}

		@Override
		public void setLocale(Locale locale) {
		}

		@Override
		public Locale getLocale() {
			return null;
		}
	}
}
