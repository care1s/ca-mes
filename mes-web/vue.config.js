/**
 * Vue CLI 配置文件 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */

const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  // 基础配置
  publicPath: '/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: false, // 禁用ESLint
  
  // 开发服务器配置
  devServer: {
    port: 8081,
    open: true,
    overlay: {
      warnings: false,
      errors: true
    },
    proxy: {
      '/mes': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/mes': '/mes'
        }
      },
      '/mobile': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/mobile': '/mobile'
        }
      },
      '/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/auth': '/auth'
        }
      }
    }
  },
  
  // Webpack配置
  configureWebpack: {
    // 禁用性能提示
    performance: {
      hints: false
    },
    // 配置别名
    resolve: {
      alias: {
        '@': resolve('src')
      }
    }
  },
  
  // 链式Webpack配置
  chainWebpack(config) {
    // 预加载
    config.plugin('preload').tap(() => [
      {
        rel: 'preload',
        fileBlacklist: [/\.map$/, /hot-update\.js$/, /runtime\..*\.js$/],
        include: 'initial'
      }
    ])
    
    // 当文件太多时，防止页面请求太多
    config.when(process.env.NODE_ENV !== 'development', config => {
      config
        .plugin('ScriptExtHtmlWebpackPlugin')
        .after('html')
        .use('script-ext-html-webpack-plugin', [
          {
            inline: /runtime\..*\.js$/
          }
        ])
        .end()
      
      config.optimization.splitChunks({
        chunks: 'all',
        cacheGroups: {
          libs: {
            name: 'chunk-libs',
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: 'initial'
          },
          elementUI: {
            name: 'chunk-elementUI',
            priority: 20,
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/
          },
          commons: {
            name: 'chunk-commons',
            test: resolve('src/components'),
            minChunks: 3,
            priority: 5,
            reuseExistingChunk: true
          }
        }
      })
      
      config.optimization.runtimeChunk('single')
    })
  }
}
