<template>
  <div class="my-orders">
    <el-button type="text" @click="$router.push('/')" style="margin-bottom:10px;font-size:16px;">
      ← 返回首页
    </el-button>
    <h2>我的订单</h2>
    <el-tabs v-model="activeTab" @tab-click="loadOrders">
      <el-tab-pane label="我买到的" name="buy">
        <el-table :data="buyOrders" style="width:100%;">
          <el-table-column prop="orderNo" label="订单号" width="200" />
          <el-table-column prop="productTitle" label="商品" />
          <el-table-column prop="totalAmount" label="金额" width="120" />
          <el-table-column prop="buyerRemark" label="买家留言" width="150" />
          <el-table-column label="状态" width="120">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0">待付款</el-tag>
              <el-tag v-if="scope.row.status === 1" type="warning">待发货</el-tag>
              <el-tag v-if="scope.row.status === 2" type="primary">待收货</el-tag>
              <el-tag v-if="scope.row.status === 3" type="success">已完成</el-tag>
              <el-tag v-if="scope.row.status === 4" type="info">已取消</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status === 2" size="mini" type="success" @click="confirmReceive(scope.row.id)">确认收货</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="我卖出的" name="sell">
        <el-table :data="sellOrders" style="width:100%;">
          <el-table-column prop="orderNo" label="订单号" width="200" />
          <el-table-column prop="productTitle" label="商品" />
          <el-table-column prop="totalAmount" label="金额" width="120" />
          <el-table-column prop="buyerRemark" label="买家留言" width="150" />
          <el-table-column label="状态" width="120">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0">待付款</el-tag>
              <el-tag v-if="scope.row.status === 1" type="warning">待发货</el-tag>
              <el-tag v-if="scope.row.status === 2" type="primary">待收货</el-tag>
              <el-tag v-if="scope.row.status === 3" type="success">已完成</el-tag>
              <el-tag v-if="scope.row.status === 4" type="info">已取消</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="goToDetail(scope.row.productId)">查看商品</el-button>
              <el-button v-if="scope.row.status === 0" size="mini" type="danger" @click="cancelOrder(scope.row.id)">取消</el-button>
              <el-button v-if="scope.row.status === 1" size="mini" type="primary" @click="shipOrder(scope.row.id)">发货</el-button>
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
      activeTab: 'buy',
      buyOrders: [],
      sellOrders: []
    }
  },
  mounted() {
    const tab = this.$route.query.tab
    if (tab && (tab === 'buy' || tab === 'sell')) {
      this.activeTab = tab
    }
    this.loadOrders()
  },
  methods: {
    async loadOrders() {
      const [buyRes, sellRes] = await Promise.all([
        api.getMyBuyOrders(),
        api.getMySellOrders()
      ])
      if (buyRes.code === 200) this.buyOrders = buyRes.data
      if (sellRes.code === 200) this.sellOrders = sellRes.data
    },
    goToDetail(productId) {
      this.$router.push({
        path: '/detail/' + productId,
        query: { from: 'orders', tab: this.activeTab }
      })
    },
    async confirmReceive(id) {
      const res = await api.updateOrderStatus(id, 3)
      if (res.code === 200) {
        this.$message.success('确认收货成功')
        this.loadOrders()
      } else {
        this.$message.error(res.msg)
      }
    },
    async shipOrder(id) {
      const res = await api.updateOrderStatus(id, 2)
      if (res.code === 200) {
        this.$message.success('发货成功')
        this.loadOrders()
      } else {
        this.$message.error(res.msg)
      }
    },
    async cancelOrder(id) {
      const res = await api.updateOrderStatus(id, 4)
      if (res.code === 200) {
        this.$message.success('已取消')
        this.loadOrders()
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.my-orders {
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