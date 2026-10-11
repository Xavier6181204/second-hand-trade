<template>
  <div class="register-container">
    <el-card class="register-card">
      <el-button type="text" @click="$router.push('/')" style="margin-bottom:10px;font-size:14px;">
        ← 返回首页
      </el-button>
      <h2 style="text-align:center;margin-bottom:20px;">注册</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <div style="text-align:center;">
        <el-button type="primary" @click="handleRegister" :loading="loading">
          注册
        </el-button>
      </div>
      <div style="text-align:center;margin-top:12px;">
        <router-link to="/login">已有账号？去登录</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      form: { username: '', password: '', phone: '' },
      loading: false
    }
  },
  methods: {
    async handleRegister() {
      if (!this.form.username || !this.form.password) {
        this.$message.warning('请填写完整信息')
        return
      }
      this.loading = true
      const res = await api.register(this.form)
      this.loading = false
      if (res.code === 200) {
        this.$message.success('注册成功，请登录')
        this.$router.push('/login')
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}
.register-card {
  width: 400px;
}
</style>