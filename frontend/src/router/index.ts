// frontend/src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import MenuPage from '../views/Menu.vue'
import CartPage from '../views/Cart.vue'
import LoginPage from '../views/Login.vue'
import AdminPage from '../views/Admin.vue'

const routes = [
  { path: '/', name: 'Menu', component: MenuPage },
  { path: '/cart', name: 'Cart', component: CartPage },
  { path: '/login', name: 'Login', component: LoginPage },
  { path: '/admin', name: 'Admin', component: AdminPage },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router