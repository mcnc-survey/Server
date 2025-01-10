package api.mcnc.email_service.dto;

import java.util.List;

/**
 * HTML 이메일 요청을 위한 DTO 클래스
 * @author 차익현
 */
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
