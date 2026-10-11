<template>
  <div>
    <!-- 导航栏 -->
    <el-menu :default-active="activeIndex" mode="horizontal" class="header-menu">
      <el-menu-item index="0" class="logo" @click="goHome">📦 校园闲置交易</el-menu-item>
      <el-menu-item index="1" @click="goHome">首页</el-menu-item>
      <el-menu-item v-for="c in categories" :key="c.id" :index="'c'+c.id" @click="filterByCategory(c.id)">
        {{ c.name }}
      </el-menu-item>
      <div style="float:right;display:flex;align-items:center;gap:10px;height:60px;padding-right:20px;">
        <el-input v-model="keyword" placeholder="搜索商品..." style="width:200px;" size="small" clearable @keyup.enter="search()" />
        <el-button type="primary" size="small" @click="search()">搜索</el-button>
        <el-button v-if="!user" type="text" @click="$router.push('/login')">登录</el-button>
        <el-button v-if="!user" type="text" @click="$router.push('/register')">注册</el-button>
        <el-dropdown v-if="user" @command="handleCommand">
          <span style="cursor:pointer;">{{ user.username }} ▼</span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="publish">发布商品</el-dropdown-item>
            <el-dropdown-item command="cart">🛒 购物车</el-dropdown-item>
            <el-dropdown-item command="messages">💬 我的留言</el-dropdown-item>
            <el-dropdown-item command="my-products">我的商品</el-dropdown-item>
            <el-dropdown-item command="my-orders">我的订单</el-dropdown-item>
            <el-dropdown-item v-if="user.role === 'admin'" command="admin">后台管理</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-menu>

    <!-- 商品列表 -->
    <div class="container">
      <div class="product-grid">
        <div v-for="p in products" :key="p.id" class="product-card" @click="goToDetail(p.id)">
          <div class="product-image">
            <img :src="p.images ? 'http://localhost:8080' + p.images.split(',')[0] : ''" alt="商品图片" />
          </div>
          <div class="product-info">
            <div class="product-title">{{ p.title }}</div>
            <div class="product-price">¥{{ p.price }}</div>
            <div class="product-meta">{{ p.createTime | formatDate }}</div>
          </div>
        </div>
      </div>
      <div v-if="products.length === 0" style="text-align:center;padding:50px;color:#999;">
        暂无商品
      </div>
    </div>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      activeIndex: '1',
      categories: [],
      products: [],
      keyword: '',
      categoryId: null
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  mounted() {
    this.loadCategories()
    const categoryId = this.$route.query.categoryId
    if (categoryId) {
      this.categoryId = parseInt(categoryId)
      this.activeIndex = 'c' + categoryId
    }
    this.loadProducts()
  },
  filters: {
    formatDate(val) {
      if (!val) return ''
      const d = new Date(val)
      return d.toLocaleDateString()
    }
  },
  methods: {
    async loadCategories() {
      const res = await api.getCategoryList()
      if (res.code === 200) {
        this.categories = res.data
      }
    },
    async loadProducts() {
      const res = await api.getProductList(this.categoryId, this.keyword)
      if (res.code === 200) {
        this.products = res.data
      }
    },
    goHome() {
      this.categoryId = null
      this.keyword = ''
      this.activeIndex = '1'
      this.$router.push('/')
      this.loadProducts()
    },
    filterByCategory(id) {
      this.categoryId = id
      this.keyword = ''
      this.activeIndex = 'c' + id
      this.$router.push({ path: '/', query: { categoryId: id } })
      this.loadProducts()
    },
    search() {
      this.categoryId = null
      this.loadProducts()
    },
    goToDetail(productId) {
      // 携带当前分类信息跳转
      const query = {}
      if (this.categoryId) {
        query.categoryId = this.categoryId
        query.from = 'category'
      } else {
        query.from = 'home'
      }
      this.$router.push({ path: '/detail/' + productId, query })
    },
    handleCommand(cmd) {
      if (cmd === 'logout') {
        this.$store.dispatch('logout')
        this.$message.success('已退出')
        this.$router.go(0)
      } else if (cmd === 'publish') {
        this.$router.push('/publish')
      } else if (cmd === 'cart') {
        this.$router.push('/my-cart')
      } else if (cmd === 'messages') {
        this.$router.push('/my-messages')
      } else if (cmd === 'my-products') {
        this.$router.push('/my-products')
      } else if (cmd === 'my-orders') {
        this.$router.push('/my-orders')
      } else if (cmd === 'admin') {
        this.$router.push('/admin')
      }
    }
  }
}
</script>

<style scoped>
.header-menu {
  background: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.logo {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
  cursor: pointer;
}
.container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
}
.product-image {
  height: 200px;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.product-info {
  padding: 12px 16px;
}
.product-title {
  font-size: 14px;
  font-weight: 500;
  line-height: 1.4;
  height: 40px;
  overflow: hidden;
}
.product-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin: 6px 0;
}
.product-meta {
  color: #999;
  font-size: 12px;
}
</style>