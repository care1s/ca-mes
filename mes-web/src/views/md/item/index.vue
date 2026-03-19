<template>
  <div class="app-container">
    <!-- 简洁页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-goods"></i>
        <span class="title">物料信息管理</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增物料</el-button>
        <el-button type="danger" icon="el-icon-delete" size="small" :disabled="selectedItems.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button icon="el-icon-download" size="small">导出</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <el-row :gutter="15" class="main-content">
      <!-- 左侧物料类型树 -->
      <el-col :span="4" class="tree-col">
        <el-card class="tree-card" shadow="never" :body-style="{ padding: '0' }">
          <div slot="header" class="tree-header">
            <span><i class="el-icon-s-grid"></i> 物料类型</span>
          </div>
          <div class="tree-search">
            <el-input
              v-model="filterText"
              placeholder="搜索类型"
              size="small"
              prefix-icon="el-icon-search"
              clearable
            />
          </div>
          <el-tree
            ref="tree"
            class="filter-tree"
            :data="typeTreeData"
            :props="defaultProps"
            :filter-node-method="filterNode"
            :expand-on-click-node="false"
            highlight-current
            node-key="typeId"
            default-expand-all
            @node-click="handleNodeClick"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <span class="node-content" :class="{ active: currentTypeId === data.typeId }">
                <i :class="getTypeIcon(data.itemAttr)" :style="{ color: getTypeColor(data.itemAttr) }"></i>
                <span class="node-label">{{ node.label }}</span>
                <span class="node-count">{{ data.count }}</span>
              </span>
              <span class="node-actions">
                <el-tooltip content="添加子类型" placement="top">
                  <i class="el-icon-circle-plus-outline" @click.stop="handleAddType(data.typeId)"></i>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <i class="el-icon-edit" @click.stop="handleEditType(data)"></i>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <i class="el-icon-delete" @click.stop="handleDeleteType(data)"></i>
                </el-tooltip>
              </span>
            </span>
          </el-tree>
        </el-card>
      </el-col>

      <!-- 右侧物料列表 -->
      <el-col :span="20" class="table-col">
        <!-- 面包屑和搜索 -->
        <el-card class="filter-card" shadow="never" :body-style="{ padding: '12px 15px' }">
          <div class="filter-row">
            <div class="breadcrumb-wrap">
              <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                  <span class="breadcrumb-link" @click="clearTypeFilter">全部物料</span>
                </el-breadcrumb-item>
                <el-breadcrumb-item v-if="breadcrumbPath.length > 0">
                  <el-dropdown v-if="breadcrumbPath.length > 1" trigger="click">
                    <span class="el-dropdown-link">
                      {{ breadcrumbPath[breadcrumbPath.length - 1].typeName }}<i class="el-icon-arrow-down"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-for="(item, idx) in breadcrumbPath" :key="idx" @click.native="jumpToType(item.typeId)">
                        {{ item.typeName }}
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                  <span v-else>{{ breadcrumbPath[0].typeName }}</span>
                </el-breadcrumb-item>
              </el-breadcrumb>
              <el-tag v-if="currentTypeName" size="small" closable @close="clearTypeFilter" type="info" effect="plain" style="margin-left: 10px;">
                {{ currentTypeName }}
              </el-tag>
            </div>
            <div class="search-wrap">
              <el-input
                v-model="queryParams.keyword"
                placeholder="搜索物料编码/名称/规格"
                size="small"
                clearable
                style="width: 220px"
                @keyup.enter.native="handleQuery"
              />
              <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
              <el-button icon="el-icon-refresh-right" size="small" @click="resetQuery">重置</el-button>
            </div>
          </div>
        </el-card>

        <!-- 扁平统计栏 -->
        <div class="stat-bar">
          <div class="stat-item">
            <span class="stat-label">当前类型</span>
            <span class="stat-value">{{ currentTypeName || '全部' }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">物料总数</span>
            <span class="stat-value blue">{{ statistics.total }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">启用</span>
            <span class="stat-value green">{{ statistics.active }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">库存预警</span>
            <span class="stat-value orange">{{ statistics.lowStock }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">停用</span>
            <span class="stat-value gray">{{ statistics.inactive }}</span>
          </div>
        </div>

        <!-- 数据表格 -->
        <el-card class="table-card" shadow="never" :body-style="{ padding: '0', display: 'flex', flexDirection: 'column', height: '100%' }">
          <div class="table-scroll-wrapper">
            <el-table
              ref="itemTable"
              v-loading="loading"
              :data="tableData"
              border
              stripe
              highlight-current-row
              size="small"
              style="width: 100%;"
              :height="tableHeight"
              @selection-change="handleSelectionChange"
              @row-dblclick="handleRowDblClick"
              class="md-item-table"
            >
              <el-table-column type="selection" width="55" align="center" fixed="left" />
              <el-table-column type="index" label="序号" width="50" align="center" fixed="left" />
              <el-table-column prop="itemCode" label="物料编码" min-width="110" show-overflow-tooltip fixed="left">
                <template slot-scope="scope">
                  <el-link type="primary" @click="handleView(scope.row)">{{ scope.row.itemCode }}</el-link>
                </template>
              </el-table-column>
              <el-table-column prop="itemName" label="物料名称" min-width="150" show-overflow-tooltip />
              <el-table-column label="所属类型" min-width="90" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="getTypeTagType(scope.row.itemAttr)" effect="plain">
                    {{ scope.row.itemTypeName }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="specification" label="规格型号" min-width="120" show-overflow-tooltip />
              <el-table-column prop="unitName" label="单位" min-width="60" align="center" />
              <el-table-column prop="safetyStock" label="安全库存" min-width="90" align="right" />
              <el-table-column prop="status" label="状态" min-width="70" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.status"
                    active-value="0"
                    inactive-value="1"
                    @change="handleStatusChange(scope.row)"
                  />
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="100" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
                  <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          
          <!-- 分页在表格下方 -->
          <div class="pagination-wrap">
            <el-pagination
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              :page-sizes="[20, 50, 100, 200]"
              :page-size="queryParams.pageSize"
              :current-page="queryParams.pageNum"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 物料详情弹框 -->
    <el-dialog 
      title="物料详情" 
      :visible.sync="detailDialogVisible" 
      width="600px" 
      :close-on-click-modal="true"
      :modal="false"
      custom-class="no-mask-dialog"
    >
      <div class="detail-content" v-if="currentRow">
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">物料编码：</span>
              <span class="detail-value">{{ currentRow.itemCode }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">物料名称：</span>
              <span class="detail-value">{{ currentRow.itemName }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">所属类型：</span>
              <span class="detail-value">
                <el-tag size="small" :type="getTypeTagType(currentRow.itemAttr)" effect="plain">
                  {{ currentRow.itemTypeName }}
                </el-tag>
              </span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">规格型号：</span>
              <span class="detail-value">{{ currentRow.specification || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">计量单位：</span>
              <span class="detail-value">{{ currentRow.unitName }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">安全库存：</span>
              <span class="detail-value">{{ currentRow.safetyStock }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">当前库存：</span>
              <span class="detail-value" :class="getStockClass(currentRow)">{{ currentRow.quantity }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">状态：</span>
              <span class="detail-value">
                <el-tag size="small" :type="currentRow.status === '0' ? 'success' : 'info'">
                  {{ currentRow.status === '0' ? '启用' : '停用' }}
                </el-tag>
              </span>
            </div>
          </el-col>
        </el-row>
        
        <div class="detail-section">
          <div class="detail-item block">
            <span class="detail-label">备注：</span>
            <span class="detail-value">{{ currentRow.remark || '-' }}</span>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentRow); detailDialogVisible = false">编 辑</el-button>
      </div>
    </el-dialog>

    <!-- 物料对话框 -->
    <el-dialog 
      :title="isEdit ? '编辑物料' : '新增物料'" 
      :visible.sync="dialogVisible" 
      width="780px" 
      :close-on-click-modal="false"
      :modal="false"
      custom-class="no-mask-dialog item-dialog"
    >
      <!-- 基本信息区域 -->
      <div class="basic-info-section">
        <div class="section-title">
          <i class="el-icon-info"></i>
          <span>基本信息</span>
        </div>
        <el-form :model="form" :rules="rules" ref="form" label-width="100px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="物料编码" prop="itemCode">
                <el-input v-model="form.itemCode" placeholder="请输入物料编码" :disabled="isEdit" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="物料名称" prop="itemName">
                <el-input v-model="form.itemName" placeholder="请输入物料名称" />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="物料类型" prop="itemTypeId">
                <!-- 多级类型使用级联选择器 -->
                <el-cascader
                  v-if="hasMultiLevelTypes"
                  v-model="itemTypePath"
                  :options="typeTreeData"
                  :props="{ value: 'typeId', label: 'typeName', children: 'children', checkStrictly: true }"
                  placeholder="选择物料类型"
                  style="width: 100%"
                  clearable
                  @change="handleTypeChange"
                />
                <!-- 单级类型使用普通下拉框 -->
                <el-select
                  v-else
                  v-model="form.itemTypeId"
                  placeholder="选择物料类型"
                  style="width: 100%"
                  @change="handleTypeChange"
                >
                  <el-option
                    v-for="type in typeTreeData"
                    :key="type.typeId"
                    :label="type.typeName"
                    :value="type.typeId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计量单位" prop="unitName">
                <el-select v-model="form.unitName" placeholder="选择单位" style="width: 100%" filterable allow-create>
                  <el-option v-for="unit in unitOptions" :key="unit" :label="unit" :value="unit" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="规格型号">
                <el-input v-model="form.specification" placeholder="请输入规格型号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="启用安全库存">
                <el-switch
                  v-model="form.enableSafetyStock"
                  active-text="启用"
                  inactive-text="不启用"
                  @change="handleSafetyStockChange"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20" v-if="form.enableSafetyStock">
            <el-col :span="12">
              <el-form-item label="安全库存">
                <el-input-number v-model="form.safetyStock" :min="0" :precision="2" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最大库存">
                <el-input-number v-model="form.maxStock" :min="0" :precision="2" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="备注">
            <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 子标签页区域 -->
      <div class="sub-tabs-section">
        <el-tabs v-model="activeSubTab" type="card">
          <el-tab-pane label="BOM物料清单" name="bom">
            <div class="bom-section">
              <div class="bom-header">
                <span class="bom-title">子物料组成</span>
                <el-button type="primary" size="small" icon="el-icon-plus" @click="addBomLine">添加物料</el-button>
              </div>
              <div class="bom-table-wrapper">
                <el-table :data="form.bomLines" border size="small" class="bom-table" style="width: 100%">
                  <el-table-column type="index" label="序号" width="100" align="center" />
                  <el-table-column label="物料编码" width="180">
                    <template slot-scope="scope">
                      <span class="bom-item-code">{{ scope.row.itemCode }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="物料名称" min-width="220" show-overflow-tooltip>
                    <template slot-scope="scope">
                      <span>{{ scope.row.itemName }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="规格型号" min-width="180" show-overflow-tooltip>
                    <template slot-scope="scope">
                      <span>{{ scope.row.specification || '-' }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="单位" width="80" align="center">
                    <template slot-scope="scope">
                      <span>{{ scope.row.unitName || '-' }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="用量" width="180" align="center">
                    <template slot-scope="scope">
                      <el-input-number 
                        v-model="scope.row.quantity" 
                        :min="0.01" 
                        :precision="2" 
                        size="small"
                        style="width: 150px"
                        controls-position="right"
                      />
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80" align="center">
                    <template slot-scope="scope">
                      <el-button type="text" size="small" style="color: #f56c6c" @click="removeBomLine(scope.$index)">
                        <i class="el-icon-delete"></i>
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <div v-if="form.bomLines.length === 0" class="bom-empty">
                <i class="el-icon-s-grid"></i>
                <span>暂无BOM物料，点击上方按钮添加</span>
              </div>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="SIP检验标准" name="sip">
            <div class="empty-placeholder">
              <i class="el-icon-document-checked"></i>
              <span>SIP检验标准功能开发中...</span>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="SOP作业指导" name="sop">
            <div class="empty-placeholder">
              <i class="el-icon-document"></i>
              <span>SOP作业指导功能开发中...</span>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="工艺参数" name="param">
            <div class="empty-placeholder">
              <i class="el-icon-setting"></i>
              <span>工艺参数功能开发中...</span>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
      </div>
    </el-dialog>

    <!-- 类型对话框 -->
    <el-dialog 
      :title="isEditType ? '编辑类型' : '新增类型'" 
      :visible.sync="typeDialogVisible" 
      width="500px"
      :modal="false"
      custom-class="no-mask-dialog"
    >
      <el-form :model="typeForm" :rules="typeRules" ref="typeForm" label-width="100px">
        <el-form-item label="上级类型">
          <span v-if="typeForm.parentName">{{ typeForm.parentName }}</span>
          <span v-else style="color: #909399;">顶级类型</span>
        </el-form-item>
        <el-form-item label="类型编码" prop="typeCode">
          <el-input v-model="typeForm.typeCode" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="typeForm.typeName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="物料属性" prop="itemAttr">
          <el-radio-group v-model="typeForm.itemAttr">
            <el-radio label="RAW">原材料</el-radio>
            <el-radio label="SEMI">半成品</el-radio>
            <el-radio label="PRODUCT">产成品</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="typeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitTypeForm">保 存</el-button>
      </div>
    </el-dialog>
    
    <!-- BOM物料选择弹窗 -->
    <el-dialog
      title="选择物料"
      :visible.sync="bomItemDialogVisible"
      width="850px"
      :modal="false"
      custom-class="no-mask-dialog"
    >
      <div class="bom-item-search">
        <el-input
          v-model="bomItemSearchKeyword"
          placeholder="搜索物料编码/名称/规格"
          size="small"
          prefix-icon="el-icon-search"
          clearable
          @keyup.enter.native="filterBomItems"
        />
      </div>
      <el-table
        :data="filteredBomItems"
        border
        size="small"
        highlight-current-row
        @current-change="handleBomItemSelect"
        v-loading="bomItemDialogLoading"
        height="350"
        style="width: 100%"
      >
        <el-table-column width="70" align="center">
          <template slot-scope="scope">
            <el-radio v-model="selectedBomItem" :label="scope.row" class="hidden-radio">
              <span></span>
            </el-radio>
          </template>
        </el-table-column>
        <el-table-column prop="itemCode" label="物料编码" width="180" />
        <el-table-column prop="itemName" label="物料名称" min-width="220" show-overflow-tooltip />
        <el-table-column prop="specification" label="规格型号" min-width="180" show-overflow-tooltip />
        <el-table-column prop="unitName" label="单位" width="80" align="center" />
      </el-table>
      <div slot="footer">
        <el-button @click="bomItemDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmAddBomItem" :disabled="!selectedBomItem">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  listItem, 
  addItem, 
  updateItem, 
  batchDelItem,
  exportItem,
  getItemTypeTree,
  addItemType,
  updateItemType,
  delItemType
} from '@/api/md'

export default {
  name: 'MdItem',
  data() {
    return {
      loading: false,
      total: 0,
      filterText: '',
      currentTypeId: null,
      currentTypeName: '',
      breadcrumbPath: [],
      tableHeight: 500,
      
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        keyword: ''
      },
      
      statistics: {
        total: 0,
        active: 0,
        lowStock: 0,
        inactive: 0
      },
      
      typeTreeData: [],
      
      availableBomItems: [],
      
      defaultProps: {
        children: 'children',
        label: 'typeName'
      },
      
      tableData: [],
      selectedItems: [],
      
      dialogVisible: false,
      isEdit: false,
      activeSubTab: 'bom',
      form: {
        itemId: null,
        itemCode: '',
        itemName: '',
        itemTypeId: null,
        specification: '',
        unitName: '',
        enableSafetyStock: false,
        safetyStock: 0,
        maxStock: 0,
        remark: '',
        bomLines: []
      },
      rules: {
        itemCode: [{ required: true, message: '请输入物料编码', trigger: 'blur' }],
        itemName: [{ required: true, message: '请输入物料名称', trigger: 'blur' }],
        itemTypeId: [{ required: true, message: '请选择物料类型', trigger: 'change' }],
        unitName: [{ required: true, message: '请选择单位', trigger: 'change' }]
      },
      
      // 详情弹框
      detailDialogVisible: false,
      currentRow: null,
      
      unitOptions: ['PC', 'KG', 'M', 'SET', 'BOX', 'L', 'ML', 'G'],
      
      // BOM物料选择弹窗
      bomItemDialogVisible: false,
      selectedBomItem: null,
      bomItemSearchKeyword: '',
      bomItemDialogLoading: false,
      
      typeDialogVisible: false,
      isEditType: false,
      typeForm: {
        typeId: null,
        parentId: 0,
        parentName: '',
        typeCode: '',
        typeName: '',
        itemAttr: 'RAW'
      },
      typeRules: {
        typeCode: [{ required: true, message: '请输入编码', trigger: 'blur' }],
        typeName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        itemAttr: [{ required: true, message: '请选择属性', trigger: 'change' }]
      }
    }
  },
  
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  
  mounted() {
    this.fetchTypeTree()
    this.fetchData()
    this.calcTableHeight()
    window.addEventListener('resize', this.calcTableHeight)
  },
  
  beforeDestroy() {
    window.removeEventListener('resize', this.calcTableHeight)
  },
  
  computed: {
    filteredBomItems() {
      if (!this.bomItemSearchKeyword) {
        return this.availableBomItems
      }
      const keyword = this.bomItemSearchKeyword.toLowerCase()
      return this.availableBomItems.filter(item => 
        item.itemCode.toLowerCase().includes(keyword) ||
        item.itemName.toLowerCase().includes(keyword) ||
        (item.specification && item.specification.toLowerCase().includes(keyword))
      )
    },
    
    // 判断是否有多级类型
    hasMultiLevelTypes() {
      // 递归检查是否有任何类型有子级
      const checkChildren = (nodes) => {
        for (let node of nodes) {
          if (node.children && node.children.length > 0) {
            return true
          }
        }
        return false
      }
      return checkChildren(this.typeTreeData)
    },
    
    // 级联选择器的值（路径数组）
    itemTypePath: {
      get() {
        // 根据 itemTypeId 查找完整路径
        if (!this.form.itemTypeId || !this.typeTreeData.length) return []
        return this.findTypePath(this.typeTreeData, this.form.itemTypeId)
      },
      set(val) {
        // 级联选择器改变时，更新 itemTypeId
        if (Array.isArray(val) && val.length > 0) {
          this.form.itemTypeId = val[val.length - 1]
        } else {
          this.form.itemTypeId = null
        }
      }
    }
  },
  
  methods: {
    calcTableHeight() {
      this.tableHeight = window.innerHeight - 340
    },
    
    // 获取物料类型树
    async fetchTypeTree() {
      try {
        const res = await getItemTypeTree()
        this.typeTreeData = res.data || []
      } catch (error) {
        this.$message.error('获取物料类型失败')
      }
    },
    
    // 获取物料列表
    async fetchData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.queryParams.pageNum,
          pageSize: this.queryParams.pageSize,
          itemTypeId: this.currentTypeId,
          keyword: this.queryParams.keyword
        }
        console.log('请求参数:', params)
        console.log('Token:', localStorage.getItem('mes-token'))
        const res = await listItem(params)
        console.log('响应结果:', res)
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.updateStatistics()
      } catch (error) {
        console.error('获取物料列表失败:', error)
        this.$message.error('获取物料列表失败')
      } finally {
        this.loading = false
      }
    },
    
    // 更新统计信息
    updateStatistics() {
      const total = this.tableData.length
      const active = this.tableData.filter(item => item.status === '0').length
      const inactive = this.tableData.filter(item => item.status === '1').length
      const lowStock = this.tableData.filter(item => item.quantity <= item.safetyStock).length
      this.statistics = { total, active, lowStock, inactive }
    },
    
    filterNode(value, data) {
      if (!value) return true
      return data.typeName.indexOf(value) !== -1
    },
    
    handleNodeClick(data) {
      this.currentTypeId = data.typeId
      this.currentTypeName = data.typeName
      this.buildBreadcrumb(data.typeId)
      this.fetchData()
    },
    
    buildBreadcrumb(typeId) {
      this.breadcrumbPath = []
      this.findPath(this.typeTreeData, typeId)
    },
    
    findPath(nodes, typeId, path = []) {
      for (let node of nodes) {
        if (node.typeId === typeId) {
          this.breadcrumbPath = [...path, node]
          return true
        }
        if (node.children && node.children.length > 0) {
          if (this.findPath(node.children, typeId, [...path, node])) {
            return true
          }
        }
      }
      return false
    },
    
    jumpToType(typeId) {
      this.$refs.tree.setCurrentKey(typeId)
      const node = this.findNode(this.typeTreeData, typeId)
      if (node) {
        this.handleNodeClick(node)
      }
    },
    
    findNode(nodes, typeId) {
      for (let node of nodes) {
        if (node.typeId === typeId) return node
        if (node.children) {
          const found = this.findNode(node.children, typeId)
          if (found) return found
        }
      }
      return null
    },
    
    // 查找类型的完整路径（返回 ID 数组）
    findTypePath(nodes, typeId, path = []) {
      for (let node of nodes) {
        if (node.typeId === typeId) {
          return [...path, node.typeId]
        }
        if (node.children && node.children.length > 0) {
          const result = this.findTypePath(node.children, typeId, [...path, node.typeId])
          if (result) return result
        }
      }
      return null
    },
    
    clearTypeFilter() {
      this.currentTypeId = null
      this.currentTypeName = ''
      this.breadcrumbPath = []
      this.$refs.tree.setCurrentKey(null)
      this.fetchData()
    },
    
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        keyword: ''
      }
      this.fetchData()
    },
    
    handleAdd() {
      this.isEdit = false
      // 自动生成物料编码：MAT + 年月日 + 4位随机数
      const date = new Date()
      const dateStr = date.getFullYear().toString().substr(2) + 
                     String(date.getMonth() + 1).padStart(2, '0') + 
                     String(date.getDate()).padStart(2, '0')
      const randomNum = Math.floor(1000 + Math.random() * 9000)
      const autoCode = 'MAT' + dateStr + randomNum
      
      this.form = {
        itemId: null,
        itemCode: autoCode,
        itemName: '',
        itemTypeId: this.currentTypeId,
        specification: '',
        unitName: '',
        enableSafetyStock: false,
        safetyStock: 0,
        maxStock: 0,
        remark: '',
        bomLines: []
      }
      // 加载可用的BOM物料（排除当前物料本身）
      this.loadAvailableBomItems()
      this.activeSubTab = 'bom'
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.isEdit = true
      this.form = { 
        ...row,
        bomLines: row.bomLines || []
      }
      // 加载可用的BOM物料
      this.loadAvailableBomItems()
      this.dialogVisible = true
    },
    
    // 双击行打开详情
    handleRowDblClick(row) {
      this.currentRow = row
      this.detailDialogVisible = true
    },
    
    handleView(row) {
      this.currentRow = row
      this.detailDialogVisible = true
    },
    
    // 多选框选择变化
    handleSelectionChange(selection) {
      this.selectedItems = selection
    },
    
    // 批量删除
    handleBatchDelete() {
      if (this.selectedItems.length === 0) {
        this.$message.warning('请先选择要删除的物料')
        return
      }
      
      const count = this.selectedItems.length
      const names = this.selectedItems.map(item => item.itemName).join('、')
      
      this.$confirm(`确定删除选中的 ${count} 个物料？\n${names.length > 50 ? names.substring(0, 50) + '...' : names}`, '提示', {
        type: 'warning',
        confirmButtonText: '确定删除',
        cancelButtonText: '取消'
      }).then(async () => {
        try {
          const itemIds = this.selectedItems.map(item => item.itemId)
          await batchDelItem(itemIds)
          this.$message.success(`成功删除 ${count} 个物料`)
          this.selectedItems = []
          this.fetchData()
        } catch (error) {
          this.$message.error('批量删除失败')
        }
      })
    },
    
    handleStatusChange(row) {
      const status = row.status === '0' ? '启用' : '停用'
      updateItem({ ...row, status: row.status }).then(() => {
        this.$message.success(`已${status}：${row.itemName}`)
      }).catch(() => {
        this.$message.error('状态更新失败')
      })
    },
    
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },
    
    handleTypeChange(val) {
      // cascader 返回数组，取最后一个值作为选中的类型ID
      if (Array.isArray(val) && val.length > 0) {
        this.form.itemTypeId = val[val.length - 1]
      } else {
        this.form.itemTypeId = val
      }
    },
    
    generateCode() {
      this.form.itemCode = 'MAT' + (1000 + Math.floor(Math.random() * 9000))
    },
    
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            // 过滤掉未选择物料的BOM行
            const submitData = {
              ...this.form,
              bomLines: this.form.bomLines.filter(line => line.itemId)
            }
            if (this.isEdit) {
              await updateItem(submitData)
              this.$message.success('更新成功')
            } else {
              await addItem(submitData)
              this.$message.success('新增成功')
            }
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error(this.isEdit ? '更新失败' : '新增失败')
          }
        }
      })
    },
    
    handleSafetyStockChange(val) {
      if (!val) {
        this.form.safetyStock = 0
        this.form.maxStock = 0
      }
    },
    
    // BOM操作
    addBomLine() {
      // 打开物料选择弹窗
      this.selectedBomItem = null
      this.bomItemSearchKeyword = ''
      this.bomItemDialogVisible = true
    },
    
    confirmAddBomItem() {
      if (!this.selectedBomItem) {
        this.$message.warning('请先选择物料')
        return
      }
      
      // 检查是否已存在
      const exists = this.form.bomLines.some(line => line.itemId === this.selectedBomItem.itemId)
      if (exists) {
        this.$message.warning('该物料已添加')
        return
      }
      
      // 添加到BOM列表
      this.form.bomLines.push({
        itemId: this.selectedBomItem.itemId,
        itemCode: this.selectedBomItem.itemCode,
        itemName: this.selectedBomItem.itemName,
        specification: this.selectedBomItem.specification,
        unitName: this.selectedBomItem.unitName,
        quantity: 1
      })
      
      this.bomItemDialogVisible = false
      this.$message.success('添加成功')
    },
    
    handleBomItemSelect(val) {
      this.selectedBomItem = val
    },
    
    filterBomItems() {
      // 搜索功能通过 computed 属性 filteredBomItems 自动实现
    },
    
    removeBomLine(index) {
      this.form.bomLines.splice(index, 1)
    },
    
    // 加载可用的BOM物料
    async loadAvailableBomItems() {
      try {
        // 查询所有启用的物料作为BOM物料选项
        const res = await listItem({ pageNum: 1, pageSize: 1000 })
        this.availableBomItems = (res.rows || []).filter(item => 
          item.status === '0'
        )
      } catch (error) {
        console.error('加载BOM物料失败:', error)
        this.availableBomItems = []
      }
    },
    
    // 类型操作
    handleAddType(parentId) {
      this.isEditType = false
      const parent = parentId === 0 ? null : this.findNode(this.typeTreeData, parentId)
      this.typeForm = {
        typeId: null,
        parentId: parentId,
        parentName: parent ? parent.typeName : '',
        typeCode: '',
        typeName: '',
        itemAttr: 'RAW'
      }
      this.typeDialogVisible = true
    },
    
    handleEditType(data) {
      this.isEditType = true
      this.typeForm = {
        typeId: data.typeId,
        parentId: data.parentId || 0,
        parentName: '',
        typeCode: data.typeCode,
        typeName: data.typeName,
        itemAttr: data.itemAttr
      }
      this.typeDialogVisible = true
    },
    
    handleDeleteType(data) {
      if (data.children && data.children.length > 0) {
        this.$message.warning('该类型下有子类型，请先删除子类型')
        return
      }
      this.$confirm(`删除类型 "${data.typeName}"？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await delItemType(data.typeId)
          this.$message.success('删除成功')
          this.fetchTypeTree()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    
    submitTypeForm() {
      this.$refs.typeForm.validate(async valid => {
        if (valid) {
          try {
            if (this.isEditType) {
              await updateItemType(this.typeForm)
              this.$message.success('更新成功')
            } else {
              await addItemType(this.typeForm)
              this.$message.success('新增成功')
            }
            this.typeDialogVisible = false
            this.fetchTypeTree()
          } catch (error) {
            this.$message.error(this.isEditType ? '更新失败' : '新增失败')
          }
        }
      })
    },
    
    getTypeIcon(attr) {
      const map = { 'RAW': 'el-icon-box', 'SEMI': 'el-icon-s-operation', 'PRODUCT': 'el-icon-shopping-bag-1' }
      return map[attr] || 'el-icon-s-grid'
    },
    
    getTypeColor(attr) {
      const map = { 'RAW': '#409EFF', 'SEMI': '#E6A23C', 'PRODUCT': '#67C23A' }
      return map[attr] || '#909399'
    },
    
    getTypeTagType(attr) {
      const map = { 'RAW': 'primary', 'SEMI': 'warning', 'PRODUCT': 'success' }
      return map[attr] || 'info'
    },
    
    getStockClass(row) {
      if (row.quantity <= row.safetyStock) return 'stock-low'
      return 'stock-normal'
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 15px;
  height: calc(100vh - 100px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 12px;
  border-bottom: 1px solid #EBEEF5;

  .title-section {
    display: flex;
    align-items: center;
    
    i {
      font-size: 22px;
      color: #409EFF;
      margin-right: 8px;
    }
    
    .title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
  }
}

.main-content {
  height: calc(100% - 50px);
  
  .tree-col {
    height: 100%;
    
    .tree-card {
      height: 100%;
      
      .tree-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 15px;
        
        span {
          font-weight: 600;
          font-size: 14px;
        }
      }
      
      .tree-search {
        padding: 10px 15px;
      }
      
      .filter-tree {
        padding: 0 10px 15px;
        height: calc(100% - 90px);
        overflow-y: auto;
      }
      
      .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 13px;
        padding-right: 8px;
        
        .node-content {
          display: flex;
          align-items: center;
          flex: 1;
          padding: 4px 6px;
          border-radius: 3px;
          
          i {
            margin-right: 6px;
            font-size: 14px;
          }
          
          .node-label {
            flex: 1;
          }
          
          .node-count {
            color: #909399;
            font-size: 11px;
            margin-left: 4px;
          }
          
          &.active {
            background: #E3F2FD;
          }
        }
        
        .node-actions {
          display: none;
          
          i {
            margin-left: 8px;
            cursor: pointer;
            color: #909399;
            font-size: 13px;
            
            &:hover {
              color: #409EFF;
            }
            
            &.el-icon-delete:hover {
              color: #F56C6C;
            }
          }
        }
        
        &:hover .node-actions {
          display: inline-flex;
        }
      }
    }
  }
  
  .table-col {
    height: 100%;
    display: flex;
    flex-direction: column;
    
    .filter-card {
      margin-bottom: 10px;
      flex-shrink: 0;
      
      .filter-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .breadcrumb-wrap {
          display: flex;
          align-items: center;
          
          .breadcrumb-link {
            cursor: pointer;
            color: #409EFF;
            
            &:hover {
              text-decoration: underline;
            }
          }
        }
        
        .search-wrap {
          display: flex;
          gap: 8px;
        }
      }
    }
    
    .stat-bar {
      display: flex;
      gap: 15px;
      margin-bottom: 10px;
      padding: 10px 15px;
      background: #fff;
      border-radius: 4px;
      border: 1px solid #EBEEF5;
      flex-shrink: 0;
      
      .stat-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 13px;
        
        .stat-label {
          color: #606266;
        }
        
        .stat-value {
          font-weight: 600;
          font-size: 15px;
          
          &.blue { color: #409EFF; }
          &.green { color: #67C23A; }
          &.orange { color: #E6A23C; }
          &.gray { color: #909399; }
        }
      }
    }
    
    .table-card {
      flex: 1;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      
      .table-scroll-wrapper {
        flex: 1;
        overflow: auto;
        position: relative;
        
        /* 自定义滚动条样式 - 优美设计 */
        &::-webkit-scrollbar {
          width: 8px;
          height: 8px;
        }
        
        &::-webkit-scrollbar-track {
          background: #f1f1f1;
          border-radius: 4px;
        }
        
        &::-webkit-scrollbar-thumb {
          background: linear-gradient(135deg, #7C3AED 0%, #A78BFA 100%);
          border-radius: 4px;
          border: 2px solid #f1f1f1;
          
          &:hover {
            background: linear-gradient(135deg, #6D28D9 0%, #8B5CF6 100%);
          }
        }
        
        &::-webkit-scrollbar-corner {
          background: #f1f1f1;
        }
        
        /* Firefox 滚动条 */
        scrollbar-width: thin;
        scrollbar-color: #A78BFA #f1f1f1;
        
        /* 暗色主题滚动条 */
        .theme-dark & {
          &::-webkit-scrollbar-track {
            background: #2d2d3a;
          }
          
          &::-webkit-scrollbar-thumb {
            background: linear-gradient(135deg, #A78BFA 0%, #C4B5FD 100%);
            border: 2px solid #2d2d3a;
            
            &:hover {
              background: linear-gradient(135deg, #8B5CF6 0%, #A78BFA 100%);
            }
          }
          
          &::-webkit-scrollbar-corner {
            background: #2d2d3a;
          }
          
          scrollbar-color: #A78BFA #2d2d3a;
        }
      }
      
      .el-table {
        border-top: none;
      }
      
      .stock-low {
        color: #F56C6C;
        font-weight: 600;
      }
      
      .stock-normal {
        color: #606266;
      }
      
      .pagination-wrap {
        padding: 12px 15px;
        border-top: 1px solid #EBEEF5;
        display: flex;
        justify-content: flex-end;
        flex-shrink: 0;
        background: #fff;
      }
    }
  }
}

// 详情弹框样式
.detail-content {
  padding: 10px 0;
  
  .detail-section {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .detail-item {
    display: flex;
    align-items: baseline;
    padding: 8px 0;
    
    &.block {
      flex-direction: column;
      
      .detail-label {
        margin-bottom: 8px;
      }
      
      .detail-value {
        padding: 10px 12px;
        background: #f5f7fa;
        border-radius: 4px;
        width: 100%;
        min-height: 40px;
      }
    }
    
    .detail-label {
      color: #606266;
      font-size: 14px;
      min-width: 80px;
      font-weight: 500;
    }
    
    .detail-value {
      color: #303133;
      font-size: 14px;
      flex: 1;
      
      &.cost {
        color: #7C3AED;
        font-weight: 600;
      }
    }
  }
}

::v-deep {
  .el-tree-node__content {
    height: 32px;
  }
  
  .el-card__header {
    padding: 12px 15px;
    border-bottom: 1px solid #EBEEF5;
  }
  
  // 表格表头样式 - 文字不换行，宽度自适应
  .md-item-table {
    // 统一表头和内容行高
    .el-table__cell {
      padding: 8px 0 !important;
      height: 40px !important;
      
      .cell {
        line-height: 24px;
        padding: 0 8px;
      }
    }
    
    // 表头样式 - 文字完全显示
    .el-table__header-wrapper {
      .el-table__header {
        th.el-table__cell {
          .cell {
            white-space: nowrap !important;
            overflow: visible !important;
            text-overflow: clip !important;
            font-weight: 600;
            font-size: 14px;
          }
        }
      }
    }
    
    // 内容样式 - 超出显示省略号
    .el-table__body-wrapper {
      .el-table__body {
        td.el-table__cell {
          .cell {
            white-space: nowrap !important;
            overflow: hidden !important;
            text-overflow: ellipsis !important;
            font-size: 13px;
          }
        }
      }
    }
    
    // 固定列样式统一
    .el-table__fixed,
    .el-table__fixed-right {
      .el-table__cell {
        padding: 8px 0 !important;
        height: 40px !important;
      }
    }
  }
  
  // 物料弹框样式
  ::v-deep .item-dialog {
    .el-dialog__body {
      padding: 20px;
      max-height: 70vh;
      overflow-y: auto;
    }
    
    // 基本信息区域
    .basic-info-section {
      margin-bottom: 20px;
      
      .section-title {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 2px solid #7C3AED;
        
        i {
          font-size: 18px;
          color: #7C3AED;
          margin-right: 8px;
        }
        
        span {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }
    }
    
    // 子标签页区域
    .sub-tabs-section {
      margin-top: 20px;
      
      .el-tabs__header {
        margin-bottom: 15px;
      }
      
      .el-tabs__item {
        font-size: 14px;
        
        &.is-active {
          color: #7C3AED;
          font-weight: 600;
        }
      }
      
      .el-tabs__active-bar {
        background-color: #7C3AED;
      }
    }
  }
  
  // BOM区域样式
  .bom-section {
    .bom-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
      padding: 0 5px;
      
      .bom-title {
        font-size: 15px;
        font-weight: 600;
        color: #303133;
      }
    }
    
    .bom-table-wrapper {
      width: 100%;
      overflow-x: auto;
      overflow-y: hidden;
      
      &::-webkit-scrollbar {
        height: 8px;
      }
      
      &::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 4px;
      }
      
      &::-webkit-scrollbar-thumb {
        background: linear-gradient(135deg, #7C3AED 0%, #A78BFA 100%);
        border-radius: 4px;
        
        &:hover {
          background: linear-gradient(135deg, #6D28D9 0%, #8B5CF6 100%);
        }
      }
    }
    
    .bom-table {
      width: 100% !important;
      
      .el-table__header-wrapper th {
        background-color: #f5f7fa;
        font-weight: 600;
        color: #303133;
        text-align: center;
        white-space: nowrap;
        
        .cell {
          white-space: nowrap;
        }
      }
      
      .el-table__body-wrapper td {
        vertical-align: middle;
      }
    }
    
    .bom-empty {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 40px 20px;
      color: #909399;
      
      i {
        font-size: 48px;
        margin-bottom: 10px;
        color: #dcdfe6;
      }
      
      span {
        font-size: 14px;
      }
    }
    
    // BOM表格中物料编码样式
    .bom-item-code {
      font-weight: 600;
      color: #409EFF;
    }
  }
  
  // BOM物料选择弹窗样式
  .bom-item-search {
    margin-bottom: 15px;
  }
  
  .hidden-radio {
    .el-radio__input {
      display: block;
    }
    .el-radio__label {
      display: none;
    }
  }
  
  // 确保弹窗中表格表头不换行
  .no-mask-dialog {
    .el-table__header-wrapper th {
      white-space: nowrap;
      
      .cell {
        white-space: nowrap;
      }
    }
  }
  
  // 空状态占位
  .empty-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 20px;
    color: #909399;
    
    i {
      font-size: 56px;
      margin-bottom: 15px;
      color: #dcdfe6;
    }
    
    span {
      font-size: 14px;
    }
  }
  
  // 无遮罩弹框样式
  .no-mask-dialog {
    .el-dialog {
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3) !important;
      border: 1px solid #dcdfe6;
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      margin: 0 !important;
    }
    
    &.el-dialog__wrapper {
      display: flex;
      align-items: center;
      justify-content: center;
      background: transparent !important; /* 去掉灰色遮罩 */
    }
  }
  
  // 全局覆盖 - 去掉所有弹框遮罩背景
  ::v-deep .el-dialog__wrapper {
    background: rgba(0, 0, 0, 0) !important;
  }
}
</style>
