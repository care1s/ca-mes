package com.carels.mes.common.core.web.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Controller基类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
public class BaseController {
    
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";
    
    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";
    
    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";
    
    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";
    
    /**
     * 默认每页显示条数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 默认当前页
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    
    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageHelper.startPage(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
    }
    
    /**
     * 设置请求分页数据
     */
    protected void startPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
    }
    
    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(AjaxResult.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }
    
    /**
     * 响应返回结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
    
    /**
     * 响应返回结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? AjaxResult.success() : AjaxResult.error();
    }
}
