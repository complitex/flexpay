package net.sf.navigator.taglib.el;

import net.sf.navigator.displayer.MenuDisplayer;
import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * This tag acts the same as net.sf.navigator.taglib.UseMenuDisplayerTag, except
 * that it allows JSTL Expressions in all it's attributes.
 */
public class UseMenuDisplayerTag  extends net.sf.navigator.taglib.UseMenuDisplayerTag {

    private String bundle;
    private String config = MenuDisplayer.DEFAULT_CONFIG;
    private String locale;
    private String permissions;
    private String repository;

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public void setRepository(String key) {
        this.repository = key;
    }

    public UseMenuDisplayerTag() {
        super();
        init();
    }

    private void init() {
        name = null;
        bundle = null;
        config = null;
        locale = null;
        permissions = null;
        repository = null;
    }

    public void release() {
        super.release();
        init();
    }

    public int doStartTag() throws JspException {
        evaluateExpressions();

        // Default to JSTL Bundle to use (for EL Tag)
        Tag tag = findAncestorWithClass(this, BundleSupport.class);

        if (tag != null) {
            BundleSupport parent = (BundleSupport) tag;
            rb = parent.getLocalizationContext().getResourceBundle();
        } else {
            // check for the localizationContext in applicationScope, set in web.xml
            LocalizationContext localization = BundleSupport.getLocalizationContext(pageContext);

            if (localization != null) {
                rb = localization.getResourceBundle();
            }
        }

        return super.doStartTag();
    }

    private void evaluateExpressions() throws JspException {
        ExpressionEvaluator eval = new ExpressionEvaluator(this, pageContext);

        if (name != null) {
            super.setName(eval.evalString("name", name));
        }

        if (bundle != null) {
            super.setBundle(eval.evalString("bundle", bundle));
        }

        if (config != null) {
            super.setConfig(eval.evalString("config", config));
        }

        if (locale != null) {
            super.setLocale(eval.evalString("locale", locale));
        }

        if (permissions != null) {
            super.setPermissions(eval.evalString("permissions", permissions));
        }

        if (repository != null) {
            super.setRepository(eval.evalString("repository", repository));
        }
    }

}
