package org.example.lamda.models;

public class Title {

    private String titleid;
    private String titlename;
    private String contactId;
    private String firstNamePen;
    private String lastNamePen;
    private String contributorSequence;
    private String contributorRole;
    
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getFirstNamePen() {
		return firstNamePen;
	}
	public void setFirstNamePen(String firstNamePen) {
		this.firstNamePen = firstNamePen;
	}
	public String getLastNamePen() {
		return lastNamePen;
	}
	public void setLastNamePen(String lastNamePen) {
		this.lastNamePen = lastNamePen;
	}
	public String getContributorSequence() {
		return contributorSequence;
	}
	public void setContributorSequence(String contributorSequence) {
		this.contributorSequence = contributorSequence;
	}
	public String getContributorRole() {
		return contributorRole;
	}
	public void setContributorRole(String contributorRole) {
		this.contributorRole = contributorRole;
	}
	public String getTitleid() {
		return titleid;
	}
	public void setTitleid(String titleid) {
		this.titleid = titleid;
	}
	public String getTitlename() {
		return titlename;
	}
	public void setTitlename(String titlename) {
		this.titlename = titlename;
	}
	
	@Override
	public String toString() {
	    return "Title{" +
	            "titleid='" + titleid + '\'' +
	            ", titlename='" + titlename + '\'' +
	            ", contactId='" + contactId + '\'' +
	            ", firstNamePen='" + firstNamePen + '\'' +
	            ", lastNamePen='" + lastNamePen + '\'' +
	            ", contributorSequence='" + contributorSequence + '\'' +
	            ", contributorRole='" + contributorRole + '\'' +
	            '}';
	}


}
