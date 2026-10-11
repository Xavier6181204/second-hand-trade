<template>
  <div>
    <div class="detail-container">
      <el-button type="text" @click="goBack" style="margin-bottom:20px;font-size:16px;">
        ← 返回上一页
      </el-button>
      <div v-if="product" class="detail-main">
        <div class="detail-image">
          <img :src="product.images ? 'http://localhost:8080' + product.images.split(',')[0] : ''" alt="商品图片" />
        </div>
        <div class="detail-info">
          <h2>{{ product.title }}</h2>
          <div class="price">¥{{ product.price }}</div>
          <div v-if="product.originalPrice" class="original-price">原价：¥{{ product.originalPrice }}</div>
          <div class="meta">卖家：{{ product.sellerName }}</div>
          <div class="meta">分类：{{ product.categoryName }}</div>
          <div class="meta">浏览次数：{{ product.viewCount }}</div>
          <div class="meta">发布时间：{{ product.createTime | formatDate }}</div>
          <div style="display:flex;gap:10px;margin-top:16px;flex-wrap:wrap;">
            <el-button v-if="user && product.status === 1 && user.id !== product.sellerId" type="danger" size="large" @click="showBuyDialog = true">
              立即购买
            </el-button>
            <el-button v-if="user && product.status === 1 && user.id !== product.sellerId" type="primary" size="large" @click="showMessageDialog = true">
              💬 联系卖家
            </el-button>
            <el-button v-if="user && product.status === 1 && user.id !== product.sellerId" type="success" size="large" plain @click="addToCart">
              ➕ 加入购物车
            </el-button>
          </div>
          <el-tag v-if="product.status === 0" type="warning">待审核</el-tag>
          <el-tag v-if="product.status === 1" type="success">出售中</el-tag>
          <el-tag v-if="product.status === 2" type="info">已下架</el-tag>
          <el-tag v-if="product.status === 3" type="danger">已售出</el-tag>
        </div>
      </div>
      <div class="detail-desc">
        <h4>商品描述</h4>
        <p>{{ product ? product.description : '' }}</p>
      </div>
    </div>

    <!-- 购买弹窗 -->
    <el-dialog title="确认购买" :visible.sync="showBuyDialog" width="400px">
      <p>确认购买该商品吗？</p>
      <el-input v-model="remark" placeholder="给卖家留言（选填）" />
      <span slot="footer">
        <el-button @click="showBuyDialog = false">取消</el-button>
        <el-button type="danger" @click="handleBuy">确认支付</el-button>
      </span>
    </el-dialog>

    <!-- 联系卖家弹窗 -->
    <el-dialog title="联系卖家" :visible.sync="showMessageDialog" width="400px">
      <p><strong>卖家：</strong>{{ product ? product.sellerName : '' }}</p>
      <p><strong>商品：</strong>{{ product ? product.title : '' }}</p>
      <el-input v-model="messageContent" type="textarea" rows="4" placeholder="请输入您的留言..." />
      <span slot="footer">
        <el-button @click="showMessageDialog = false">取消</el-button>
        <el-button type="primary" @click="sendMessage">发送留言</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      product: null,
      showBuyDialog: false,
      remark: '',
      showMessageDialog: false,
      messageContent: ''
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  mounted() {
    this.loadDetail()
  },
  filters: {
    formatDate(val) {
      if (!val) return ''
      const d = new Date(val)
      return d.toLocaleDateString()
    }
  },
  methods: {
    async loadDetail() {
      const id = this.$route.params.id
      const res = await api.getProductDetail(id)
      if (res.code === 200) {
        this.product = res.data
      }
    },
    goBack() {
      const from = this.$route.query.from
      const categoryId = this.$route.query.categoryId
      const tab = this.$route.query.tab

      // 从订单页来的 → 回到我的订单（并切换到对应Tab）
      if (from === 'orders') {
        this.$router.push({ path: '/my-orders', query: { tab: tab || 'sell' } })
        return
      }

      // 从留言页来的 → 回到我的留言（并切换到对应Tab）
      if (from === 'messages') {
        this.$router.push({ path: '/my-messages', query: { tab: tab || 'received' } })
        return
      }

      // 从首页来的 → 回首页
      if (from === 'home') {
        this.$router.push('/')
        return
      }

      // 从分类页面来的 → 回分类页面
      if (from === 'category' && categoryId) {
        this.$router.push({ path: '/', query: { categoryId: categoryId } })
        return
      }

      // 其他情况 → 返回上一页
      this.$router.go(-1)
    },
    async handleBuy() {
      const res = await api.createOrder(this.product.id, this.remark)
      if (res.code === 200) {
        this.$message.success('下单成功')
        this.showBuyDialog = false
        this.loadDetail()
      } else {
        this.$message.error(res.msg)
      }
    },
    async addToCart() {
      const res = await api.addToCart(this.product.id)
      if (res.code === 200) {
        this.$message.success('已加入购物车')
      } else {
        this.$message.error(res.msg)
      }
    },
    async sendMessage() {
      if (!this.messageContent.trim()) {
        this.$message.warning('请输入留言内容')
        return
      }
      const res = await api.sendMessage(this.product.id, this.product.sellerId, this.messageContent)
      if (res.code === 200) {
        this.$message.success('留言已发送')
        this.showMessageDialog = false
        this.messageContent = ''
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.detail-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}
.detail-main {
  display: flex;
  gap: 40px;
  background: #fff;
  padding: 30px;
  border-radius: 8px;
}
.detail-image {
  flex: 0 0 400px;
  height: 400px;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}
.detail-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.detail-info {
  flex: 1;
}
.price {
  color: #f56c6c;
  font-size: 28px;
  font-weight: bold;
  margin: 16px 0;
}
.original-price {
  color: #999;
  text-decoration: line-through;
  margin-bottom: 12px;
}
.meta {
  color: #666;
  padding: 4px 0;
}
.detail-desc {
  background: #fff;
  padding: 20px 30px;
  border-radius: 8px;
  margin-top: 20px;
}
.detail-desc h4 {
  margin-bottom: 12px;
}
</style>