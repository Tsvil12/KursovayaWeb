<template>
  <div class="section">
    <h1 class="section-title">КОРЗИНА</h1>
    
    <div v-if="cartItems.length === 0" class="empty-cart">
      <p>Корзина пуста</p>
      <router-link to="/menu" class="to-menu">Перейти в меню</router-link>
    </div>
    
    <div v-else class="cart-content">
      <div class="cart-items">
        <div class="cart-item" v-for="item in cartItems" :key="item.id">
          <div class="item-info">
            <h3>{{ item.name }}</h3>
            <p class="item-price">{{ item.price }} ₽</p>
          </div>
          
          <div class="item-actions">
            <div class="quantity-control">
              <button @click="decrementQuantity(item)" class="qty-btn">-</button>
              <span class="quantity">{{ item.quantity }}</span>
              <button @click="incrementQuantity(item)" class="qty-btn">+</button>
            </div>
            <div class="item-total">
              {{ item.price * item.quantity }} ₽
            </div>
            <button @click="removeItem(item.id)" class="remove-btn">Удалить блюдо</button>
          </div>
        </div>
      </div>
      
      <div class="cart-summary">
        <h3>Способ получения</h3>
        <div class="delivery-options">
          <label class="delivery-option">
            <input type="radio" v-model="deliveryType" value="САМОВЫВОЗ" />
            <span>Самовывоз (бесплатно)</span>
          </label>
          <label class="delivery-option">
            <input type="radio" v-model="deliveryType" value="ДОСТАВКА" />
            <span>Доставка (200 ₽)</span>
          </label>
        </div>
        
        <div v-if="deliveryType === 'ДОСТАВКА'" class="address-input">
          <input 
            type="text" 
            v-model="deliveryAddress" 
            placeholder="Введите адрес доставки"
            class="address-field"
          />
        </div>
        
        <h3>Итого</h3>
        <div class="total-price">{{ totalPrice }} ₽</div>
        <button class="checkout-btn" @click="checkout">Оформить заказ</button>
      </div>
    </div>
    
    <div v-if="notification.show" class="notification">
      {{ notification.message }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

interface CartItem {
  id: number
  name: string
  price: number
  quantity: number
}

const cartItems = ref<CartItem[]>([])
const notification = ref<{ show: boolean; message: string }>({ show: false, message: '' })
const deliveryType = ref('САМОВЫВОЗ')
const deliveryAddress = ref('')

const totalPrice = computed(() => {
  const subtotal = cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  const deliveryFee = deliveryType.value === 'ДОСТАВКА' ? 200 : 0
  return subtotal + deliveryFee
})

const loadCart = () => {
  const cart = localStorage.getItem('cart')
  if (cart) {
    cartItems.value = JSON.parse(cart)
  }
}

const saveCart = () => {
  localStorage.setItem('cart', JSON.stringify(cartItems.value))
}

const incrementQuantity = (item: CartItem) => {
  item.quantity++
  saveCart()
}

const decrementQuantity = (item: CartItem) => {
  if (item.quantity > 1) {
    item.quantity--
    saveCart()
  } else {
    removeItem(item.id)
  }
}

const removeItem = (id: number) => {
  cartItems.value = cartItems.value.filter(item => item.id !== id)
  saveCart()
  showNotification('Блюдо удалено из корзины')
}

const showNotification = (message: string) => {
  notification.value = { show: true, message }
  setTimeout(() => {
    notification.value.show = false
  }, 2000)
}

const checkout = async () => {
  if (cartItems.value.length === 0) {
    showNotification('Корзина пуста')
    return
  }
  
  const userId = localStorage.getItem('userId')
  if (!userId) {
    showNotification('Для оформления заказа войдите в систему')
    return
  }
  
  if (deliveryType.value === 'ДОСТАВКА' && !deliveryAddress.value.trim()) {
    showNotification('Введите адрес доставки')
    return
  }
  
  const orderItems = cartItems.value.map(item => ({
    dishId: item.id,
    quantity: item.quantity
  }))
  
  let url = `http://localhost:8080/api/client/orders?userId=${userId}&deliveryType=${deliveryType.value}`
  if (deliveryType.value === 'ДОСТАВКА' && deliveryAddress.value) {
    url += `&address=${encodeURIComponent(deliveryAddress.value)}`
  }
  
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(orderItems)
    })
    
    const data = await response.json()
    
    if (data.status === 'ok') {
      showNotification(`Заказ №${data.orderId} оформлен! Сумма: ${data.totalSum} ₽`)
      cartItems.value = []
      saveCart()
      deliveryType.value = 'САМОВЫВОЗ'
      deliveryAddress.value = ''
    } else {
      showNotification(data.message || 'Ошибка при оформлении заказа')
    }
  } catch (error) {
    console.error('Ошибка:', error)
    showNotification('Не удалось подключиться к серверу')
  }
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
.section {
  padding: 60px 24px;
  max-width: 1280px;
  margin: 0 auto;
  min-height: 60vh;
}

.section-title {
  font-size: 2.2rem;
  text-align: center;
  margin-bottom: 40px;
  color: #ebd6af;
}

.empty-cart {
  text-align: center;
  padding: 60px 20px;
  background: #1f1b17;
  border-radius: 24px;
}

.empty-cart p {
  color: #cfc3aa;
  font-size: 1.2rem;
  margin-bottom: 20px;
}

.to-menu {
  display: inline-block;
  background: #c8974b;
  color: #1a1614;
  padding: 12px 24px;
  border-radius: 30px;
  text-decoration: none;
  font-weight: bold;
  transition: 0.2s;
}

.to-menu:hover {
  background: #dbb05c;
  transform: scale(1.02);
}

.cart-content {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
}

.cart-items {
  flex: 2;
  min-width: 280px;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #1f1b17;
  padding: 20px;
  border-radius: 16px;
  margin-bottom: 15px;
  flex-wrap: wrap;
  gap: 15px;
}

.item-info h3 {
  color: #efdaa8;
  font-size: 1.2rem;
  margin-bottom: 5px;
}

.item-price {
  color: #c8974b;
  font-weight: bold;
}

.item-actions {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #141110;
  border-radius: 30px;
  padding: 5px 10px;
}

.qty-btn {
  background: #c8974b;
  border: none;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  font-size: 1.2rem;
  cursor: pointer;
  color: #1a1614;
  font-weight: bold;
}

.qty-btn:hover {
  background: #dbb05c;
}

.quantity {
  min-width: 30px;
  text-align: center;
  color: #e6d9c2;
  font-weight: bold;
}

.item-total {
  font-weight: bold;
  color: #efdaa8;
  min-width: 80px;
}

.remove-btn {
  background: none;
  border: none;
  font-size: 0.9rem;
  cursor: pointer;
  color: #9e8e72;
  transition: 0.2s;
}

.remove-btn:hover {
  color: #ff6b6b;
  transform: scale(1.02);
}

.cart-summary {
  flex: 1;
  min-width: 250px;
  background: #1f1b17;
  padding: 24px;
  border-radius: 24px;
  height: fit-content;
  position: sticky;
  top: 100px;
}

.cart-summary h3 {
  color: #efdaa8;
  font-size: 1.3rem;
  margin-bottom: 16px;
  margin-top: 16px;
}

.cart-summary h3:first-of-type {
  margin-top: 0;
}

.delivery-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.delivery-option {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  color: #e6d9c2;
}

.delivery-option input {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.address-input {
  margin-bottom: 20px;
}

.address-field {
  width: 100%;
  padding: 12px;
  background: #141110;
  border: 1px solid #3a3329;
  border-radius: 16px;
  color: #e6d9c2;
  font-size: 1rem;
}

.address-field:focus {
  outline: none;
  border-color: #c8974b;
}

.total-price {
  font-size: 2rem;
  color: #c8974b;
  font-weight: bold;
  margin: 20px 0;
}

.checkout-btn {
  background: #c8974b;
  border: none;
  padding: 14px 28px;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: bold;
  color: #1a1614;
  cursor: pointer;
  width: 100%;
  transition: 0.2s;
}

.checkout-btn:hover {
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
</style>