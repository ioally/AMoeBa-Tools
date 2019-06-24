package com.ioally.amoeba.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class HolidayDto {

    /**
     * 节假日id
     */
    private Integer holidayId;

    /**
     * 节假日年度
     */
    private Integer holidayYear;

    /**
     * 节假日名称
     */
    private String holidayName;

    /**
     * 调休放假策略
     */
    private String holidayStrategy;

    /**
     * 放假天数
     */
    private Integer holidayDays;

    /**
     * 具体放假日期集合
     */
    private List<Date> holidayDate = new ArrayList<>();

    public Integer getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Integer holidayId) {
        this.holidayId = holidayId;
    }

    public Integer getHolidayYear() {
        return holidayYear;
    }

    public void setHolidayYear(Integer holidayYear) {
        this.holidayYear = holidayYear;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getHolidayStrategy() {
        return holidayStrategy;
    }

    public void setHolidayStrategy(String holidayStrategy) {
        this.holidayStrategy = holidayStrategy;
    }

    public Integer getHolidayDays() {
        return holidayDays;
    }

    public void setHolidayDays(Integer holidayDays) {
        this.holidayDays = holidayDays;
    }

    public List<Date> getHolidayDate() {
        return holidayDate;
    }

    @Override
    public String toString() {
        return "HolidayDto{" +
                "holidayId=" + holidayId +
                ", holidayYear=" + holidayYear +
                ", holidayName='" + holidayName + '\'' +
                ", holidayStrategy='" + holidayStrategy + '\'' +
                ", holidayDays=" + holidayDays +
                ", holidayDate=" + holidayDate +
                '}';
    }
}
