<template>
  <div class="my-messages">
    <el-button type="text" @click="$router.push('/')" style="margin-bottom:10px;font-size:16px;">
      ← 返回首页
    </el-button>
    <h2>我的留言</h2>
    
    <el-tabs v-model="activeTab" @tab-click="loadMessages">
      <el-tab-pane label="我收到的" name="received">
        <el-table :data="receivedMessages" style="width:100%;">
          <el-table-column prop="productTitle" label="商品" width="150" />
          <el-table-column prop="fromUserName" label="留言人" width="120" />
          <el-table-column prop="content" label="留言内容" />
          <el-table-column prop="reply" label="我的回复" />
          <el-table-column label="状态" width="120">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0" type="warning">未回复</el-tag>
              <el-tag v-if="scope.row.status === 1" type="success">已回复</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status === 0" size="mini" type="primary" @click="showReplyDialog(scope.row)">回复</el-button>
              <el-button size="mini" type="text" @click="goToDetail(scope.row.productId)">查看商品</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-if="receivedMessages.length === 0" style="text-align:center;padding:20px;color:#999;">
          暂无收到的留言
        </div>
      </el-tab-pane>

      <el-tab-pane label="我发出的" name="sent">
        <el-table :data="sentMessages" style="width:100%;">
          <el-table-column prop="productTitle" label="商品" width="150" />
          <el-table-column prop="toUserName" label="发送给" width="120" />
          <el-table-column prop="content" label="我的留言" />
          <el-table-column prop="reply" label="卖家回复" />
          <el-table-column label="状态" width="120">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0" type="warning">未回复</el-tag>
              <el-tag v-if="scope.row.status === 1" type="success">已回复</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="goToDetail(scope.row.productId)">查看商品</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-if="sentMessages.length === 0" style="text-align:center;padding:20px;color:#999;">
          暂无发出的留言
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 回复弹窗 -->
    <el-dialog title="回复留言" :visible.sync="dialogVisible" width="400px">
      <p><strong>留言人：</strong>{{ currentMessage.fromUserName }}</p>
      <p><strong>留言内容：</strong>{{ currentMessage.content }}</p>
      <el-input v-model="replyContent" type="textarea" rows="3" placeholder="请输入回复内容" />
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReply">确认回复</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      activeTab: 'received',
      receivedMessages: [],
      sentMessages: [],
      dialogVisible: false,
      currentMessage: {},
      replyContent: ''
    }
  },
  mounted() {
    const tab = this.$route.query.tab
    if (tab && (tab === 'received' || tab === 'sent')) {
      this.activeTab = tab
    }
    this.loadMessages()
  },
  methods: {
    async loadMessages() {
      const [receivedRes, sentRes] = await Promise.all([
        api.getReceivedMessages(),
        api.getSentMessages()
      ])
      if (receivedRes.code === 200) {
        this.receivedMessages = receivedRes.data
      }
      if (sentRes.code === 200) {
        this.sentMessages = sentRes.data
      }
    },
    goToDetail(productId) {
      this.$router.push({
        path: '/detail/' + productId,
        query: { from: 'messages', tab: this.activeTab }
      })
    },
    showReplyDialog(row) {
      this.currentMessage = row
      this.replyContent = ''
      this.dialogVisible = true
    },
    async handleReply() {
      if (!this.replyContent.trim()) {
        this.$message.warning('请输入回复内容')
        return
      }
      const res = await api.replyMessage(this.currentMessage.id, this.replyContent)
      if (res.code === 200) {
        this.$message.success('回复成功')
        this.dialogVisible = false
        this.loadMessages()
      } else {
        this.$message.error(res.msg)
      }
    }
  }
}
</script>

<style scoped>
.my-messages {
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