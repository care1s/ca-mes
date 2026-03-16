package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 物料类型管理Controller - carels
 * 
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */
@RestController
@RequestMapping("/mes/md/itemType")
public class MdItemTypeController extends BaseController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 获取物料类型树
     */
    @GetMapping("/tree")
    public AjaxResult tree() {
        // 查询所有物料类型
        List<Map<String, Object>> typeList = jdbcTemplate.queryForList(
            "SELECT type_id, type_code, type_name, parent_id, type_attr FROM md_item_type ORDER BY type_id"
        );
        
        // 统计每个类型的物料数量
        Map<Long, Integer> typeCountMap = new HashMap<>();
        List<Map<String, Object>> countList = jdbcTemplate.queryForList(
            "SELECT item_type_id, COUNT(*) as cnt FROM md_item WHERE item_type_id IS NOT NULL GROUP BY item_type_id"
        );
        for (Map<String, Object> count : countList) {
            Object typeIdObj = count.get("item_type_id");
            if (typeIdObj != null) {
                Long typeId = Long.valueOf(typeIdObj.toString());
                Integer cnt = Integer.valueOf(count.get("cnt").toString());
                typeCountMap.put(typeId, cnt);
            }
        }
        
        // 构建树形结构
        List<Map<String, Object>> treeData = new ArrayList<>();
        Map<Long, Map<String, Object>> typeMap = new HashMap<>();
        
        // 先创建所有类型节点
        for (Map<String, Object> type : typeList) {
            Long typeId = Long.valueOf(type.get("type_id").toString());
            String typeName = (String) type.get("type_name");
            String typeAttr = (String) type.get("type_attr");
            Long parentId = type.get("parent_id") == null ? 0L : Long.valueOf(type.get("parent_id").toString());
            Integer count = typeCountMap.getOrDefault(typeId, 0);
            
            Map<String, Object> node = new HashMap<>();
            node.put("typeId", typeId);
            node.put("typeName", typeName);
            node.put("itemAttr", typeAttr);
            node.put("count", count);
            node.put("children", new ArrayList<>());
            node.put("parentId", parentId);
            
            typeMap.put(typeId, node);
        }
        
        // 构建树结构
        for (Map<String, Object> node : typeMap.values()) {
            Long parentId = (Long) node.get("parentId");
            if (parentId == 0 || parentId == null) {
                // 顶级节点
                treeData.add(node);
            } else {
                // 子节点
                Map<String, Object> parent = typeMap.get(parentId);
                if (parent != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
                    children.add(node);
                }
            }
        }
        
        // 如果没有数据，返回空列表
        if (treeData.isEmpty()) {
            // 返回默认结构
            treeData = getDefaultTreeData();
        }
        
        return AjaxResult.success(treeData);
    }
    
    /**
     * 获取物料类型列表
     */
    @GetMapping("/list")
    public AjaxResult list() {
        List<Map<String, Object>> typeList = jdbcTemplate.queryForList(
            "SELECT type_id, type_name, type_attr FROM md_item_type ORDER BY type_id"
        );
        return AjaxResult.success(typeList);
    }
    
    /**
     * 新增物料类型
     */
    @PostMapping
    public AjaxResult add(@RequestBody Map<String, Object> typeData) {
        String typeCode = (String) typeData.get("typeCode");
        String typeName = (String) typeData.get("typeName");
        String itemAttr = (String) typeData.get("itemAttr");
        Long parentId = typeData.get("parentId") == null ? 0L : Long.valueOf(typeData.get("parentId").toString());
        
        // 检查编码是否已存在
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM md_item_type WHERE type_code = ?",
            Integer.class,
            typeCode
        );
        if (count != null && count > 0) {
            return AjaxResult.error("类型编码已存在");
        }
        
        // 插入新类型
        jdbcTemplate.update(
            "INSERT INTO md_item_type (type_code, type_name, parent_id, type_attr, status) VALUES (?, ?, ?, ?, '0')",
            typeCode,
            typeName,
            parentId,
            itemAttr
        );
        
        return AjaxResult.success("新增成功");
    }
    
    /**
     * 修改物料类型
     */
    @PutMapping
    public AjaxResult update(@RequestBody Map<String, Object> typeData) {
        Long typeId = Long.valueOf(typeData.get("typeId").toString());
        String typeCode = (String) typeData.get("typeCode");
        String typeName = (String) typeData.get("typeName");
        String itemAttr = (String) typeData.get("itemAttr");
        
        // 检查编码是否被其他类型使用
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM md_item_type WHERE type_code = ? AND type_id != ?",
            Integer.class,
            typeCode,
            typeId
        );
        if (count != null && count > 0) {
            return AjaxResult.error("类型编码已存在");
        }
        
        // 更新类型
        jdbcTemplate.update(
            "UPDATE md_item_type SET type_code = ?, type_name = ?, type_attr = ? WHERE type_id = ?",
            typeCode,
            typeName,
            itemAttr,
            typeId
        );
        
        return AjaxResult.success("修改成功");
    }
    
    /**
     * 删除物料类型
     */
    @DeleteMapping("/{typeId}")
    public AjaxResult delete(@PathVariable Long typeId) {
        // 检查是否有物料使用该类型
        Integer itemCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM md_item WHERE item_type_id = ?",
            Integer.class,
            typeId
        );
        if (itemCount != null && itemCount > 0) {
            return AjaxResult.error("该类型下存在物料，无法删除");
        }
        
        // 检查是否有子类型
        Integer childCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM md_item_type WHERE parent_id = ?",
            Integer.class,
            typeId
        );
        if (childCount != null && childCount > 0) {
            return AjaxResult.error("该类型下存在子类型，无法删除");
        }
        
        jdbcTemplate.update("DELETE FROM md_item_type WHERE type_id = ?", typeId);
        return AjaxResult.success("删除成功");
    }
    
    /**
     * 默认树形数据（当数据库为空时）
     */
    private List<Map<String, Object>> getDefaultTreeData() {
        List<Map<String, Object>> treeData = new ArrayList<>();
        
        // 原材料
        Map<String, Object> rawMaterial = new HashMap<>();
        rawMaterial.put("typeId", 1);
        rawMaterial.put("typeName", "原材料");
        rawMaterial.put("itemAttr", "RAW");
        rawMaterial.put("count", 0);
        rawMaterial.put("children", new ArrayList<>());
        treeData.add(rawMaterial);
        
        // 半成品
        Map<String, Object> semiProduct = new HashMap<>();
        semiProduct.put("typeId", 2);
        semiProduct.put("typeName", "半成品");
        semiProduct.put("itemAttr", "SEMI");
        semiProduct.put("count", 0);
        semiProduct.put("children", new ArrayList<>());
        treeData.add(semiProduct);
        
        // 产成品
        Map<String, Object> product = new HashMap<>();
        product.put("typeId", 3);
        product.put("typeName", "产成品");
        product.put("itemAttr", "PRODUCT");
        product.put("count", 0);
        product.put("children", new ArrayList<>());
        treeData.add(product);
        
        return treeData;
    }
}
