<template>
  <div class="login-container">
    <el-card class="login-card">
      <el-button type="text" @click="$router.push('/')" style="margin-bottom:10px;font-size:14px;">
        ← 返回首页
      </el-button>
      <h2 style="text-align:center;margin-bottom:20px;">登录</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
      </el-form>
      <div style="text-align:center;">
        <el-button type="primary" @click="handleLogin" :loading="loading">
          登录
        </el-button>
      </div>
      <div style="text-align:center;margin-top:12px;">
        <router-link to="/register">还没有账号？立即注册</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: { username: '', password: '' },
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      if (!this.form.username || !this.form.password) {
        this.$message.warning('请填写完整信息')
        return
      }
      this.loading = true
      const res = await this.$store.dispatch('login', this.form)
      this.loading = false
      if (res.code === 200) {
        this.$message.success('登录成功')
        this.$router.push('/')
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}
.login-card {
  width: 400px;
}
</style>