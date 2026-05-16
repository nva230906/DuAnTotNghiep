<template>
  <div class="employee-container">
    <!-- Search & Action Bar -->
    <div class="search-bar">
      <div class="search-wrapper">
        <i class="fas fa-search search-icon"></i>
        <input
          type="text"
          v-model="keyword"
          placeholder="Tìm kiếm mã hoặc tên khách hàng..."
          @keyup.enter="searchKhachHang"
        />
        <button @click="searchKhachHang" class="btn-search-submit">Tìm kiếm</button>
      </div>
      <div class="action-group">
        <button @click="resetSearch" class="btn-reset" title="Làm mới dữ liệu">
          <i class="fas fa-sync-alt"></i> Làm mới
        </button>

        <button @click="handleExportExecl" class="btn-export" title="Xuất file Excel">
          <i class="fas fa-file-excel"></i> Xuất Excel
        </button>
        <button class="btn-add" @click="$router.push('/khach-hang/add')">
          <i class="fas fa-plus"></i> Thêm khách hàng
        </button>
      </div>
    </div>

    <!-- Table Card -->
    <div class="table-card">
      <div class="table-header-title">
        <h2>Danh Sách Khách Hàng</h2>
      </div>

      <div class="table-responsive">
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Ảnh</th>
              <th>Họ và tên</th>
              <th>Email</th>
              <th>Tài khoản</th>
              <th>Số điện thoại</th>
              <th>Địa chỉ</th>
              <th>Giới tính</th>
              <th>Trạng thái</th>
              <th class="text-center">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in listKhachHang" :key="item.id">
              <td>{{ pageNo * pageSize + index + 1 }}</td>
              <td>
                <div class="avatar-wrapper" v-if="item.anh">
                  <img
                    :src="`http://localhost:8080/api/shop-ban-quan-ao/khach-hang/uploads/${item.anh}`"
                    class="avatar-img"
                  />
                </div>
                <span v-else class="no-avatar">Chưa có ảnh</span>
              </td>
              <td class="font-medium text-nowrap">{{ item.hoTen }}</td>
              <td class="text-nowrap">{{ item.email }}</td>
              <td class="text-nowrap">{{ item.tenTaiKhoan }}</td>
              <td class="text-nowrap">{{ item.soDienThoai }}</td>
              <td class="address-cell">{{ item.diaChi }}</td>
              <td class="text-nowrap">
                <span :class="['gender-tag', item.gioiTinh ? 'male' : 'female']">
                  {{ item.gioiTinh ? 'Nam' : 'Nữ' }}
                </span>
              </td>
              <td class="text-nowrap">
                <span :class="['status-badge', item.trangThai ? 'active' : 'inactive']">
                  {{ item.trangThai ? 'Hoạt động' : 'Ngừng hoạt động' }}
                </span>
              </td>
              <td class="text-nowrap">
                <div class="action-buttons">
                  <button class="btn-icon edit" @click="detailKhachHang(item.id)" title="Sửa">
                    <i class="fas fa-edit"></i> Sửa
                  </button>
                  <button class="btn-icon delete" @click="deleteKhachHang(item)" title="Xoá">
                    <i class="fas fa-trash"></i> Xoá
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="listKhachHang.length === 0">
              <td colspan="10" class="text-center no-data">Không có dữ liệu khách hàng</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div class="pagination">
        <div class="pagination-info">
          Hiển thị
          <select v-model="pageSize" @change="handleSizeChange" class="select-page">
            <option :value="5">5</option>
            <option :value="10">10</option>
            <option :value="20">20</option>
          </select>
          khách hàng / trang
        </div>

        <div class="pagination-controls">
          <button class="btn-page" :disabled="pageNo === 0" @click="changePage(-1)">
            <i class="fas fa-chevron-left"></i> Trước
          </button>

          <div class="page-indicator">
            Trang <span>{{ pageNo + 1 }}</span>
          </div>

          <button
            class="btn-page"
            :disabled="listKhachHang.length < pageSize"
            @click="changePage(1)"
          >
            Sau <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import { useToast } from 'vue-toastification'
import {
  deleteKhachHangService,
  exportKhachHangExcelService,
  phanTrangKhachHangService,
  searchKhachHangService,
} from '@/service/KhachHangService'

const router = useRouter()
const toast = useToast()

const listKhachHang = ref([])
const pageNo = ref(0)
const pageSize = ref(5)
const keyword = ref('')

const fetchKhachHang = async () => {
  try {
    if (keyword.value.trim()) {
      listKhachHang.value = await searchKhachHangService(
        keyword.value,
        pageNo.value,
        pageSize.value,
      )
    } else {
      listKhachHang.value = await phanTrangKhachHangService(pageNo.value, pageSize.value)
    }
  } catch (error) {
    console.error('Lỗi load dữ liệu', error)
  }
}

const changePage = (step) => {
  pageNo.value += step
  fetchKhachHang()
}

const handleSizeChange = () => {
  pageNo.value = 0
  fetchKhachHang()
}

const searchKhachHang = () => {
  pageNo.value = 0
  fetchKhachHang()
}

const resetSearch = () => {
  keyword.value = ''
  pageNo.value = 0
  fetchKhachHang()
}

const deleteKhachHang = async (item) => {
  const result = await Swal.fire({
    title: 'Xác nhận xóa?',
    text: `Bạn có chắc chắn muốn xóa khách hàng ${item.hoTen}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Đồng ý xóa',
    cancelButtonText: 'Hủy',
  })

  if (result.isConfirmed) {
    try {
      await deleteKhachHangService(item.id)
      toast.success('Xóa khách hàng thành công!')
      fetchKhachHang()
    } catch (error) {
      toast.error('Có lỗi xảy ra khi xóa!')
    }
  }
}

const handleExportExecl = async () => {
  const result = await Swal.fire({
    title: 'Xác nhận xuất file?',
    text: 'Hệ thống sẽ xuất danh sách khách hàng ra file Excel.',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Đồng ý',
    cancelButtonText: 'Hủy',
  })

  if (result.isConfirmed) {
    try {
      await exportKhachHangExcelService(keyword.value || '', '', '')
      toast.success('Xuất file Excel thành công!')
    } catch (error) {
      toast.error('Lỗi khi xuất file!')
    }
  }
}

const detailKhachHang = (id) => {
  router.push(`/khach-hang/edit/${id}`)
}

onMounted(fetchKhachHang)
</script>
<style scoped>
.employee-container {
  padding: 24px;
  background-color: #f8fafc;
  min-height: 100vh;
  font-family: 'Inter', sans-serif;
}

.text-nowrap {
  white-space: nowrap !important;
}

.address-cell {
  min-width: 200px;
  white-space: normal;
  line-height: 1.4;
  color: #64748b;
  font-size: 0.9rem;
}

/* Search Bar */
.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-bottom: 25px;
}

.search-wrapper {
  display: flex;
  align-items: center;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 50px;
  padding: 6px 6px 6px 18px;
  flex: 1;
  max-width: 500px;
}

.search-wrapper input {
  border: none;
  outline: none;
  flex: 1;
  font-size: 0.95rem;
  background: transparent;
}

.btn-search-submit {
  background: #2563eb;
  color: white;
  border: none;
  padding: 8px 22px;
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
}

.action-group {
  display: flex;
  gap: 12px;
}

.btn-reset,
.btn-export,
.btn-add {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  font-size: 0.85rem;
}

.btn-reset {
  background: white;
  color: #64748b;
  border: 1px solid #e2e8f0;
}
.btn-export {
  background: #107c10;
  color: white;
}
.btn-add {
  background: #1e293b;
  color: white;
}

/* Table Card */
.table-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  border: 1px solid #f1f5f9;
  overflow: hidden;
}

.table-header-title {
  padding: 20px 24px;
  border-bottom: 1px solid #f1f5f9;
}

.table-header-title h2 {
  margin: 0;
  font-size: 1.25rem;
  color: #1e293b;
}

.table-responsive {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  background: #f8fafc;
  padding: 14px 20px;
  text-align: left;
  font-size: 0.75rem;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  white-space: nowrap;
}

td {
  padding: 14px 20px;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
  font-size: 0.9rem;
}

.font-medium {
  font-weight: 500;
  color: #334155;
}

/* Avatar */
.avatar-wrapper {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid #e2e8f0;
  display: inline-block;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Tags */
.gender-tag,
.status-badge {
  padding: 4px 12px;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  display: inline-flex;
  justify-content: center;
  min-width: 80px;
}

.gender-tag.male {
  background: #e0f2fe;
  color: #0369a1;
}
.gender-tag.female {
  background: #fce7f3;
  color: #be185d;
}
.status-badge.active {
  background: #dcfce7;
  color: #15803d;
}
.status-badge.inactive {
  background: #fee2e2;
  color: #b91c1c;
}

/* Actions */
.action-buttons {
  display: flex;
  gap: 8px;
}

.btn-icon {
  padding: 6px 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: 600;
}

.btn-icon.edit {
  background-color: #f0f7ff;
  color: #2563eb;
}
.btn-icon.delete {
  background-color: #fff1f2;
  color: #ef4444;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
}

.select-page {
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 12px;
}

.btn-page {
  padding: 8px 16px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: white;
  cursor: pointer;
}

.page-indicator span {
  font-weight: 700;
  color: #2563eb;
}
/* Khống chế độ rộng cột Địa chỉ */
.address-cell {
  max-width: 150px; /* Hoặc 200px tùy bạn */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Khống chế độ rộng cột Email */
td:nth-child(4) {
  /* Cột Email */
  max-width: 120px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
th,
td {
  padding: 12px 8px; /* Giảm từ 20px xuống còn 8px ở hai bên */
  font-size: 0.85rem; /* Giảm nhẹ cỡ chữ nếu cần */
}
.table-responsive {
  overflow-x: hidden; /* Ẩn thanh cuộn ngang */
  width: 100%;
}

table {
  width: 100%;
  table-layout: auto; /* Để trình duyệt tự tính toán tối ưu nhất */
}
@media (max-width: 1200px) {
  /* Ẩn cột Tài khoản và Địa chỉ khi màn hình nhỏ để tránh hiện thanh cuộn */
  th:nth-child(5), td:nth-child(5), /* Cột Tài khoản */
  th:nth-child(7), td:nth-child(7)  /* Cột Địa chỉ */ {
    display: none;
  }
}
</style>
