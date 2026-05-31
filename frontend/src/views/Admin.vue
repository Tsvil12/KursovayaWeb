<template>
  <div class="section">
    <h1 class="section-title">АДМИН-ПАНЕЛЬ</h1>
    
    <div v-if="!isAdmin" class="access-denied">
      <p>Доступ запрещён. Только для администраторов.</p>
    </div>
    
    <div v-else>
      <div class="tabs">
        <button :class="{ active: activeTab === 'dishes' }" @click="activeTab = 'dishes'">Блюда</button>
        <button :class="{ active: activeTab === 'orders' }" @click="activeTab = 'orders'">Заказы</button>
        <button :class="{ active: activeTab === 'featured' }" @click="activeTab = 'featured'">Блюда недели</button>
      </div>
      
      <!-- Блюда -->
      <div v-if="activeTab === 'dishes'">
        <button class="add-btn" @click="showAddForm = !showAddForm">+ Добавить блюдо</button>
        
        <div v-if="showAddForm" class="add-form">
          <h3>Новое блюдо</h3>
          <input v-model="newDish.name" placeholder="Название" />
          <input v-model="newDish.price" type="number" placeholder="Цена" step="1" />
          <input v-model="newDish.description" placeholder="Описание" />
          <select v-model="newDish.categoryId">
            <option :value="null">Выберите категорию</option>
            <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
          </select>
          <button @click="addDish">Сохранить</button>
          <button @click="showAddForm = false">Отмена</button>
        </div>
        
        <div class="dishes-list">
          <div class="dish-item" v-for="dish in dishes" :key="dish.id">
            <div class="dish-info">
              <strong>{{ dish.name }}</strong> — {{ dish.price }} ₽
              <span v-if="dish.category" class="category">({{ dish.category }})</span>
            </div>
            <div class="dish-actions">
              <button @click="deleteDish(dish.id)" class="delete-btn">Удалить блюдо</button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Заказы -->
      <div v-if="activeTab === 'orders'">
        <div class="orders-list">
          <div class="order-card" v-for="order in orders" :key="order.id">
            <div class="order-header">
              <span>Заказ №{{ order.id }} | {{ order.date }}</span>
              <select :value="order.status" @change="updateStatus(order.id, ($event.target as HTMLSelectElement).value)">
                <option value="PENDING">Ожидает</option>
                <option value="PAID">Оплачен</option>
                <option value="CANCELLED">Отменён</option>
              </select>
            </div>
            <div class="order-body">
              <div>Клиент: {{ order.userName }} | {{ order.deliveryType === 'ДОСТАВКА' ? 'Доставка' : 'Самовывоз' }}</div>
              <div v-if="order.address">Адрес: {{ order.address }}</div>
              <div>Сумма: {{ order.totalSum }} ₽</div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Блюда недели -->
      <div v-if="activeTab === 'featured'">
        <div class="featured-list">
          <div class="featured-item" v-for="dish in allDishes" :key="dish.id">
            <span>{{ dish.name }} — {{ dish.price }} ₽</span>
            <button v-if="!dish.isFeatured" @click="setFeatured(dish.id, true)">★ Добавить</button>
            <button v-else @click="setFeatured(dish.id, false)">★ Убрать</button>
          </div>
        </div>
        <div class="featured-preview">
          <h3>Блюда недели</h3>
          <div class="featured-dishes">
            <div v-for="dish in featuredDishes" :key="dish.id">{{ dish.name }} — {{ dish.price }} ₽</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const activeTab = ref('dishes')
const isAdmin = ref(false)
const dishes = ref<any[]>([])
const allDishes = ref<any[]>([])
const orders = ref<any[]>([])
const featuredDishes = ref<any[]>([])
const showAddForm = ref(false)
const newDish = ref({ name: '', price: 0, description: '', categoryId: null })

const categories = ref([
  { id: 1, name: 'Супы' },
  { id: 2, name: 'Салаты' },
  { id: 3, name: 'Горячее' },
  { id: 4, name: 'Горячие напитки' },
  { id: 5, name: 'Холодные напитки' }
])

const loadData = async () => {
  const menuRes = await fetch('http://localhost:8080/api/public/menu')
  const menuData = await menuRes.json()
  if (menuData.status === 'ok') {
    dishes.value = menuData.dishes
    allDishes.value = menuData.dishes
  }
  
  const ordersRes = await fetch('http://localhost:8080/api/admin/orders')
  if (ordersRes.ok) {
    orders.value = await ordersRes.json()
  }
  
  const featuredRes = await fetch('http://localhost:8080/api/public/featured')
  const featuredData = await featuredRes.json()
  if (featuredData.status === 'ok') {
    featuredDishes.value = featuredData.dishes
  }
}

const addDish = async () => {
  if (!newDish.value.name.trim()) {
    alert('Введите название блюда')
    return
  }
  
  if (!newDish.value.price || newDish.value.price <= 0) {
    alert('Введите корректную цену')
    return
  }
  
  const dishData = {
    name: newDish.value.name.trim(),
    price: Number(newDish.value.price),
    description: newDish.value.description.trim() || 'Без описания',
    isFeatured: false
  }
  
  if (newDish.value.categoryId) {
    (dishData as any).category = { id: Number(newDish.value.categoryId) }
  }
  
  console.log('Отправка:', dishData)
  
  try {
    const response = await fetch('http://localhost:8080/api/admin/dishes', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(dishData)
    })
    
    const text = await response.text()
    console.log('Ответ:', response.status, text)
    
    if (response.ok) {
      showAddForm.value = false
      newDish.value = { name: '', price: 0, description: '', categoryId: null }
      loadData()
      alert('Блюдо добавлено!')
    } else {
      alert(`Ошибка ${response.status}: ${text}`)
    }
  } catch (error) {
    console.error('Ошибка:', error)
    alert('Ошибка при добавлении')
  }
}

const deleteDish = async (id: number) => {
  if (confirm('Удалить блюдо?')) {
    await fetch(`http://localhost:8080/api/admin/dishes/${id}`, {
      method: 'DELETE'
    })
    loadData()
  }
}

const updateStatus = async (orderId: number, status: string) => {
  await fetch(`http://localhost:8080/api/admin/orders/${orderId}/status?status=${status}`, {
    method: 'PUT'
  })
  loadData()
}

const setFeatured = async (dishId: number, featured: boolean) => {
  await fetch(`http://localhost:8080/api/admin/dishes/${dishId}/featured?featured=${featured}`, {
    method: 'PUT'
  })
  loadData()
}

onMounted(() => {
  isAdmin.value = localStorage.getItem('role') === 'ADMIN'
  loadData()
})
</script>

<style scoped>
.section { padding: 60px 24px; max-width: 1280px; margin: 0 auto; min-height: 60vh; }
.section-title { font-size: 2.2rem; text-align: center; margin-bottom: 40px; color: #ebd6af; }
.access-denied { text-align: center; padding: 60px; background: #1f1b17; border-radius: 24px; color: #ff6b6b; }
.tabs { display: flex; gap: 10px; margin-bottom: 30px; border-bottom: 1px solid #3a3329; padding-bottom: 10px; }
.tabs button { background: none; border: none; padding: 10px 24px; cursor: pointer; color: #cfc3aa; border-radius: 30px; }
.tabs button.active { background: #c8974b; color: #1a1614; }
.add-btn { background: #c8974b; border: none; padding: 10px 20px; border-radius: 30px; margin-bottom: 20px; cursor: pointer; }
.add-form { background: #1f1b17; padding: 24px; border-radius: 24px; margin-bottom: 20px; display: flex; flex-direction: column; gap: 15px; }
.add-form input, .add-form select { padding: 10px; background: #141110; border: 1px solid #3a3329; border-radius: 12px; color: #e6d9c2; }
.add-form button { width: 100px; padding: 8px; border-radius: 20px; cursor: pointer; }
.dishes-list, .orders-list { display: flex; flex-direction: column; gap: 12px; }
.dish-item, .order-card { background: #1f1b17; padding: 16px 24px; border-radius: 16px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; }
.order-card { flex-direction: column; align-items: stretch; gap: 10px; }
.order-header { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; }
.category { color: #c8974b; margin-left: 5px; }
.featured-list { display: flex; flex-direction: column; gap: 12px; margin-bottom: 40px; }
.featured-item { display: flex; justify-content: space-between; align-items: center; background: #1f1b17; padding: 12px 20px; border-radius: 16px; flex-wrap: wrap; gap: 10px; }
.featured-preview { background: #1f1b17; padding: 24px; border-radius: 24px; }
.featured-dishes { display: flex; gap: 20px; flex-wrap: wrap; margin-top: 16px; }
.featured-dishes div { background: #c8974b20; padding: 8px 16px; border-radius: 30px; color: #c8974b; }
.delete-btn { background: none; border: none; font-size: 1.2rem; cursor: pointer; color: #c8974b; }
</style>