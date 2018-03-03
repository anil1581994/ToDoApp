package com.bridgelabz.user.model;

public class EmailProperties {
  private String email;
  private String password;
  private String emailAddress;
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getEmailAddress() {
	return emailAddress;
}
public void setEmailAddress(String emailAddress) {
	this.emailAddress = emailAddress;
}
@Override
public String toString() {
	return "EmailProperties [email=" + email + ", password=" + password + ", emailAddress=" + emailAddress + "]";
}

}
