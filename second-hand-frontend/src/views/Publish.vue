<template>
  <div class="publish-container">
    <el-button type="text" @click="$router.push('/')" style="margin-bottom:10px;font-size:16px;">
      ← 返回首页
    </el-button>
    <h2>发布商品</h2>
    <el-form :model="form" label-width="100px" style="max-width:600px;">
      <el-form-item label="商品标题">
        <el-input v-model="form.title" placeholder="请输入商品标题" />
      </el-form-item>
      <el-form-item label="商品分类">
        <el-select v-model="form.categoryId" placeholder="请选择分类">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="价格">
        <el-input-number v-model="form.price" :precision="2" :min="0" />
      </el-form-item>
      <el-form-item label="原价">
        <el-input-number v-model="form.originalPrice" :precision="2" :min="0" />
      </el-form-item>
      <el-form-item label="商品描述">
        <div style="margin-bottom:8px;">
          <el-button
            size="small"
            type="primary"
            plain
            :loading="aiLoading"
            @click="generateDesc"
          >
            🤖 AI 帮我写描述
          </el-button>
          <span style="color:#999;font-size:12px;margin-left:8px;">根据标题和分类自动生成</span>
        </div>
        <el-input v-model="form.description" type="textarea" rows="4" placeholder="请描述商品详情" />
      </el-form-item>
      <el-form-item label="商品图片">
        <el-upload
          action="http://localhost:8080/api/product/upload"
          :with-credentials="true"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :on-remove="handleRemove"
          :file-list="fileList"
          :before-upload="beforeUpload"
          list-type="picture-card"
        >
          <i class="el-icon-plus"></i>
          <div slot="tip" class="el-upload__tip">只能上传一张图片（jpg/png），不超过5MB</div>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handlePublish" :loading="loading">发布</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      form: {
        title: '',
        categoryId: null,
        price: 0,
        originalPrice: null,
        description: '',
        images: ''
      },
      categories: [],
      fileList: [],
      loading: false,
      aiLoading: false
    }
  },
  mounted() {
    this.loadCategories()
  },
  methods: {
    async loadCategories() {
      const res = await api.getCategoryList()
      if (res.code === 200) {
        this.categories = res.data
      }
    },
    async generateDesc() {
      if (!this.form.title) {
        this.$message.warning('请先填写商品标题')
        return
      }
      let categoryName = ''
      const cat = this.categories.find(c => c.id === this.form.categoryId)
      if (cat) {
        categoryName = cat.name
      }

      this.aiLoading = true
      try {
        const res = await api.generateDescription(this.form.title, categoryName)
        if (res.code === 200) {
          this.form.description = res.data.description
          this.$message.success('AI 已生成描述，可手动修改')
        } else {
          this.$message.error(res.msg)
        }
      } catch (e) {
        this.$message.error('生成失败，请检查网络')
      }
      this.aiLoading = false
    },
    beforeUpload() {
      if (this.fileList.length >= 1) {
        this.$message.warning('只能上传一张图片，请先移除当前图片再上传新的')
        return false
      }
      return true
    },
    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        const url = response.data.url
        // 拼成完整地址，供 el-upload 预览使用
        const fullUrl = 'http://localhost:8080' + url
        this.fileList.push({ name: file.name, url: fullUrl })
        // 存到数据库的仍然是相对路径
        this.form.images = this.fileList.map(f => f.url.replace('http://localhost:8080', '')).join(',')
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.msg)
      }
    },
    handleUploadError(err, file, fileList) {
      console.log('上传失败', err)
      this.$message.error('图片上传失败，请检查网络或稍后重试')
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
      this.form.images = this.fileList.map(f => f.url.replace('http://localhost:8080', '')).join(',')
    },
    async handlePublish() {
      if (!this.form.title || !this.form.categoryId || this.form.price <= 0) {
        this.$message.warning('请填写完整信息')
        return
      }
      this.loading = true
      const res = await api.addProduct(this.form)
      this.loading = false
      if (res.code === 200) {
        this.$message.success('发布成功，等待审核')
        this.$router.push('/my-products')
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.publish-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 30px;
  background: #fff;
  border-radius: 8px;
}
h2 {
  margin-bottom: 30px;
}
</style>