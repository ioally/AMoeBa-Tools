package com.ioally.amoeba.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchExecuteDto extends BaseRequestDto {

    /**
     * 上午上班
     */
    private String amStartTime;
    /**
     * 上午下班
     */
    private String amEndTime;
    /**
     * 下午上班
     */
    private String pmStartTime;
    /**
     * 下午下班
     */
    private String pmEndTime;
    /**
     * 日志信息集合
     */
    private List<LogInfoDto> logInfoDtos;

    public String getAmStartTime() {
        return amStartTime;
    }

    public void setAmStartTime(String amStartTime) {
        this.amStartTime = amStartTime;
    }

    public String getAmEndTime() {
        return amEndTime;
    }

    public void setAmEndTime(String amEndTime) {
        this.amEndTime = amEndTime;
    }

    public String getPmStartTime() {
        return pmStartTime;
    }

    public void setPmStartTime(String pmStartTime) {
        this.pmStartTime = pmStartTime;
    }

    public String getPmEndTime() {
        return pmEndTime;
    }

    public void setPmEndTime(String pmEndTime) {
        this.pmEndTime = pmEndTime;
    }

    public List<LogInfoDto> getLogInfoDtos() {
        return logInfoDtos;
    }

    public void setLogInfoDtos(List<LogInfoDto> logInfoDtos) {
        this.logInfoDtos = logInfoDtos;
    }
}
