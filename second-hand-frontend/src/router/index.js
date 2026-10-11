import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Detail from '../views/Detail.vue'
import Publish from '../views/Publish.vue'
import MyProducts from '../views/MyProducts.vue'
import MyOrders from '../views/MyOrders.vue'
import Admin from '../views/Admin.vue'
import MyMessages from '../views/MyMessages.vue'
import MyCart from '../views/MyCart.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/detail/:id', name: 'Detail', component: Detail },
  { path: '/publish', name: 'Publish', component: Publish },
  { path: '/my-products', name: 'MyProducts', component: MyProducts },
  { path: '/my-orders', name: 'MyOrders', component: MyOrders },
  { path: '/admin', name: 'Admin', component: Admin },
  { path: '/my-messages', name: 'MyMessages', component: MyMessages },
  { path: '/my-cart', name: 'MyCart', component: MyCart }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

// 路由守卫：公开页面不需要登录
const publicPaths = ['/', '/login', '/register']

router.beforeEach((to, from, next) => {
  const user = JSON.parse(sessionStorage.getItem('user'))
  const isPublic = publicPaths.includes(to.path) || to.path.startsWith('/detail/')
  
  if (!isPublic && !user) {
    next('/login')
  } else {
    next()
  }
})

// 解决重复导航报错
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => {
    if (err.name === 'NavigationDuplicated') {
      return err
    }
    throw err
  })
}

export default router