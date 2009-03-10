package net.sf.navigator.taglib;

import net.sf.navigator.displayer.MenuDisplayer;
import net.sf.navigator.displayer.MenuDisplayerMapping;
import net.sf.navigator.menu.MenuRepository;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Arrays;
import java.util.Collections;

public class UseMenuDisplayerTag extends TagSupport {

	protected Logger log = LoggerFactory.getLogger(getClass());

    public static final String PRIVATE_REPOSITORY = "net.sf.navigator.repositoryKey";
    public static final String DISPLAYER_KEY = "net.sf.navigator.taglib.DISPLAYER";

    protected MenuDisplayer menuDisplayer;
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public int doStartTag() throws JspException {

        MenuRepository rep = (MenuRepository) pageContext.findAttribute(MenuRepository.MENU_REPOSITORY_KEY);

        if (rep == null) {
            throw new JspException("The menu repository could not be found.");
        } else {
            pageContext.setAttribute(PRIVATE_REPOSITORY, rep);
        }

        MenuDisplayerMapping displayerMapping = rep.getMenuDisplayer(name);

        if (displayerMapping == null) {
            throw new JspException("The displayer mapping for the specified MenuDisplayer does not exist");
        }

        MenuDisplayer displayerInstance;

        try {
            displayerInstance = (MenuDisplayer) Class.forName(displayerMapping.getType()).newInstance();
            menuDisplayer = displayerInstance;
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        displayerInstance.init(pageContext, displayerMapping);
		displayerInstance.setUserPreferences(UserPreferences.getPreferences(request));
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if (null == currentUser || null == currentUser.getAuthorities() || currentUser.getAuthorities().length < 1) {
			displayerInstance.setRolesGranted(Collections.emptyList());
		} else {
			displayerInstance.setRolesGranted(Arrays.asList(currentUser.getAuthorities()));
		}

        pageContext.setAttribute(DISPLAYER_KEY, displayerInstance);

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        menuDisplayer.end(pageContext);
        pageContext.removeAttribute(DISPLAYER_KEY);
        pageContext.removeAttribute(PRIVATE_REPOSITORY);
        return EVAL_PAGE;
    }

    public void release() {
        menuDisplayer = null;
        name = null;
    }

}
