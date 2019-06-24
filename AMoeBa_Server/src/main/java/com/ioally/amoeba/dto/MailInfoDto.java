package com.ioally.amoeba.dto;

public class MailInfoDto {

    /**
     * 发送人别名，默认邮件地址
     */
    private String fromAlias;

    /**
     * 邮件接收人
     */
    private String[] toEmail;

    /**
     * 邮件抄送人
     */
    private String[] copyTo;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String text;

    public String getFromAlias() {
        return fromAlias;
    }

    public void setFromAlias(String fromAlias) {
        this.fromAlias = fromAlias;
    }

    public String[] getToEmail() {
        return toEmail;
    }

    public void setToEmail(String... toEmail) {
        this.toEmail = toEmail;
    }

    public String[] getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(String... copyTo) {
        this.copyTo = copyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public static final class Builder {
        private String fromAlias;
        private String[] toEmail;
        private String[] copyTo;
        private String subject;
        private String text;

        public Builder() {
        }

        public Builder fromAlias(String fromAlias) {
            this.fromAlias = fromAlias;
            return this;
        }

        public Builder toEmail(String... toEmail) {
            this.toEmail = toEmail;
            return this;
        }

        public Builder copyTo(String... copyTo) {
            this.copyTo = copyTo;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public MailInfoDto build() {
            MailInfoDto mailInfoDto = new MailInfoDto();
            mailInfoDto.setFromAlias(fromAlias);
            mailInfoDto.setToEmail(toEmail);
            mailInfoDto.setCopyTo(copyTo);
            mailInfoDto.setSubject(subject);
            mailInfoDto.setText(text);
            return mailInfoDto;
        }
    }
}
