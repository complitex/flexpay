package org.flexpay.common.util.config;

import net.sourceforge.navigator.menu.MenuComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.action.breadcrumbs.Crumb;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.UserRole;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.SecurityUtil;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;

public class UserPreferences extends DomainObject implements Serializable, UserDetails {

	private UserDetails targetDetails = null;
	// ldap object classes used to split properties
	private Set<String> objectClasses = Collections.emptySet();
	private Set<String> attributes = Collections.emptySet();

	private String usernameStub;
	private String fullName;
	private String lastName;
	private String firstName;

	private Locale locale;

	private UserRole userRole;

	private Integer pageSize = 20;
	private String testProp = "setup-me";

	private MenuComponent menuComponent = new MenuComponent();

	private String activeMenu;
	private Stack<Crumb> crumbs = new Stack<Crumb>();

	private Certificate certificate;

	public UserPreferences() {
	}

	/**
	 * Get current user session preferences
	 *
	 * @return UserPreferences
	 */
	public static UserPreferences getPreferences() {
		return (UserPreferences) SecurityUtil.getAuthentication().getPrincipal();
	}

	public Set<String> getObjectClasses() {
		return objectClasses;
	}

	public void setObjectClasses(Set<String> objectClasses) {
		this.objectClasses = objectClasses;
	}

	public Set<String> attributes() {
		return attributes;
	}

	public void attributes(Set<String> attributes) {
		this.attributes = attributes;
	}

	public Locale getLocale() {
		if (locale == null) {
			locale = ApplicationConfig.getDefaultLocale();
		}
		return locale; 
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getLanguageCode() {
		return getLocale().getLanguage();
	}

	public void setLanguageCode(String code) {
		if (StringUtils.isNotBlank(code)) {
			Language lang = LanguageUtil.getLanguage(new Locale(code));
			setLocale(lang.getLocale());
		} else {
			setLocale(ApplicationConfig.getDefaultLocale());
		}
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public MenuComponent getMenuComponent() {
		return menuComponent;
	}

	public void setMenuComponent(MenuComponent menuComponent) {
		this.menuComponent = menuComponent;
	}

	public String getTestProp() {
		return testProp;
	}

	public void setTestProp(String testProp) {
		this.testProp = testProp;
	}

	public String getActiveMenu() {
		return activeMenu;
	}

	public void setActiveMenu(String activeMenu) {
		this.activeMenu = activeMenu;
	}

	public Stack<Crumb> getCrumbs() {
		return crumbs;
	}

	public void setCrumbs(Stack<Crumb> crumbs) {
		this.crumbs = crumbs;
	}

	public void setTargetDetails(UserDetails targetDetails) {
		this.targetDetails = targetDetails;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	/**
	 * Returns the authorities granted to the user. Cannot return <code>null</code>.
	 *
	 * @return the authorities, sorted by natural key (never <code>null</code>)
	 */
	@Override
	public GrantedAuthority[] getAuthorities() {
		return targetDetails.getAuthorities();
	}

	/**
	 * Returns the password used to authenticate the user. Cannot return <code>null</code>.
	 *
	 * @return the password (never <code>null</code>)
	 */
	@Override
	public String getPassword() {
		return targetDetails.getPassword();
	}

	/**
	 * Returns the username used to authenticate the user. Cannot return <code>null</code>.
	 *
	 * @return the username (never <code>null</code>)
	 */
	@Override
	public String getUsername() {
		return targetDetails == null ? usernameStub : targetDetails.getUsername();
	}

	public void setUsername(String username) {
		// hack for hibernate property access
		if (targetDetails == null) {
			usernameStub = username;
		}
	}

	/**
	 * Indicates whether the user's account has expired. An expired account cannot be authenticated.
	 *
	 * @return <code>true</code> if the user's account is valid (ie non-expired), <code>false</code> if no longer valid (ie
	 *         expired)
	 */
	@Override
	public boolean isAccountNonExpired() {
		return targetDetails.isAccountNonExpired();
	}

	/**
	 * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
	 *
	 * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
	 */
	@Override
	public boolean isAccountNonLocked() {
		return targetDetails.isAccountNonLocked();
	}

	/**
	 * Indicates whether the user's credentials (password) has expired. Expired credentials prevent authentication.
	 *
	 * @return <code>true</code> if the user's credentials are valid (ie non-expired), <code>false</code> if no longer
	 *         valid (ie expired)
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return targetDetails.isAccountNonExpired();
	}

	/**
	 * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
	 *
	 * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
	 */
	@Override
	public boolean isEnabled() {
		return targetDetails.isAccountNonExpired();
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("usernameStub", usernameStub).
                append("fullName", fullName).
                append("lastName", lastName).
                append("firstName", firstName).
                append("locale", locale).
                append("userRole", userRole).
                append("pageSize", pageSize).
                append("activeMenu", activeMenu).
                append("certificate", certificate).
                toString();
    }
}
