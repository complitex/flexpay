package net.sf.navigator.taglib.el;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * Utility class to help with the evaluation of JSTL Expression Language. It
 * mainly encapsulates the calls to ExpressionEvaluationManager to ease the
 * use of this class.
 */
public class ExpressionEvaluator {

	protected Logger log = LoggerFactory.getLogger(getClass());

    private PageContext context;
    private Tag tag;

    public ExpressionEvaluator(Tag tag, PageContext context) {
        this.tag = tag;
        this.context = context;
    }

    /**
     * Evaluate expression in attrValue.
     *
     * @param attrName attrName
	 * @param attrValue attrValue
	 * @param returnClass returnClass
	 *
	 * @return evaluate expression of attrValue, null if attrValue is null.
	 * @throws javax.servlet.jsp.JspException javax.servlet.jsp.JspException
     */
    public Object eval(String attrName, String attrValue, Class<?> returnClass) throws JspException {
        return attrValue == null ? null : ExpressionEvaluatorManager.evaluate(attrName, attrValue, returnClass, tag, context);
    }

    public String evalString(String attrName, String attrValue) throws JspException {
        return (String) eval(attrName, attrValue, String.class);
    }

    public boolean evalBoolean(String attrName, String attrValue) throws JspException {
        Boolean rtn = (Boolean) eval(attrName, attrValue, Boolean.class);
        return rtn != null && rtn;
    }

    public long evalLong(String attrName, String attrValue) throws JspException {
        Long rtn = (Long) eval(attrName, attrValue, Long.class);
        return rtn != null ? rtn : -1L;
    }

    public int evalInt(String attrName, String attrValue) throws JspException {
        Integer rtn = (Integer) eval(attrName, attrValue, Integer.class);
        return rtn != null ? rtn : -1;
    }

}
