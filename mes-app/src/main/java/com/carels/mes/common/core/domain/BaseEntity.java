package com.carels.mes.common.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer"}, ignoreUnknown = true)
public class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 搜索值 */
    private String searchValue;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新者 */
    private String updateBy;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    /** 备注 */
    private String remark;
    
    /** 请求参数 */
    private String params;
    
    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    
    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
