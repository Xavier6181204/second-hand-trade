import Vue from 'vue'
import Vuex from 'vuex'
import api from '../api'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: null
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      if (user) {
        sessionStorage.setItem('user', JSON.stringify(user))
      } else {
        sessionStorage.removeItem('user')
      }
    }
  },
  actions: {
    async login({ commit }, { username, password }) {
      const res = await api.login({ username, password })
      if (res.code === 200) {
        commit('SET_USER', res.data)
        return res
      }
      return res
    },
    async register({ commit }, data) {
      return await api.register(data)
    },
    async getUserInfo({ commit }) {
      const res = await api.getUserInfo()
      if (res.code === 200) {
        commit('SET_USER', res.data)
      }
      return res
    },
    logout({ commit }) {
      commit('SET_USER', null)
    }
  }
})