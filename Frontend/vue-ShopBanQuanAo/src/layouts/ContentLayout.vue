<template>
  <main class="main-content">
    <div class="content-container">
      <slot></slot>
    </div>
  </main>
</template>

<style scoped>
.main-content {
  margin-left: 250px;
  padding-top: 64px;
  min-height: 100vh;
  background-color: #f8fafc; /* Màu nền rất nhẹ, sang trọng */
}

.content-container {
  padding: 24px;
}

/* Style bảng cho hiện đại */
table {
  width: 100%;
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

th {
  background-color: #f8fafc;
  color: #475569;
  font-weight: 600;
  padding: 16px;
  border-bottom: 1px solid #e2e8f0;
}

td {
  padding: 14px 16px;
  color: #1e293b;
  border-bottom: 1px solid #f1f5f9;
}

/* Bo góc các ô input và button */
input,
select {
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  padding: 8px 12px;
}

button {
  border-radius: 6px;
  padding: 8px 16px;
  font-weight: 500;
}
</style>
