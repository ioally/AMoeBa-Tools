package com.ioally.amoeba.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 批量填写阿米巴dto
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaveAMoeBalLogDto extends BaseRequestDto implements Serializable {

    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 排除开始日期
     */
    private String startExcludeDate;
    /**
     * 排除结束日期
     */
    private String endExcludeDate;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否跳过双休日
     */
    private Boolean isSkipLbt;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartExcludeDate() {
        return startExcludeDate;
    }

    public void setStartExcludeDate(String startExcludeDate) {
        this.startExcludeDate = startExcludeDate;
    }

    public String getEndExcludeDate() {
        return endExcludeDate;
    }

    public void setEndExcludeDate(String endExcludeDate) {
        this.endExcludeDate = endExcludeDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSkipLbt() {
        return isSkipLbt;
    }

    public void setSkipLbt(Boolean skipLbt) {
        isSkipLbt = skipLbt;
    }
}
