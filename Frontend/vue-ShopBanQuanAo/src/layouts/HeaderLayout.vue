<template>
  <header class="header">
    <div class="header-inner">
      <span class="breadcrumb">Trang chủ / Phân ca</span>
      <div class="user-info">
        <span>Xin chào, Admin</span>
      </div>
    </div>
  </header>
</template>

<style scoped>
.header {
  height: 60px;
  background-color: #ffffff;
  border-bottom: 1px solid #dee2e6;
  /* Cố định ở trên đầu nhưng chừa chỗ cho Nav bên trái (250px) */
  position: fixed;
  top: 0;
  left: 250px;
  right: 0;
  z-index: 999;
}

.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}
</style>
