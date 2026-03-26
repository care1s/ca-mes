<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pro">
      <div class="title-section">
        <i class="el-icon-s-management"></i>
        <span class="title">工艺路线</span>
        <span class="subtitle">Process Route</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增工艺路线</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="路线编码">
          <el-input v-model="queryParams.routeCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="路线名称">
          <el-input v-model="queryParams.routeName" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="适用产品">
          <el-input v-model="queryParams.itemName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table :data="tableData" stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="routeCode" label="路线编码" width="120" />
        <el-table-column prop="routeName" label="路线名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="itemName" label="适用产品" min-width="150" show-overflow-tooltip />
        <el-table-column prop="version" label="版本" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" size="small">
              {{ scope.row.status === '0' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isDefault" label="默认" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isDefault === 'Y'" type="success" size="small">是</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleConfigProcess(scope.row)">配置工序</el-button>
            <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          :current-page="queryParams.pageNum"
          :page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false" :modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="路线编码" prop="routeCode">
              <el-input v-model="form.routeCode" placeholder="请输入编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路线名称" prop="routeName">
              <el-input v-model="form.routeName" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="适用产品" prop="itemId">
          <el-select v-model="form.itemId" placeholder="请选择产品" style="width: 100%" filterable @change="handleItemChange">
            <el-option v-for="item in itemList" :key="item.itemId" :label="item.itemName" :value="item.itemId" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="版本号">
              <el-input v-model="form.version" placeholder="V1.0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="默认路线">
              <el-switch v-model="form.isDefault" active-value="Y" inactive-value="N" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 配置工序对话框 -->
    <el-dialog :title="'配置工序 - ' + currentRoute.routeName" :visible.sync="processDialogVisible" width="800px" :close-on-click-modal="false" :modal="false">
      <div class="process-config">
        <div class="process-toolbar">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAddProcess">添加工序</el-button>
          <span class="tips">提示：拖拽可调整工序顺序</span>
        </div>
        <el-table :data="routeProcessList" border stripe class="process-table">
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="processCode" label="工序编码" width="100" />
          <el-table-column prop="processName" label="工序名称" min-width="120" />
          <el-table-column prop="workstationName" label="默认工作站" width="120" />
          <el-table-column prop="standardHours" label="标准工时" width="90" align="right">
            <template slot-scope="scope">{{ scope.row.standardHours }}h</template>
          </el-table-column>
          <el-table-column prop="inspectFlag" label="检验" width="70" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.inspectFlag === 'Y'" type="warning" size="mini">是</el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleEditRouteProcess(scope.row)">编辑</el-button>
              <el-button type="text" size="small" style="color: #f56c6c" @click="handleDeleteRouteProcess(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRouteProcess">保存</el-button>
      </div>
    </el-dialog>

    <!-- 添加工序对话框 -->
    <el-dialog title="选择工序" :visible.sync="selectProcessDialogVisible" width="500px" :modal="false">
      <el-form :model="processForm" label-width="100px">
        <el-form-item label="选择工序" prop="processId">
          <el-select v-model="processForm.processId" placeholder="请选择工序" style="width: 100%" filterable @change="handleProcessChange">
            <el-option v-for="item in availableProcessList" :key="item.processId" :label="item.processName + ' (' + item.processCode + ')'" :value="item.processId" />
          </el-select>
        </el-form-item>
        <el-form-item label="默认工作站">
          <el-select v-model="processForm.workstationId" placeholder="请选择工作站" style="width: 100%">
            <el-option v-for="item in workstationList" :key="item.workstationId" :label="item.workstationName" :value="item.workstationId" />
          </el-select>
        </el-form-item>
        <el-form-item label="标准工时">
          <el-input-number v-model="processForm.standardHours" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否检验">
          <el-switch v-model="processForm.inspectFlag" active-value="Y" inactive-value="N" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="selectProcessDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAddProcess">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProRoute, getProRoute, addProRoute, updateProRoute, delProRoute, listProProcess, listRouteProcess, saveRouteProcess } from '@/api/pro'

export default {
  name: 'ProRoute',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        routeCode: '',
        routeName: '',
        itemName: ''
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        routeId: null,
        routeCode: '',
        routeName: '',
        itemId: null,
        itemCode: '',
        itemName: '',
        version: 'V1.0',
        isDefault: 'N',
        remark: ''
      },
      rules: {
        routeCode: [{ required: true, message: '请输入路线编码', trigger: 'blur' }],
        routeName: [{ required: true, message: '请输入路线名称', trigger: 'blur' }],
        itemId: [{ required: true, message: '请选择适用产品', trigger: 'change' }]
      },
      itemList: [
        { itemId: 1, itemCode: 'P001', itemName: '手机主板' },
        { itemId: 2, itemCode: 'P002', itemName: '电池组件' },
        { itemId: 3, itemCode: 'P003', itemName: '显示屏' }
      ],
      // 工序配置相关
      processDialogVisible: false,
      currentRoute: {},
      routeProcessList: [],
      selectProcessDialogVisible: false,
      processForm: {
        processId: null,
        processCode: '',
        processName: '',
        workstationId: null,
        workstationName: '',
        standardHours: 0,
        inspectFlag: 'N'
      },
      availableProcessList: [],
      workstationList: [
        { workstationId: 1, workstationName: '切割工作站1' },
        { workstationId: 2, workstationName: '焊接工作站1' },
        { workstationId: 3, workstationName: '组装线A' },
        { workstationId: 4, workstationName: '测试工位1' },
        { workstationId: 5, workstationName: '包装工位1' }
      ]
    }
  },
  created() {
    this.fetchData()
    this.loadProcessList()
  },
  methods: {
    fetchData() {
      this.loading = true
      listProRoute(this.queryParams).then(response => {
        this.tableData = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    loadProcessList() {
      listProProcess({ pageNum: 1, pageSize: 100 }).then(response => {
        this.availableProcessList = response.rows || []
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        routeCode: '',
        routeName: '',
        itemName: ''
      }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增工艺路线'
      this.form = {
        routeId: null,
        routeCode: '',
        routeName: '',
        itemId: null,
        itemCode: '',
        itemName: '',
        version: 'V1.0',
        isDefault: 'N',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑工艺路线'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$alert(`路线编码：${row.routeCode}<br>路线名称：${row.routeName}<br>适用产品：${row.itemName}<br>版本：${row.version}`, '工艺路线详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    handleDelete(row) {
      this.$confirm(`确认删除工艺路线 "${row.routeName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delProRoute(row.routeId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleItemChange(itemId) {
      const item = this.itemList.find(i => i.itemId === itemId)
      if (item) {
        this.form.itemCode = item.itemCode
        this.form.itemName = item.itemName
      }
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.routeId) {
            updateProRoute(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            addProRoute(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            })
          }
        }
      })
    },
    // 配置工序相关方法
    handleConfigProcess(row) {
      this.currentRoute = { ...row }
      // 从后端加载该工艺路线的工序列表
      listRouteProcess(row.routeId).then(response => {
        this.routeProcessList = response.data || []
        this.processDialogVisible = true
      }).catch(() => {
        this.$message.error('加载工序列表失败')
      })
    },
    handleAddProcess() {
      this.processForm = {
        processId: null,
        processCode: '',
        processName: '',
        workstationId: null,
        workstationName: '',
        standardHours: 0,
        inspectFlag: 'N'
      }
      this.selectProcessDialogVisible = true
    },
    handleProcessChange(processId) {
      const process = this.availableProcessList.find(p => p.processId === processId)
      if (process) {
        this.processForm.processCode = process.processCode
        this.processForm.processName = process.processName
        this.processForm.standardHours = process.standardHours
      }
    },
    confirmAddProcess() {
      if (!this.processForm.processId) {
        this.$message.error('请选择工序')
        return
      }
      const workstation = this.workstationList.find(w => w.workstationId === this.processForm.workstationId)
      this.routeProcessList.push({
        ...this.processForm,
        workstationName: workstation ? workstation.workstationName : ''
      })
      this.selectProcessDialogVisible = false
    },
    handleEditRouteProcess(row) {
      this.processForm = { ...row }
      this.selectProcessDialogVisible = true
    },
    handleDeleteRouteProcess(index) {
      this.routeProcessList.splice(index, 1)
    },
    saveRouteProcess() {
      // 保存到后端
      saveRouteProcess(this.currentRoute.routeId, this.routeProcessList).then(() => {
        this.$message.success('工序配置保存成功')
        this.processDialogVisible = false
      }).catch(() => {
        this.$message.error('保存失败')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 0 15px 0;
  border-bottom: 1px solid #ebeef5;

  .title-section {
    display: flex;
    align-items: center;
    gap: 10px;

    i {
      font-size: 24px;
      color: #409eff;
    }

    .title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    .subtitle {
      font-size: 14px;
      color: #909399;
      font-weight: normal;
    }
  }
}

.search-card {
  margin-bottom: 20px;

  .search-form {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
}

.table-card {
  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.process-config {
  .process-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;

    .tips {
      color: #909399;
      font-size: 13px;
    }
  }
}

::v-deep .el-loading-mask {
  display: none !important;
}
</style>
