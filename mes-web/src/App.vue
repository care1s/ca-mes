<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
/**
 * MES系统根组件 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'App'
}
</script>

<style lang="scss">
// 全局样式
#app {
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>
