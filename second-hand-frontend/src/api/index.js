import axios from 'axios'

const baseURL = 'http://localhost:8080/api'

const request = axios.create({
    baseURL,
    timeout: 60000,
    withCredentials: true
})

request.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
        if (error.response && error.response.status === 401) {
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default {
    // 用户
    login(params) {
        return request.post('/user/login', null, { params })
    },
    register(data) {
        return request.post('/user/register', data)
    },
    getUserInfo() {
        return request.get('/user/info')
    },
    logout() {
        return request.post('/user/logout')
    },
    updateUser(data) {
        return request.post('/user/update', data)
    },

    // 分类
    getCategoryList() {
        return request.get('/category/list')
    },

    // 商品 - 前台首页
    getProductList(categoryId, keyword) {
        return request.get('/product/list', { params: { categoryId, keyword } })
    },

    // 商品 - 后台管理
    getAdminProductList(categoryId, keyword) {
        return request.get('/product/admin/list', { params: { categoryId, keyword } })
    },

    getProductDetail(id) {
        return request.get(`/product/detail/${id}`)
    },
    getMyProducts() {
        return request.get('/product/my')
    },
    addProduct(data) {
        return request.post('/product/add', null, { params: data })
    },
    updateProduct(data) {
        return request.post('/product/update', null, { params: data })
    },
    updateProductStatus(id, status) {
        return request.post('/product/status', null, { params: { id, status } })
    },
    uploadImage(file) {
        const formData = new FormData()
        formData.append('file', file)
        return axios.post('http://localhost:8080/api/product/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
            withCredentials: true
        })
    },

    // 订单
    createOrder(productId, remark) {
        return request.post('/order/create', null, { params: { productId, remark } })
    },
    getMyBuyOrders() {
        return request.get('/order/my-buy')
    },
    getMySellOrders() {
        return request.get('/order/my-sell')
    },
    updateOrderStatus(id, status) {
        return request.post('/order/status', null, { params: { id, status } })
    },

    // 留言
    sendMessage(productId, toUserId, content) {
        return request.post('/message/send', null, { params: { productId, toUserId, content } })
    },
    getReceivedMessages() {
        return request.get('/message/received')
    },
    getSentMessages() {
        return request.get('/message/sent')
    },
    replyMessage(id, reply) {
        return request.post('/message/reply', null, { params: { id, reply } })
    },

    // 购物车
    addToCart(productId) {
        return request.post('/cart/add', null, { params: { productId } })
    },
    removeFromCart(id) {
        return request.post('/cart/remove', null, { params: { id } })
    },
    getMyCart() {
        return request.get('/cart/my')
    },
    getCartCount() {
        return request.get('/cart/count')
    },

    // AI 生成商品描述
    generateDescription(title, category) {
        return request.post('/ai/generate-desc', null, { params: { title, category } })
    }
}