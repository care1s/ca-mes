<template>
  <div class="sidebar-wrapper">
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item 
          v-for="route in routes" 
          :key="route.path" 
          :item="route" 
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import SidebarItem from './SidebarItem'

export default {
  name: 'Sidebar',
  components: { SidebarItem },
  computed: {
    routes() {
      return this.$router.options.routes.filter(r => !r.hidden)
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    isCollapse() {
      return !this.$store.state.app.sidebar.opened
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-wrapper {
  height: 100%;
}

.scrollbar-wrapper {
  height: 100%;
}
</style>
