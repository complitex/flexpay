package net.sourceforge.navigator.taglib;

import net.sourceforge.navigator.displayer.MenuDisplayer;
import net.sourceforge.navigator.displayer.MenuDisplayerMapping;
import net.sourceforge.navigator.menu.MenuRepository;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Arrays;
import java.util.Collections;

public class UseMenuDisplayerTag extends TagSupport {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final String PRIVATE_REPOSITORY = "net.sourceforge.navigator.repositoryKey";
	public static final String DISPLAYER_KEY = "net.sourceforge.navigator.taglib.DISPLAYER";

	protected MenuDisplayer menuDisplayer;
	protected String name;

	public void setName(String name) {
		this.name = name;
	}

	@Override
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

		displayerInstance.init(pageContext, displayerMapping);
		displayerInstance.setUserPreferences(UserPreferences.getPreferences());
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if (null == currentUser || null == currentUser.getAuthorities() || currentUser.getAuthorities().isEmpty()) {
			displayerInstance.setRolesGranted(Collections.emptyList());
		} else {
			displayerInstance.setRolesGranted(currentUser.getAuthorities());
		}

		pageContext.setAttribute(DISPLAYER_KEY, displayerInstance);

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		menuDisplayer.end(pageContext);
		pageContext.removeAttribute(DISPLAYER_KEY);
		pageContext.removeAttribute(PRIVATE_REPOSITORY);
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		menuDisplayer = null;
		name = null;
	}

}
