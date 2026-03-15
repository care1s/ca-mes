package com.carels.mes.module.md.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物料实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MdItem extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 物料ID */
    private Long itemId;
    
    /** 物料编码 */
    private String itemCode;
    
    /** 物料名称 */
    private String itemName;
    
    /** 物料类型ID */
    private Long itemTypeId;
    
    /** 物料类型名称 */
    private String itemTypeName;
    
    /** 规格型号 */
    private String specification;
    
    /** 计量单位ID */
    private Long unitId;
    
    /** 计量单位名称 */
    private String unitName;
    
    /** 物料属性: RAW-原材料, SEMI-半成品, PRODUCT-产成品 */
    private String itemAttr;
    
    /** 条码类型: NONE-无条码, BATCH-批次条码, SN-序列号 */
    private String barcodeType;
    
    /** 默认仓库ID */
    private Long warehouseId;
    
    /** 默认仓库名称 */
    private String warehouseName;
    
    /** 默认库位ID */
    private Long locationId;
    
    /** 默认库位名称 */
    private String locationName;
    
    /** 安全库存 */
    private Double safetyStock;
    
    /** 最大库存 */
    private Double maxStock;
    
    /** 最小库存 */
    private Double minStock;
    
    /** 采购单价 */
    private Double purchasePrice;
    
    /** 销售单价 */
    private Double salePrice;
    
    /** 标准成本 */
    private Double standardCost;
    
    /** 保质期(天) */
    private Integer shelfLife;
    
    /** 启用状态: 0-启用, 1-停用 */
    private String status;
    
    /** 检验标志: Y-需要检验, N-不需要 */
    private String inspectFlag;
    
    /** 默认检验模板ID */
    private Long inspectTemplateId;
    
    /** 图号 */
    private String drawingNo;
    
    /** 品牌 */
    private String brand;
    
    /** 生产厂家 */
    private String manufacturer;
}
