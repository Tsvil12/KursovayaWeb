<template>
  <div class="section">
    <h1 class="section-title">ВХОД</h1>
    
    <div class="login-form">
      <div class="form-group">
        <label>Логин</label>
        <input type="text" v-model="username" placeholder="Введите логин" />
      </div>
      
      <div class="form-group">
        <label>Пароль</label>
        <input type="password" v-model="password" placeholder="Введите пароль" @keyup.enter="login" />
      </div>
      
      <button class="login-btn" @click="login" :disabled="loading">
        {{ loading ? 'Вход...' : 'Войти' }}
      </button>
      
      <div class="register-link">
        Нет аккаунта? <router-link to="/register">Зарегистрироваться</router-link>
      </div>
      
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)
const errorMessage = ref('')

const login = async () => {
  if (!username.value || !password.value) {
    errorMessage.value = 'Введите логин и пароль'
    return
  }
  
  loading.value = true
  errorMessage.value = ''
  
  try {
    const response = await fetch(`http://localhost:8080/api/auth/login?username=${username.value}&password=${password.value}`, {
      method: 'POST'
    })
    
    const data = await response.json()
    
    if (data.status === 'ok') {
      localStorage.setItem('token', data.token)
      localStorage.setItem('role', data.role)
      localStorage.setItem('username', data.username)
      localStorage.setItem('userId', data.userId)
      
      window.location.href = '/'
    } else {
      errorMessage.value = data.message || 'Ошибка входа'
    }
  } catch (error) {
    console.error('Ошибка:', error)
    errorMessage.value = 'Не удалось подключиться к серверу'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.section {
  padding: 60px 24px;
  max-width: 500px;
  margin: 0 auto;
  min-height: 60vh;
}

.section-title {
  font-size: 2.2rem;
  text-align: center;
  margin-bottom: 40px;
  color: #ebd6af;
}

.login-form {
  background: #1f1b17;
  padding: 40px;
  border-radius: 24px;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #efdaa8;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  background: #141110;
  border: 1px solid #3a3329;
  border-radius: 16px;
  color: #e6d9c2;
  font-size: 1rem;
}

.form-group input:focus {
  outline: none;
  border-color: #c8974b;
}

.login-btn {
  width: 100%;
  background: #c8974b;
  border: none;
  padding: 14px 28px;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: bold;
  color: #1a1614;
  cursor: pointer;
  transition: 0.2s;
}

.login-btn:hover:not(:disabled) {
  background: #dbb05c;
  transform: scale(1.02);
}

.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #cfc3aa;
}

.register-link a {
  color: #c8974b;
  text-decoration: none;
}

.error-message {
  margin-top: 16px;
  padding: 10px;
  background: #ff6b6b20;
  border: 1px solid #ff6b6b;
  border-radius: 12px;
  color: #ff6b6b;
  text-align: center;
}
</style>