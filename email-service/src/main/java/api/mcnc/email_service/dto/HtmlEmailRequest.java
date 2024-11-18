package api.mcnc.email_service.dto;

import java.util.List;

public class HtmlEmailRequest {
    private List<String> emails;
    private String userName;
    private String projectName;
    private String dynamicLink;

    // 기본 생성자, getter, setter
    public HtmlEmailRequest() {}

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDynamicLink() {
        return dynamicLink;
    }

    public void setDynamicLink(String dynamicLink) {
        this.dynamicLink = dynamicLink;
    }
}
