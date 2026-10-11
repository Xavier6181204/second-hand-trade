<template>
  <div class="my-products">
    <el-button type="text" @click="$router.push('/')" style="margin-bottom:10px;font-size:16px;">
      ← 返回首页
    </el-button>
    <h2>我的商品</h2>
    <el-table :data="products" style="width:100%;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="price" label="价格" width="120" />
      <el-table-column label="状态" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
          <el-tag v-if="scope.row.status === 1" type="success">出售中</el-tag>
          <el-tag v-if="scope.row.status === 2" type="info">已下架</el-tag>
          <el-tag v-if="scope.row.status === 3" type="danger">已售出</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="$router.push('/detail/'+scope.row.id)">查看</el-button>
          <!-- 只有出售中的商品才显示"下架"按钮 -->
          <el-button 
            v-if="scope.row.status === 1" 
            size="mini" 
            type="warning" 
            @click="updateStatus(scope.row.id, 2)"
          >
            下架
          </el-button>
          <!-- 已下架的商品只显示文字 -->
          <span v-if="scope.row.status === 2" style="color:#999;font-size:13px;margin-left:10px;">已下架</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      products: []
    }
  },
  mounted() {
    this.loadProducts()
  },
  methods: {
    async loadProducts() {
      const res = await api.getMyProducts()
      if (res.code === 200) {
        this.products = res.data
      }
    },
    async updateStatus(id, status) {
      const res = await api.updateProductStatus(id, status)
      if (res.code === 200) {
        this.$message.success('操作成功')
        this.loadProducts()
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.my-products {
  max-width: 1200px;
  margin: 30px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}
h2 {
  margin-bottom: 20px;
}
</style>