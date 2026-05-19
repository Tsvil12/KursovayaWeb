<template>
  <div class="hero">
    <div class="hero-content">
        <h1>Паб Русский</h1>
      <div class="tagline">Цвиль Павел и Гергет Игорь</div>
      <div class="sub">Тц. Гринвич</div>
    </div>
  </div>
</template>

<style scoped>
.hero {
  background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.75)), 
              url('https://images.unsplash.com/photo-1552566626-52f8b828add9?q=80&w=2070&auto=format') center/cover no-repeat;
  min-height: 70vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 2rem;
}

.hero-content h1 {
  font-size: 3.8rem;
  font-weight: 400;
  letter-spacing: 4px;
  text-transform: uppercase;
  color: #f5e2b0;
  text-shadow: 0 4px 20px black;
}

.hero-content .tagline {
  font-size: 1.4rem;
  margin: 1rem 0;
  word-spacing: 8px;
  font-style: italic;
}

.hero-content .sub {
  font-size: 1.1rem;
  color: #dfcba4;
  max-width: 700px;
  margin: 1rem auto;
}

@media (max-width: 760px) {
  .hero-content h1 {
    font-size: 2.2rem;
  }
  .tagline {
    font-size: 1rem;
  }
}
</style>