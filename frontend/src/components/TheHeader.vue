<template>
  <div class="top-bar">
    <router-link to="/" class="logo">
      РУССКИЙ ПАБ
    </router-link>
    <div class="nav">
      <router-link to="/menu">МЕНЮ</router-link>
      <router-link to="/cart">КОРЗИНА</router-link>
      
      <!-- Если авторизован -->
      <template v-if="isAuthenticated">
        <span class="user-name">{{ username }}</span>
        <button @click="handleLogout" class="logout-btn">ВЫЙТИ</button>
        <router-link to="/admin" v-if="isAdmin" class="admin-link">АДМИН</router-link>
      </template>
      
      <!-- Если не авторизован -->
      <router-link v-else to="/login">ВХОД</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const username = ref('')
const isAuthenticated = ref(false)
const isAdmin = ref(false)

const loadUserInfo = () => {
  const token = localStorage.getItem('token')
  const savedUsername = localStorage.getItem('username')
  const savedRole = localStorage.getItem('role')
  
  console.log('Загрузка пользователя:', { token: !!token, savedUsername, savedRole })
  
  if (token && savedUsername) {
    isAuthenticated.value = true
    username.value = savedUsername
    isAdmin.value = savedRole === 'ADMIN'
  } else {
    isAuthenticated.value = false
    username.value = ''
    isAdmin.value = false
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  localStorage.removeItem('username')
  localStorage.removeItem('userId')
  isAuthenticated.value = false
  username.value = ''
  isAdmin.value = false
  router.push('/login')
}

// Загружаем при монтировании
onMounted(() => {
  loadUserInfo()
})

// Следим за изменением маршрута (после логина)
watch(() => route.path, () => {
  loadUserInfo()
})
</script>

<style scoped>
.top-bar {
  background: #000000cc;
  backdrop-filter: blur(4px);
  padding: 16px 24px;
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  flex-wrap: wrap;
  border-bottom: 1px solid #c6a56e;
  position: sticky;
  top: 0;
  z-index: 10;
}

.logo {
  font-size: 1.6rem;
  font-weight: bold;
  letter-spacing: 2px;
  color: #e6c88f;
  text-transform: uppercase;
  font-family: 'Impact', 'Arial Black', sans-serif;
  text-decoration: none;
  transition: opacity 0.2s;
}

.logo:hover {
  opacity: 0.9;
}

.nav {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-wrap: wrap;
}

.nav a {
  color: #e6d9c2;
  text-decoration: none;
  font-size: 1rem;
  font-weight: 500;
  transition: 0.2s;
  border-bottom: 1px solid transparent;
}

.nav a:hover {
  border-bottom-color: #dbb05c;
  color: #f5e2b2;
}

.user-name {
  color: #c8974b;
  font-weight: bold;
}

.logout-btn {
  background: none;
  border: 1px solid #9e8e72;
  color: #e6d9c2;
  padding: 4px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: 0.2s;
  font-size: 0.9rem;
}

.logout-btn:hover {
  background: #9e8e72;
  color: #1a1614;
}

.admin-link {
  background: #c8974b;
  color: #1a1614 !important;
  padding: 4px 12px;
  border-radius: 20px;
  font-weight: bold;
}

.admin-link:hover {
  background: #dbb05c;
  border-bottom-color: transparent !important;
}

@media (max-width: 760px) {
  .top-bar {
    flex-direction: column;
    gap: 12px;
    text-align: center;
  }
  .nav {
    justify-content: center;
  }
}
</style>