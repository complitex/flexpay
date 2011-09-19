package org.flexpay.common.progressbar;

import com.opensymphony.xwork2.ActionInvocation;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Used to output raw text based on a string in the action class
 *
 * @author davidc
 */
public class RawTextResult extends StrutsResultSupport {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private String stringName = "stringResult";
    
    /** Creates a new instance of XMLStringTransformResult */
    public RawTextResult() {
    }
    
    /**
     * This is executed when the result is called
     */
    @Override
    public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
        String stringResult = null;
        PrintWriter out = null;
        
        try {
            // Find the JDom document from the invocation variable stack
            // This is a org.jdom.Document member variable in your action that
            // uses this class as a result
            stringResult = (String) invocation.getStack().findValue(conditionalParse(stringName, invocation));
            
            if (stringResult == null) {
                String msg = ("Cannot find a String with the name [" + stringName + "] in the invocation stack. " + "You must have a getStringResult() method in the stack that returns the String.");
                logger.error(msg);
                throw new IllegalArgumentException(msg);
            }
            
            // Find the Response in context
            HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(HTTP_RESPONSE);
            
            response.setContentType("text/plain");
            out = response.getWriter();
            
            out.print(stringResult);
            
            out.flush();
        } catch (Exception e) {
            logger.error("Problem with outputting the raw string result " + e.getMessage());
            throw e;
        } finally {
            if (out != null)
                out.close();
        }
    }
    
}
