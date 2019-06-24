package com.ioally.amoeba.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogInfoDto extends BaseRequestDto {

    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 工作内容描述
     */
    private String content;
    /**
     * 跳过双休日
     */
    private Boolean skipWeekend;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSkipWeekend() {
        return skipWeekend;
    }

    public void setSkipWeekend(Boolean skipWeekend) {
        this.skipWeekend = skipWeekend;
    }
}
