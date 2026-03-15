package com.carels.mes.common.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用响应结果
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
public class AjaxResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 状态码 */
    private int code;
    
    /** 返回消息 */
    private String msg;
    
    /** 返回数据 */
    private Object data;
    
    /** 总记录数（分页用） */
    private Long total;
    
    /** 分页数据 */
    private Object rows;
    
    /** 成功状态码 */
    public static final int SUCCESS = 200;
    
    /** 失败状态码 */
    public static final int ERROR = 500;
    
    /** 未授权状态码 */
    public static final int UNAUTHORIZED = 401;
    
    /** 禁止访问状态码 */
    public static final int FORBIDDEN = 403;
    
    public AjaxResult() {}
    
    public AjaxResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    /**
     * 返回成功消息
     */
    public static AjaxResult success() {
        return new AjaxResult(SUCCESS, "操作成功", null);
    }
    
    /**
     * 返回成功消息
     */
    public static AjaxResult success(String msg) {
        return new AjaxResult(SUCCESS, msg, null);
    }
    
    /**
     * 返回成功消息和数据
     */
    public static AjaxResult success(Object data) {
        return new AjaxResult(SUCCESS, "操作成功", data);
    }
    
    /**
     * 返回成功消息和数据
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(SUCCESS, msg, data);
    }
    
    /**
     * 返回错误消息
     */
    public static AjaxResult error() {
        return new AjaxResult(ERROR, "操作失败", null);
    }
    
    /**
     * 返回错误消息
     */
    public static AjaxResult error(String msg) {
        return new AjaxResult(ERROR, msg, null);
    }
    
    /**
     * 返回错误消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }
    
    /**
     * 返回分页数据
     */
    public static AjaxResult page(Long total, Object rows) {
        AjaxResult result = new AjaxResult();
        result.setCode(SUCCESS);
        result.setMsg("查询成功");
        result.setTotal(total);
        result.setRows(rows);
        return result;
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code == SUCCESS;
    }
}
