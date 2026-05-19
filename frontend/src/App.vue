<template>
  <div>
    <TheHeader />
    <router-view />
    <TheFooter />
  </div>
</template>

<script setup lang="ts">
import TheHeader from './components/TheHeader.vue'
import TheFooter from './components/TheFooter.vue'
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  background-color: #1a1614;
  color: #e8dfd3;
  font-family: 'Georgia', 'Times New Roman', 'Segoe UI', serif;
  line-height: 1.4;
}

#app {
  background-color: #1a1614;
  min-height: 100vh;
}
</style>