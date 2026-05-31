<template>
  <main>
    <HeroSection />

    <div class="section">
      <div class="section-title">АВТОРСКАЯ КУХНЯ</div>
      <div class="grid-2">
        <div class="card">
          <h3>Традиции</h3>
          <p>Щи, пельмени, борщ, уха по царским рецептам, но в современном прочтении.</p>
        </div>
        <div class="card">
          <h3>Русский вайб</h3>
          <p>Настоящие русские блюда от охотников, дичь, стейки из марала</p>
        </div>
        <div class="card">
          <h3>Напитки</h3>
          <p>Домашние настойки, крафтовое пиво, авторские коктейли на облепихе и клюкве.</p>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">БЛЮДА НЕДЕЛИ</div>
      <div v-if="loading" class="loading">Загрузка...</div>
      <div v-if="error" class="error">{{ error }}</div>
      <div class="menu-teaser" v-if="!loading && !error">
        <div class="menu-item" v-for="dish in bestDishes" :key="dish.id">
          {{ dish.name }} — {{ dish.price }} ₽
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">НАСТОЙКИ И АВТОРСКИЕ КОКТЕЙЛИ</div>
      <ul class="drinks-row">
        <li>Клюква на коньяке</li>
        <li>Рябиновая настойка</li>
        <li>Хреновуха мятная</li>
        <li>Медовуха с пряностями</li>
        <li>Крафтовое пиво с таёжными травами</li>
      </ul>
    </div>

    <div class="section">
      <div class="section-title">АТМОСФЕРА</div>
      <div class="atmosphere-grid">
        <div class="atmosphere-card">
          <div class="atmosphere-icon">✦</div>
          <strong>Шарм русского паба</strong>
          <p>Живая музыка, деревянная отделка, уютный зал.</p>
        </div>
        <div class="atmosphere-card">
          <div class="atmosphere-icon">✦</div>
          <strong>Приветливый персонал</strong>
          <p>Наши официанты знают свое дело.</p>
        </div>
        <div class="atmosphere-card">
          <div class="atmosphere-icon">✦</div>
          <strong>Вечеринки и застолья</strong>
          <p>Каждые выходные какое-либо мероприятие.</p>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import HeroSection from '../components/HeroSection.vue'

interface Dish {
  id: number
  name: string
  price: number
  description: string
  category: string
}

const dishes = ref<Dish[]>([])
const loading = ref(true)
const error = ref('')

// Показываем только первые 3 блюда
const bestDishes = computed(() => dishes.value.slice(0, 3))

const fetchMenu = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/public/menu')
    const data = await response.json()
    if (data.status === 'ok') {
      dishes.value = data.dishes
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

onMounted(() => {
  fetchMenu()
})
</script>

<style scoped>
.section {
  padding: 60px 24px;
  max-width: 1280px;
  margin: 0 auto;
  border-bottom: 1px solid #2d2a24;
}
.section-title {
  font-size: 2.2rem;
  text-align: center;
  margin-bottom: 40px;
  color: #ebd6af;
}
.grid-2 {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  justify-content: space-between;
}
.card {
  flex: 1;
  min-width: 240px;
  background: #141110e0;
  padding: 28px 20px;
  border-radius: 24px;
  border: 1px solid #3a3329;
}
.card h3 {
  font-size: 1.8rem;
  color: #efdaa8;
}
.card p {
  color: #cfc3aa;
}
.menu-teaser {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  margin-top: 20px;
}
.menu-item {
  background: #1f1b17;
  padding: 12px 28px;
  border-radius: 60px;
  font-weight: bold;
  border-left: 3px solid #c8974b;
  color: #e6d9c2;
}
.drinks-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  list-style: none;
}
.drinks-row li {
  background: #00000066;
  padding: 8px 24px;
  border-radius: 40px;
  color: #e6d9c2;
}
.atmosphere-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}
.atmosphere-card {
  background: #21201e;
  border-radius: 32px;
  padding: 24px;
  flex: 1;
  min-width: 200px;
  color: #cfc3aa;
}
.atmosphere-card strong {
  display: block;
  color: #efdaa8;
}
.loading, .error {
  text-align: center;
  padding: 40px;
  color: #cfc3aa;
}
</style>