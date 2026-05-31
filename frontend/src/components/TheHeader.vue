<template>
  <div class="top-bar">
    <div class="logo">
      РУССКИЙ ПАБ
    </div>
    <div class="nav">
      <router-link to="/menu">МЕНЮ</router-link>
      <router-link to="/cart">КОРЗИНА</router-link>
      <router-link to="/login">ВХОД</router-link>
      <router-link to="/admin" v-if="isAdmin">АДМИН</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const isAdmin = ref(false)
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
}

.nav a {
  color: #e6d9c2;
  text-decoration: none;
  margin-left: 24px;
  font-size: 1rem;
  font-weight: 500;
  transition: 0.2s;
  border-bottom: 1px solid transparent;
}

.nav a:hover {
  border-bottom-color: #dbb05c;
  color: #f5e2b2;
}

@media (max-width: 760px) {
  .top-bar {
    flex-direction: column;
    gap: 12px;
    text-align: center;
  }
  .nav a {
    margin: 0 12px;
    font-size: 0.9rem;
  }
}
</style>