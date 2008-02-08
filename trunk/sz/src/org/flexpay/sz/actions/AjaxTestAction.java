package org.flexpay.sz.actions;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class AjaxTestAction implements SessionAware {
	private static final String SESSION_BYTES_UPLOADED_NOTCHANGED_COUNT = "SESSION_BYTES_UPLOADED_NOTCHANGED_COUNT";
    private Map sessionMap;
    private String rnd;
    private String stringResult;
	
	public AjaxTestAction()
    {
    }
	
	public String execute()
    {
        setStringResult("" + System.currentTimeMillis());
        
        return "success";
    }

	

	public void setSession(Map sessionMap)
    {
        this.sessionMap = sessionMap;
    }

	public String getRnd() {
		return rnd;
	}

	public void setRnd(String rnd) {
		this.rnd = rnd;
	}

	public String getStringResult() {
		return stringResult;
	}

	public void setStringResult(String stringResult) {
		this.stringResult = stringResult;
	}

	

}
