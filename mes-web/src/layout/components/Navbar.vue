<template>
  <div class="navbar">
    <div class="left-menu">
      <span class="logo">MES系统 - carels</span>
    </div>
    <div class="right-menu">
      <el-dropdown trigger="click">
        <span class="el-dropdown-link">
          管理员<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>个人中心</el-dropdown-item>
          <el-dropdown-item divided>退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Navbar'
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
}

.right-menu {
  .el-dropdown-link {
    cursor: pointer;
    color: #606266;
  }
}
</style>
