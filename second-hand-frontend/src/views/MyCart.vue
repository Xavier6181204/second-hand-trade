<template>
  <div class="my-cart">
    <el-button type="text" @click="$router.push('/')" style="margin-bottom:10px;font-size:16px;">
      ← 返回首页
    </el-button>
    <h2>我的购物车</h2>
    <el-table :data="cartItems" style="width:100%;">
      <el-table-column label="商品" width="250">
        <template slot-scope="scope">
          <div style="display:flex;align-items:center;gap:10px;">
            <img :src="scope.row.productImage ? 'http://localhost:8080' + scope.row.productImage.split(',')[0] : ''" 
                 style="width:60px;height:60px;object-fit:cover;border-radius:4px;" />
            <span>{{ scope.row.productTitle }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="productPrice" label="价格" width="120" />
      <el-table-column prop="sellerName" label="卖家" width="120" />
      <el-table-column label="状态" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.productStatus === 1" type="success">出售中</el-tag>
          <el-tag v-if="scope.row.productStatus !== 1" type="danger">已下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="removeFromCart(scope.row)">移除</el-button>
          <el-button v-if="scope.row.productStatus === 1" size="mini" type="primary" @click="$router.push('/detail/'+scope.row.productId)">
            查看购买
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="cartItems.length === 0" style="text-align:center;padding:40px;color:#999;">
      购物车是空的
    </div>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      cartItems: []
    }
  },
  mounted() {
    this.loadCart()
  },
  methods: {
    async loadCart() {
      const res = await api.getMyCart()
      if (res.code === 200) {
        this.cartItems = res.data
      }
    },
    async removeFromCart(row) {
      const res = await api.removeFromCart(row.id)
      if (res.code === 200) {
        this.$message.success('已移除')
        this.loadCart()
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.my-cart {
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