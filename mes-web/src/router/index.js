/**
 * MES系统路由配置 - carels (完整版)
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */

import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/login/index'

Vue.use(Router)

// 404页面
const Error404 = {
  template: '<div style="text-align:center;padding-top:100px;"><h1>404</h1><p>页面不存在 - carels</p></div>'
}

// 占位页面组件工厂
const createPlaceholder = (title) => ({
  template: `<div class="app-container"><h1>${title}</h1><p>功能开发中 - carels</p></div>`
})

// 首页
const Dashboard = () => import('@/views/dashboard/index')

// 基础资料页面
const MdItem = () => import('@/views/md/item/index')
const MdItemType = () => import('@/views/md/itemType/index')
const MdWorkshop = () => import('@/views/md/workshop/index')
const MdProductionLine = () => import('@/views/md/productionLine/index')
const MdWorkstation = () => import('@/views/md/workstation/index')
const MdClient = () => import('@/views/md/client/index')
const MdVendor = () => import('@/views/md/vendor/index')

// 生产管理页面
const ProWorkorder = () => import('@/views/pro/workorder/index')
const ProTask = () => import('@/views/pro/task/index')
const ProFeedback = () => import('@/views/pro/feedback/index')
const ProRoute = () => import('@/views/pro/route/index')
const ProAndon = () => import('@/views/pro/andon/index')
const ProPlan = () => import('@/views/pro/plan/index')
const ProMaterialReq = () => import('@/views/pro/materialReq/index')

// 仓储管理页面
const WmWarehouse = () => import('@/views/wm/warehouse/index')
const WmStock = () => import('@/views/wm/stock/index')
const WmRecpt = () => import('@/views/wm/recpt/index')
const WmIssue = () => import('@/views/wm/issue/index')
const WmBatch = () => import('@/views/wm/batch/index')
const WmStocktaking = () => import('@/views/wm/stocktaking/index')
const WmTransfer = () => import('@/views/wm/transfer/index')
const WmWarning = () => import('@/views/wm/warning/index')

// 质量管理页面
const QcIqc = () => import('@/views/qc/iqc/index')
const QcIpqc = () => import('@/views/qc/ipqc/index')
const QcOqc = () => import('@/views/qc/oqc/index')
const QcTemplate = () => import('@/views/qc/template/index')
const QcDefect = () => import('@/views/qc/defect/index')

// 设备管理页面
const DvMachinery = () => import('@/views/dv/machinery/index')
const DvCheck = () => import('@/views/dv/check/index')
const DvRepair = () => import('@/views/dv/repair/index')

// 采购管理页面
const PurRequest = () => import('@/views/pur/request/index')
const PurOrder = () => import('@/views/pur/order/index')
const PurReceipt = () => import('@/views/pur/receipt/index')
const PurReturn = () => import('@/views/pur/return/index')

// 报表页面
const ReportChart = () => import('@/views/report/chart/index')
const ReportWorkorderBoard = () => import('@/views/report/workorder-board/index')
const ReportQualityBoard = () => import('@/views/report/quality-board/index')

// 系统管理页面
const SysUser = () => import('@/views/system/user/index')
const SysRole = () => import('@/views/system/role/index')
const SysMenu = () => import('@/views/system/menu/index')
const SysDept = () => import('@/views/system/dept/index')
const SysDict = () => import('@/views/system/dict/index')
const SysConfig = () => import('@/views/system/config/index')
const SysLog = () => import('@/views/system/log/index')

// 布局组件
const Layout = () => import('@/layout')

export const routes = [
  {
    path: '/login',
    component: Login,
    hidden: true
  },
  {
    path: '/404',
    component: Error404,
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: Dashboard,
      meta: { title: '首页', icon: 'el-icon-s-home' }
    }]
  },
  
  // 主数据
  {
    path: '/md',
    component: Layout,
    redirect: '/md/item',
    name: 'Md',
    meta: { title: '主数据', icon: 'el-icon-s-data' },
    children: [
      { path: 'itemType', name: 'MdItemType', component: MdItemType, meta: { title: '物料类型', icon: 'el-icon-s-grid' } },
      { path: 'item', name: 'MdItem', component: MdItem, meta: { title: '物料信息', icon: 'el-icon-goods' } },
      { path: 'workshop', name: 'MdWorkshop', component: MdWorkshop, meta: { title: '车间管理', icon: 'el-icon-office-building' } },
      { path: 'productionLine', name: 'MdProductionLine', component: MdProductionLine, meta: { title: '生产线管理', icon: 'el-icon-s-operation' } },
      { path: 'workstation', name: 'MdWorkstation', component: MdWorkstation, meta: { title: '工作站  ', icon: 'el-icon-s-custom' } },
      { path: 'client', name: 'MdClient', component: MdClient, meta: { title: '客户管理', icon: 'el-icon-user' } },
      { path: 'vendor', name: 'MdVendor', component: MdVendor, meta: { title: '供应商  ', icon: 'el-icon-truck' } }
    ]
  },
  
  // 生产执行
  {
    path: '/pro',
    component: Layout,
    redirect: '/pro/workorder',
    name: 'Pro',
    meta: { title: '生产执行', icon: 'el-icon-s-order' },
    children: [
      { path: 'plan', name: 'ProPlan', component: ProPlan, meta: { title: '生产计划', icon: 'el-icon-s-order' } },
      { path: 'materialReq', name: 'ProMaterialReq', component: ProMaterialReq, meta: { title: '物料需求', icon: 'el-icon-s-goods' } },
      { path: 'workorder', name: 'ProWorkorder', component: ProWorkorder, meta: { title: '生产工单', icon: 'el-icon-document' } },
      { path: 'task', name: 'ProTask', component: ProTask, meta: { title: '生产任务', icon: 'el-icon-s-claim' } },
      { path: 'feedback', name: 'ProFeedback', component: ProFeedback, meta: { title: '生产报工', icon: 'el-icon-check' } },
      { path: 'route', name: 'ProRoute', component: ProRoute, meta: { title: '工艺路线', icon: 'el-icon-s-management' } },
      { path: 'andon', name: 'ProAndon', component: ProAndon, meta: { title: '异常管理', icon: 'el-icon-warning' } }
    ]
  },
  
  // 仓储物流
  {
    path: '/wm',
    component: Layout,
    redirect: '/wm/warehouse',
    name: 'Wm',
    meta: { title: '仓储物流', icon: 'el-icon-house' },
    children: [
      { path: 'warehouse', name: 'WmWarehouse', component: WmWarehouse, meta: { title: '仓库管理', icon: 'el-icon-office-building' } },
      { path: 'stock', name: 'WmStock', component: WmStock, meta: { title: '库存查询', icon: 'el-icon-s-data' } },
      { path: 'recpt', name: 'WmRecpt', component: WmRecpt, meta: { title: '入库管理', icon: 'el-icon-download' } },
      { path: 'issue', name: 'WmIssue', component: WmIssue, meta: { title: '出库管理', icon: 'el-icon-upload2' } },
      { path: 'batch', name: 'WmBatch', component: WmBatch, meta: { title: '批次管理', icon: 'el-icon-collection-tag' } },
      { path: 'stocktaking', name: 'WmStocktaking', component: WmStocktaking, meta: { title: '库存盘点', icon: 'el-icon-s-check' } },
      { path: 'transfer', name: 'WmTransfer', component: WmTransfer, meta: { title: '库存调拨', icon: 'el-icon-s-promotion' } },
      { path: 'warning', name: 'WmWarning', component: WmWarning, meta: { title: '库存预警', icon: 'el-icon-warning-outline' } }
    ]
  },
  
  // 质量管理
  {
    path: '/qc',
    component: Layout,
    redirect: '/qc/iqc',
    name: 'Qc',
    meta: { title: '质量管理', icon: 'el-icon-s-check' },
    children: [
      { path: 'iqc', name: 'QcIqc', component: QcIqc, meta: { title: '来料检验', icon: 'el-icon-document-checked' } },
      { path: 'ipqc', name: 'QcIpqc', component: QcIpqc, meta: { title: '过程检验', icon: 'el-icon-s-flag' } },
      { path: 'oqc', name: 'QcOqc', component: QcOqc, meta: { title: '出货检验', icon: 'el-icon-box' } },
      { path: 'template', name: 'QcTemplate', component: QcTemplate, meta: { title: '检验模板', icon: 'el-icon-tickets' } },
      { path: 'defect', name: 'QcDefect', component: QcDefect, meta: { title: '缺陷记录', icon: 'el-icon-warning-outline' } }
    ]
  },
  
  // 设备运维
  {
    path: '/dv',
    component: Layout,
    redirect: '/dv/machinery',
    name: 'Dv',
    meta: { title: '设备运维', icon: 'el-icon-s-tools' },
    children: [
      { path: 'machinery', name: 'DvMachinery', component: DvMachinery, meta: { title: '设备台账', icon: 'el-icon-cpu' } },
      { path: 'check', name: 'DvCheck', component: DvCheck, meta: { title: '点检管理', icon: 'el-icon-view' } },
      { path: 'repair', name: 'DvRepair', component: DvRepair, meta: { title: '维修管理', icon: 'el-icon-first-aid-kit' } }
    ]
  },
  
  // 采购管理
  {
    path: '/pur',
    component: Layout,
    redirect: '/pur/request',
    name: 'Pur',
    meta: { title: '采购管理', icon: 'el-icon-s-shop' },
    children: [
      { path: 'request', name: 'PurRequest', component: PurRequest, meta: { title: '采购申请', icon: 'el-icon-document' } },
      { path: 'order', name: 'PurOrder', component: PurOrder, meta: { title: '采购订单', icon: 'el-icon-s-order' } },
      { path: 'receipt', name: 'PurReceipt', component: PurReceipt, meta: { title: '采购入库', icon: 'el-icon-s-home' } },
      { path: 'return', name: 'PurReturn', component: PurReturn, meta: { title: '采购退货', icon: 'el-icon-refresh-left' } }
    ]
  },
  
  // 报表分析
  {
    path: '/report',
    component: Layout,
    redirect: '/report/chart',
    name: 'Report',
    meta: { title: '报表分析', icon: 'el-icon-s-marketing' },
    children: [
      { path: 'chart', name: 'ReportChart', component: ReportChart, meta: { title: '生产报表', icon: 'el-icon-pie-chart' } },
      { path: 'workorder-board', name: 'ReportWorkorderBoard', component: ReportWorkorderBoard, meta: { title: '工单看板', icon: 'el-icon-data-board' } },
      { path: 'quality-board', name: 'ReportQualityBoard', component: ReportQualityBoard, meta: { title: '质量看板', icon: 'el-icon-data-analysis' } }
    ]
  },
  
  // 系统管理
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    name: 'System',
    meta: { title: '系统管理', icon: 'el-icon-s-tools' },
    children: [
      { path: 'user', name: 'SysUser', component: SysUser, meta: { title: '用户管理', icon: 'el-icon-user' } },
      { path: 'role', name: 'SysRole', component: SysRole, meta: { title: '角色管理', icon: 'el-icon-s-custom' } },
      { path: 'menu', name: 'SysMenu', component: SysMenu, meta: { title: '菜单管理', icon: 'el-icon-menu' } },
      { path: 'dept', name: 'SysDept', component: SysDept, meta: { title: '部门管理', icon: 'el-icon-s-home' } },
      { path: 'dict', name: 'SysDict', component: SysDict, meta: { title: '字典管理', icon: 'el-icon-collection' } },
      { path: 'config', name: 'SysConfig', component: SysConfig, meta: { title: '参数设置', icon: 'el-icon-s-operation' } },
      { path: 'log', name: 'SysLog', component: SysLog, meta: { title: '操作日志', icon: 'el-icon-document' } }
    ]
  },
  
  // 404页面
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: routes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
