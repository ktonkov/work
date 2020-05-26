package iss.work.mantis.model;

public class MailMessage {
    private String to;
    private String text;

    public MailMessage(String to, String text) {
        this.to = to;
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }
}
