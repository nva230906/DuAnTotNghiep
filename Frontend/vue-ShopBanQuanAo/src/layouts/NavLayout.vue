<template>
  <nav class="sidebar">
    <div class="sidebar-brand">
      <h4 class="logo">Clothes</h4>
    </div>

    <ul class="nav flex-column">
      <li class="nav-item">
        <router-link class="nav-link" to="/khach-hang">Khách Hàng</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/nhan-vien">Nhân Viên</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/ca-lam-viec">Ca Làm việc</router-link>
      </li>
    </ul>
  </nav>
</template>

<style scoped>
.sidebar {
  width: 250px;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  background-color: #1e293b;
  z-index: 1000;
  transition: all 0.3s;
}

.sidebar-brand {
  width: 100%;
  overflow: hidden; /* Ngăn chữ đâm ra ngoài Sidebar nếu quá dài */
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo {
  color: #3b82f6;
  padding: 25px 10px; /* Giảm padding hai bên để chữ có chỗ thở */
  font-family: 'Inter', sans-serif;
  font-weight: 800;
  letter-spacing: 1px;

  /* Các dòng quan trọng để sửa lỗi của bạn */
  font-size: 1.4rem; /* Hạ xuống 1.4 hoặc 1.3rem cho đến khi vừa mắt */
  white-space: nowrap; /* Ép 1 hàng */
  display: block;
  text-align: center; /* Căn giữa chữ cho chuyên nghiệp */
  width: 100%; /* Đảm bảo chiếm hết chiều ngang sidebar */
}

.nav-link {
  color: #94a3b8;
  padding: 12px 20px;
  margin: 4px 12px;
  border-radius: 8px;
  text-decoration: none;
  display: block;
  font-weight: 500;
}

.nav-link:hover {
  color: #ffffff;
  background-color: #334155;
}

.router-link-exact-active {
  background-color: #3b82f6 !important;
  color: #ffffff !important;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}
</style>
