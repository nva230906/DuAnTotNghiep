<template>
  <div class="layout-wrapper">
    <NavLayout />

    <div class="main-container">
      <HeaderLayout />

      <div class="content-body">
        <RouterView />
      </div>
    </div>
  </div>
</template>

<script setup>
import HeaderLayout from './layouts/HeaderLayout.vue'
import NavLayout from './layouts/NavLayout.vue'
</script>

<style scoped>
.main-container {
  margin-left: 250px; /* Né Sidebar bên trái */
}

.content-body {
  /* 🔥 CỰC KỲ QUAN TRỌNG: 
     Thêm khoảng trống ở trên để nội dung không bị Header đè */
  padding-top: 80px;
  padding-left: 20px;
  padding-right: 20px;
  background-color: #f8f9fa;
  min-height: 100vh;
}
</style>
