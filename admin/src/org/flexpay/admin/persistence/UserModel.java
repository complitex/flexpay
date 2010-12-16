package org.flexpay.admin.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class UserModel {

	private String userName;
	private String firstName;
	private String lastName;
	private String password;
	private String reEnterPassword;
	private String oldPassword;
	private Long roleId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReEnterPassword() {
		return reEnterPassword;
	}

	public void setReEnterPassword(String reEnterPassword) {
		this.reEnterPassword = reEnterPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getFullName() {

		if (isEmpty(firstName)) {
			return lastName;
		} else if (isEmpty(lastName)) {
			return firstName;
		}

		return firstName + " " + lastName;
	}

	public static UserModel getInstance() {
		return new UserModel();
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("userName", userName).
                append("firstName", firstName).
                append("lastName", lastName).
                append("password", password).
                append("reEnterPassword", reEnterPassword).
                append("oldPassword", oldPassword).
                append("roleId", roleId).
                toString();
    }
}
