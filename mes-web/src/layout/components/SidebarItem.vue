<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children,item)">
      <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
        <i :class="onlyOneChild.meta.icon || 'el-icon-menu'"></i>
        <span slot="title">{{ onlyOneChild.meta.title }}</span>
      </el-menu-item>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template slot="title">
        <i :class="item.meta && item.meta.icon ? item.meta.icon : 'el-icon-menu'"></i>
        <span v-if="item.meta" slot="title">{{ item.meta.title }}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'

export default {
  name: 'SidebarItem',
  props: {
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    this.onlyOneChild = null
    return {}
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          this.onlyOneChild = item
          return true
        }
      })
      if (showingChildren.length === 1) {
        return true
      }
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }
      return false
    },
    resolvePath(routePath) {
      // 处理空路径情况
      if (!routePath) {
        return this.basePath || '/'
      }
      if (routePath.charAt(0) === '/') {
        return routePath
      }
      // 使用简单字符串拼接替代 path.resolve，避免浏览器环境问题
      if (this.basePath === '/') {
        return '/' + routePath
      }
      if (this.basePath.endsWith('/')) {
        return this.basePath + routePath
      }
      return this.basePath + '/' + routePath
    }
  }
}
</script>
