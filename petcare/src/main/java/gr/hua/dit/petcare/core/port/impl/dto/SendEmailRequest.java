package gr.hua.dit.petcare.core.port.impl.dto;


public class SendEmailRequest {
    private String title;   // Το θέμα του email
    private String body;    // Το περιεχόμενο
    private String userId;  // Το email του παραλήπτη

    public SendEmailRequest() {
    }

    public SendEmailRequest(String title, String body, String userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SendEmailRequest{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}