package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.web.controller.BaseController;
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
    
    /**
     * 获取物料类型树
     */
    @GetMapping("/tree")
    public AjaxResult tree() {
        // 模拟数据
        List<Map<String, Object>> treeData = new ArrayList<>();
        
        Map<String, Object> rawMaterial = new HashMap<>();
        rawMaterial.put("typeId", 1);
        rawMaterial.put("typeName", "原材料");
        rawMaterial.put("itemAttr", "RAW");
        rawMaterial.put("count", 68);
        
        List<Map<String, Object>> rawChildren = new ArrayList<>();
        Map<String, Object> metal = new HashMap<>();
        metal.put("typeId", 11);
        metal.put("typeName", "金属材料");
        metal.put("itemAttr", "RAW");
        metal.put("count", 30);
        rawChildren.add(metal);
        
        Map<String, Object> plastic = new HashMap<>();
        plastic.put("typeId", 12);
        plastic.put("typeName", "塑料材料");
        plastic.put("itemAttr", "RAW");
        plastic.put("count", 25);
        rawChildren.add(plastic);
        
        Map<String, Object> electronic = new HashMap<>();
        electronic.put("typeId", 13);
        electronic.put("typeName", "电子元件");
        electronic.put("itemAttr", "RAW");
        electronic.put("count", 13);
        rawChildren.add(electronic);
        
        rawMaterial.put("children", rawChildren);
        treeData.add(rawMaterial);
        
        Map<String, Object> semiProduct = new HashMap<>();
        semiProduct.put("typeId", 2);
        semiProduct.put("typeName", "半成品");
        semiProduct.put("itemAttr", "SEMI");
        semiProduct.put("count", 45);
        
        List<Map<String, Object>> semiChildren = new ArrayList<>();
        Map<String, Object> componentA = new HashMap<>();
        componentA.put("typeId", 21);
        componentA.put("typeName", "组件A");
        componentA.put("itemAttr", "SEMI");
        componentA.put("count", 20);
        semiChildren.add(componentA);
        
        Map<String, Object> componentB = new HashMap<>();
        componentB.put("typeId", 22);
        componentB.put("typeName", "组件B");
        componentB.put("itemAttr", "SEMI");
        componentB.put("count", 25);
        semiChildren.add(componentB);
        
        semiProduct.put("children", semiChildren);
        treeData.add(semiProduct);
        
        Map<String, Object> product = new HashMap<>();
        product.put("typeId", 3);
        product.put("typeName", "产成品");
        product.put("itemAttr", "PRODUCT");
        product.put("count", 43);
        
        List<Map<String, Object>> productChildren = new ArrayList<>();
        Map<String, Object> productA = new HashMap<>();
        productA.put("typeId", 31);
        productA.put("typeName", "A类产品");
        productA.put("itemAttr", "PRODUCT");
        productA.put("count", 23);
        productChildren.add(productA);
        
        Map<String, Object> productB = new HashMap<>();
        productB.put("typeId", 32);
        productB.put("typeName", "B类产品");
        productB.put("itemAttr", "PRODUCT");
        productB.put("count", 20);
        productChildren.add(productB);
        
        product.put("children", productChildren);
        treeData.add(product);
        
        return AjaxResult.success(treeData);
    }
    
    /**
     * 获取物料类型列表
     */
    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(new ArrayList<>());
    }
}
