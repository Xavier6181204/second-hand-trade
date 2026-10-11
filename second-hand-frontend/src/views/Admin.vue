<template>
  <div class="admin">
    <el-button type="text" @click="$router.push('/')" style="margin-bottom:10px;font-size:16px;">
      ← 返回首页
    </el-button>
    <h2>后台管理 - 商品审核</h2>
    <el-tabs v-model="activeTab" @tab-click="loadData">
      <el-tab-pane label="待审核" name="pending">
        <el-table :data="pendingProducts" style="width:100%;">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="sellerName" label="卖家" />
          <el-table-column prop="price" label="价格" width="120" />
          <el-table-column label="状态" width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template slot-scope="scope">
              <el-button size="mini" type="success" @click="audit(scope.row.id, 1)">通过</el-button>
              <el-button size="mini" type="danger" @click="audit(scope.row.id, 2)">驳回</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="全部商品" name="all">
        <el-table :data="allProducts" style="width:100%;">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="sellerName" label="卖家" />
          <el-table-column prop="price" label="价格" width="120" />
          <el-table-column label="状态" width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
              <el-tag v-if="scope.row.status === 1" type="success">出售中</el-tag>
              <el-tag v-if="scope.row.status === 2" type="info">已下架</el-tag>
              <el-tag v-if="scope.row.status === 3" type="danger">已售出</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status === 1" size="mini" type="danger" @click="audit(scope.row.id, 2)">下架</el-button>
              <el-button v-if="scope.row.status === 2" size="mini" type="success" @click="audit(scope.row.id, 1)">上架</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      activeTab: 'pending',
      pendingProducts: [],
      allProducts: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const res = await api.getAdminProductList(null, null)
      if (res.code === 200) {
        this.allProducts = res.data
        this.pendingProducts = res.data.filter(p => p.status === 0)
      }
    },
    async audit(id, status) {
      const res = await api.updateProductStatus(id, status)
      if (res.code === 200) {
        this.$message.success('操作成功')
        this.loadData()
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.admin {
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