<template>
  <main>
    <div class="section">
      <div class="section-title">ВСЁ МЕНЮ</div>
      
      <div v-if="notification.show" class="notification">
        {{ notification.message }}
      </div>
      
      <div v-if="loading" class="loading">Загрузка...</div>
      <div v-if="error" class="error">{{ error }}</div>
      
      <div v-if="!loading && !error">
        <div v-for="category in categories" :key="category.name" class="category-block">
          <div class="category-header" @click="toggleCategory(category.name)">
            <h2>{{ category.name }}</h2>
            <span class="toggle-icon">{{ category.expanded ? '▼' : '▶' }}</span>
          </div>
          
          <div v-if="category.expanded" class="dishes-grid">
            <div class="dish-card" v-for="dish in category.dishes" :key="dish.id">
              <h3>{{ dish.name }}</h3>
              <div class="price">{{ dish.price }} ₽</div>
              <p class="description">{{ dish.description }}</p>
              <button class="add-to-cart" @click="addToCart(dish)">В корзину</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Dish {
  id: number
  name: string
  price: number
  description: string
  category: string
}

interface Category {
  name: string
  dishes: Dish[]
  expanded: boolean
}

const dishes = ref<Dish[]>([])
const categories = ref<Category[]>([])
const loading = ref(true)
const error = ref('')
const notification = ref<{ show: boolean; message: string }>({ show: false, message: '' })

// Порядок категорий
const categoryOrder = ['Супы', 'Салаты', 'Горячее', 'Горячие напитки', 'Холодные напитки']

const fetchMenu = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/public/menu')
    const data = await response.json()
    if (data.status === 'ok') {
      dishes.value = data.dishes
      groupDishesByCategory()
    } else {
      error.value = 'Ошибка загрузки меню'
    }
  } catch (err) {
    error.value = 'Не удалось подключиться к серверу'
    console.error(err)
  } finally {
    loading.value = false
  }
}

const groupDishesByCategory = () => {
  const categoryMap = new Map<string, Dish[]>()
  
  // Группируем блюда по категориям
  for (const dish of dishes.value) {
    const catName = dish.category
    if (!categoryMap.has(catName)) {
      categoryMap.set(catName, [])
    }
    categoryMap.get(catName)!.push(dish)
  }
  
  // Преобразуем в массив с нужным порядком
  const result: Category[] = []
  for (const catName of categoryOrder) {
    const catDishes = categoryMap.get(catName) || []
    if (catDishes.length > 0) {
      result.push({
        name: catName,
        dishes: catDishes,
        expanded: true // первая категория открыта, остальные закрыты
      })
    }
  }
  
  // Если есть категории не из списка — добавляем в конец
  for (const [catName, catDishes] of categoryMap) {
    if (!categoryOrder.includes(catName)) {
      result.push({
        name: catName,
        dishes: catDishes,
        expanded: false
      })
    }
  }
  
  categories.value = result
}

const toggleCategory = (categoryName: string) => {
  const category = categories.value.find(c => c.name === categoryName)
  if (category) {
    category.expanded = !category.expanded
  }
}

const addToCart = (dish: Dish) => {
  const cart = JSON.parse(localStorage.getItem('cart') || '[]')
  const existingIndex = cart.findIndex((item: any) => item.id === dish.id)
  
  if (existingIndex !== -1) {
    cart[existingIndex].quantity += 1
  } else {
    cart.push({ ...dish, quantity: 1 })
  }
  
  localStorage.setItem('cart', JSON.stringify(cart))
  
  notification.value = { show: true, message: `${dish.name} добавлено в корзину` }
  setTimeout(() => {
    notification.value.show = false
  }, 2000)
}

onMounted(() => {
  fetchMenu()
})
</script>

<style scoped>
.section {
  padding: 60px 24px;
  max-width: 1280px;
  margin: 0 auto;
  min-height: 70vh;
}

.section-title {
  font-size: 2.2rem;
  text-align: center;
  margin-bottom: 40px;
  color: #ebd6af;
}

.category-block {
  margin-bottom: 40px;
  border-bottom: 1px solid #2d2a24;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  padding: 16px 20px;
  background: #1f1b17;
  border-radius: 16px;
  margin-bottom: 20px;
  transition: background 0.2s;
}

.category-header:hover {
  background: #2a2520;
}

.category-header h2 {
  font-size: 1.5rem;
  color: #efdaa8;
  margin: 0;
}

.toggle-icon {
  font-size: 1.2rem;
  color: #c8974b;
}

.dishes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
  margin-bottom: 30px;
}

.dish-card {
  background: #141110e0;
  border-radius: 24px;
  padding: 24px;
  border: 1px solid #3a3329;
  transition: transform 0.2s;
}

.dish-card:hover {
  transform: translateY(-5px);
  border-color: #c8974b;
}

.dish-card h3 {
  font-size: 1.5rem;
  color: #efdaa8;
  margin-bottom: 10px;
}

.dish-card .price {
  font-size: 1.8rem;
  color: #c8974b;
  font-weight: bold;
  margin: 10px 0;
}

.dish-card .description {
  color: #cfc3aa;
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 20px;
}

.add-to-cart {
  background: #c8974b;
  border: none;
  padding: 10px 20px;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: bold;
  color: #1a1614;
  cursor: pointer;
  transition: 0.2s;
  width: 100%;
}

.add-to-cart:hover {
  background: #dbb05c;
  transform: scale(1.02);
}

.notification {
  position: fixed;
  top: 80px;
  right: 20px;
  background: #c8974b;
  color: #1a1614;
  padding: 12px 24px;
  border-radius: 30px;
  font-weight: bold;
  z-index: 1000;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.loading, .error {
  text-align: center;
  padding: 40px;
  color: #cfc3aa;
}
</style>