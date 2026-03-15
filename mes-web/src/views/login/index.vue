<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <div class="title-container">
        <h3 class="title">MES制造执行系统</h3>
        <p class="subtitle">carels版权所有 V1.0</p>
      </div>

      <!-- 登录端选择 -->
      <el-form-item prop="terminalType">
        <span class="svg-container">
          <i class="el-icon-monitor"></i>
        </span>
        <el-select v-model="loginForm.terminalType" placeholder="选择登录终端" style="width: 100%">
          <el-option label="PC端" value="PC">
            <span style="display: inline-flex; align-items: center;">
              <i class="el-icon-monitor" style="margin-right: 8px;"></i>PC端
            </span>
          </el-option>
          <el-option label="Web端" value="WEB">
            <span style="display: inline-flex; align-items: center;">
              <i class="el-icon-mouse" style="margin-right: 8px;"></i>Web端
            </span>
          </el-option>
          <el-option label="PDA端" value="PDA">
            <span style="display: inline-flex; align-items: center;">
              <i class="el-icon-mobile-phone" style="margin-right: 8px;"></i>PDA端
            </span>
          </el-option>
          <el-option label="触摸屏" value="TERMINAL">
            <span style="display: inline-flex; align-items: center;">
              <i class="el-icon-tablet" style="margin-right: 8px;"></i>触摸屏
            </span>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item prop="username">
        <span class="svg-container">
          <i class="el-icon-user"></i>
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="请输入用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <i class="el-icon-lock"></i>
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="请输入密码"
          name="password"
          tabindex="2"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showPwd">
          <i :class="passwordType === 'password' ? 'el-icon-view' : 'el-icon-hide'"></i>
        </span>
      </el-form-item>

      <!-- 验证码 -->
      <el-form-item prop="captcha" class="captcha-item">
        <span class="svg-container">
          <i class="el-icon-key"></i>
        </span>
        <el-input
          v-model="loginForm.captcha"
          placeholder="请输入验证码"
          name="captcha"
          tabindex="3"
          @keyup.enter.native="handleLogin"
          style="width: calc(100% - 120px)"
        />
        <div class="captcha-img" @click="refreshCaptcha">
          <img v-if="captchaUrl" :src="captchaUrl" alt="验证码" />
          <span v-else class="captcha-placeholder">点击刷新</span>
        </div>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;border-radius: 20px;" @click.native.prevent="handleLogin">登 录</el-button>
      
      <div class="tips">
        <span>默认账号: admin</span>
        <span style="margin-left: 20px;">默认密码: 123456</span>
      </div>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios'

/**
 * 登录页面 - carels
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: 'admin',
        password: '123456',
        terminalType: 'PC',
        captcha: '',
        captchaKey: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
        terminalType: [{ required: true, trigger: 'change', message: '请选择登录终端' }],
        captcha: [{ required: true, trigger: 'blur', message: '请输入验证码' }]
      },
      loading: false,
      passwordType: 'password',
      captchaUrl: '',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  mounted() {
    this.refreshCaptcha()
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$refs.password.focus()
    },
    
    // 刷新验证码 - 从后端获取验证码值，前端生成图片
    refreshCaptcha() {
      // 调用后端获取验证码值
      axios({
        url: '/auth/captcha',
        method: 'get'
      }).then(response => {
        const res = response.data
        if (res.code === 200) {
          this.loginForm.captchaKey = res.data.captchaKey
          // 后端返回验证码值，前端生成图片
          this.generateCaptcha(res.data.captchaCode)
        }
      }).catch(error => {
        console.error('获取验证码错误:', error)
        let msg = '获取验证码失败'
        if (error.message) {
          msg += ': ' + error.message
        }
        this.$message.error(msg)
      })
    },
    
    // 根据验证码值生成图片
    generateCaptcha(captchaText) {
      const canvas = document.createElement('canvas')
      canvas.width = 100
      canvas.height = 40
      const ctx = canvas.getContext('2d')
      
      // 背景色
      ctx.fillStyle = '#f0f0f0'
      ctx.fillRect(0, 0, 100, 40)
      
      // 保存验证码值（用于登录时发送给后端验证）
      this.captchaText = captchaText
      
      // 绘制文字
      ctx.font = 'bold 24px Arial'
      ctx.fillStyle = '#7C3AED'
      for (let i = 0; i < 4; i++) {
        ctx.save()
        ctx.translate(15 + i * 20, 28)
        ctx.rotate((Math.random() - 0.5) * 0.4)
        ctx.fillText(captchaText[i], 0, 0)
        ctx.restore()
      }
      
      // 添加干扰线
      ctx.strokeStyle = '#A78BFA'
      for (let i = 0; i < 3; i++) {
        ctx.beginPath()
        ctx.moveTo(Math.random() * 100, Math.random() * 40)
        ctx.lineTo(Math.random() * 100, Math.random() * 40)
        ctx.stroke()
      }
      
      this.captchaUrl = canvas.toDataURL()
    },
    
    handleLogin() {
      console.log('点击登录按钮')
      this.$refs.loginForm.validate(valid => {
        console.log('表单验证通过:', valid)
        if (valid) {
          console.log('表单验证通过，开始登录')
          this.loading = true
          // 保存登录终端类型到 localStorage
          localStorage.setItem('mes-terminal-type', this.loginForm.terminalType)
          
          this.$store.dispatch('user/login', this.loginForm).then((data) => {
            console.log('登录成功:', data)
            this.$message.success('登录成功')
            this.loading = false
            // 跳转到首页或重定向页面
            const redirectPath = this.redirect || '/'
            console.log('准备跳转到:', redirectPath)
            this.$router.push(redirectPath).then(() => {
              console.log('跳转成功')
            }).catch(err => {
              console.error('跳转失败:', err)
              this.$message.error('页面跳转失败')
            })
          }).catch((error) => {
            console.log('登录失败:', error)
            this.loading = false
            const msg = error.message || '登录失败'
            this.$message.error(msg)
            // 如果是验证码相关错误，刷新验证码并清空输入
            if (msg.includes('验证码') || msg.includes('captcha') || msg.includes('过期')) {
              this.refreshCaptcha()
              this.loginForm.captcha = ''
            }
          })
        } else {
          console.log('表单验证失败')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
$bg: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
$dark_gray: #889aa4;
$light_gray: #eee;
$primary: #7C3AED;

.login-container {
  min-height: 100%;
  width: 100%;
  background: $bg;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;

  .login-form {
    position: relative;
    width: 480px;
    max-width: 100%;
    padding: 40px 50px 30px;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 20px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(10px);
  }

  .title-container {
    position: relative;
    text-align: center;
    margin-bottom: 40px;

    .title {
      font-size: 28px;
      color: $primary;
      margin: 0 auto 10px auto;
      font-weight: bold;
    }

    .subtitle {
      font-size: 13px;
      color: #666;
      margin: 0;
    }
  }

  ::v-deep {
    .el-form-item {
      border: 1px solid rgba(0, 0, 0, 0.1);
      background: #fff;
      border-radius: 25px;
      margin-bottom: 20px;
      transition: all 0.3s;
      
      &:hover {
        border-color: $primary;
        box-shadow: 0 2px 8px rgba(124, 58, 237, 0.15);
      }
      
      &.is-focus {
        border-color: $primary;
      }
    }
    
    .el-input {
      display: inline-block;
      height: 50px;
      width: 85%;
      
      input {
        background: transparent;
        border: 0px;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: #333;
        height: 50px;
        caret-color: $primary;
        
        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px #fff inset !important;
          -webkit-text-fill-color: #333 !important;
        }
      }
    }
    
    .el-select {
      width: 85% !important;
      
      .el-input {
        width: 100% !important;
      }
      
      input {
        padding-left: 0;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $primary;
    vertical-align: middle;
    width: 40px;
    display: inline-block;
    font-size: 18px;
  }

  .show-pwd {
    position: absolute;
    right: 15px;
    top: 15px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
    transition: all 0.3s;
    
    &:hover {
      color: $primary;
    }
  }
  
  // 验证码样式
  .captcha-item {
    ::v-deep .el-form-item__content {
      display: flex;
      align-items: center;
    }
    
    .captcha-img {
      width: 100px;
      height: 48px;
      margin-left: 10px;
      border-radius: 8px;
      overflow: hidden;
      cursor: pointer;
      border: 1px solid #ddd;
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
      
      .captcha-placeholder {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
        font-size: 12px;
        color: #999;
        background: #f5f5f5;
      }
      
      &:hover {
        border-color: $primary;
      }
    }
  }
  
  .tips {
    font-size: 12px;
    color: #999;
    text-align: center;
    margin-top: 10px;
  }
}
</style>
