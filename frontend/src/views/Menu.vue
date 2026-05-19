<template>
  <main>
    <HeroSection />

    <div class="section">
      <div class="section-title">АВТОРСКАЯ КУХНЯ</div>
      <div class="grid-2">
        <div class="card">
          <h3>Традиции</h3>
          <p>Щи, пельмени, борщ, уха по царским рецептам, но в современном прочтении. Томлёное мясо, соленья, каши с грибами и трюфельным маслом.</p>
        </div>
        <div class="card">
          <h3>Современность</h3>
          <p>Международные хиты в русской интерпретации: бургер с квашеной капустой, строганоф, пастрами из утки. Качество продуктов и яркая подача.</p>
        </div>
        <div class="card">
          <h3>Напитки</h3>
          <p>Домашние настойки по секретным рецептам, крафтовое пиво, авторские коктейли на рябине и клюкве, коллекция вин.</p>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">ВКУСЫ РОССИИ</div>
      <div class="menu-teaser">
        <div class="menu-item">Пельмени с олениной</div>
        <div class="menu-item">Борщ с пампушкой</div>
        <div class="menu-item">Котлеты по-киевски</div>
        <div class="menu-item">Расстегаи с сёмгой</div>
        <div class="menu-item">Сбитень медовый</div>
        <div class="menu-item">Домашние настойки</div>
      </div>
      <p class="quote">«Гастробар, где русская душа встречает гастрономический драйв»</p>
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
      <div class="secret-badge">
        <span>Секретные рецепты</span>
      </div>
    </div>

    <div class="section">
      <div class="section-title">АТМОСФЕРА</div>
      <div class="atmosphere-grid">
        <div class="atmosphere-card">
          <div class="atmosphere-icon">✦</div>
          <strong>Пятница Развратница</strong>
          <p>Каждую пятницу живая музыка.</p>
        </div>
        <div class="atmosphere-card">
          <div class="atmosphere-icon">✦</div>
          <strong>Крутой бармен Павел</strong>
          <p>Гости в шоке от его коктейлей</p>
        </div>
        <div class="atmosphere-card">
          <div class="atmosphere-icon">✦</div>
          <strong>Крутой официант Игорь</strong>
          <p>Его скорости позавидует даже Молния Маквин. КЧАУ.</p>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">
import HeroSection from '../components/HeroSection.vue'
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
  position: relative;
  display: inline-block;
  width: 100%;
  letter-spacing: 2px;
  color: #ebd6af;
}

.section-title:after {
  content: '';
  display: block;
  width: 80px;
  height: 2px;
  background: #b8865a;
  margin: 12px auto 0;
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
  backdrop-filter: blur(2px);
  border: 1px solid #3a3329;
  transition: 0.2s;
}

.card h3 {
  font-size: 1.8rem;
  margin-bottom: 16px;
  font-weight: 400;
  color: #efdaa8;
}

.card p {
  font-size: 1rem;
  line-height: 1.5;
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
  font-size: 1rem;
  border-left: 3px solid #c8974b;
  color: #e6d9c2;
}

.quote {
  text-align: center;
  margin-top: 32px;
  font-style: italic;
  color: #cfc3aa;
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
  font-size: 0.95rem;
  font-style: italic;
  color: #e6d9c2;
}

.secret-badge {
  text-align: center;
  margin-top: 30px;
}

.secret-badge span {
  background: #c8974b20;
  padding: 8px 20px;
  border-radius: 60px;
  border: 1px solid #c8974b;
  color: #ebd6af;
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
  font-size: 1.2rem;
  margin: 12px 0 8px;
  color: #efdaa8;
}

.atmosphere-card p {
  font-size: 0.95rem;
  line-height: 1.4;
  color: #cfc3aa;
}

.atmosphere-icon {
  font-size: 1.5rem;
  color: #c8974b;
}

@media (max-width: 760px) {
  .section-title {
    font-size: 1.8rem;
  }
}
</style>