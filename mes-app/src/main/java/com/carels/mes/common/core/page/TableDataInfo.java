package com.carels.mes.common.core.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据信息 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
public class TableDataInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 状态码 */
    private int code;
    
    /** 消息 */
    private String msg;
    
    /** 总记录数 */
    private long total;
    
    /** 列表数据 */
    private List<?> rows;
    
    public TableDataInfo() {}
    
    public TableDataInfo(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }
}
