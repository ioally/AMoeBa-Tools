package com.ioally.amoeba.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ioally.amoeba.utils.other.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AMoeBaLogDto extends BaseRequestDto implements Comparable {

    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date dateStart;
    private String id;
    @JsonProperty("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date dateEnd;
    private String text;
    private String userEmployeeId;
    private String recType;
    private String isSync;
    private String dateRange;

    public AMoeBaLogDto() {
    }

    public AMoeBaLogDto(String id) {
        this.id = id;
    }

    @JSONField
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateStart() {
        return this.dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return this.dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getUserEmployeeId() {
        return this.userEmployeeId;
    }

    public void setUserEmployeeId(String userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
    }

    public String getRecType() {
        return this.recType;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }

    public String getIsSync() {
        return this.isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String toHttpString() throws UnsupportedEncodingException {
        StringBuilder httpStr = new StringBuilder("id=").append(this.id);

        if (this.dateStart != null) {
            httpStr.append("&dateStart=").append(URLEncoder.encode(DateUtil.sdf_en.format(this.dateStart), "UTF-8"));
        }

        if (this.dateEnd != null) {
            httpStr.append("&dateEnd=").append(URLEncoder.encode(DateUtil.sdf_en.format(this.dateEnd), "UTF-8"));
        }
        String tempString;
        if (StringUtils.isNotEmpty(this.text)) {
            tempString = URLEncoder.encode(this.text, "UTF-8");
            httpStr.append("&text=").append(tempString);
        }
        httpStr.append("&userEmployeeId=").append(this.userEmployeeId)
                .append("&recType=").append(this.recType == null ? "" : this.recType)
                .append("&isSync=").append(this.isSync == null ? "" : this.isSync);
        return httpStr.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof AMoeBaLogDto) {
            AMoeBaLogDto aMoeBaLogDto = (AMoeBaLogDto) o;
            Date dateStart = aMoeBaLogDto.getDateStart();
            if (this.dateStart != null) {
                int start = this.dateStart.compareTo(dateStart);
                if (start == 0) {
                    Date dateEnd = aMoeBaLogDto.getDateEnd();
                    if (this.dateEnd != null) {
                        return this.dateEnd.compareTo(dateEnd);
                    }
                } else {
                    return start;
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "AMoeBaLogDto{" +
                "dateStart=" + dateStart +
                ", id='" + id + '\'' +
                ", dateEnd=" + dateEnd +
                ", text='" + text + '\'' +
                ", userEmployeeId='" + userEmployeeId + '\'' +
                ", recType='" + recType + '\'' +
                ", isSync='" + isSync + '\'' +
                '}';
    }
}
