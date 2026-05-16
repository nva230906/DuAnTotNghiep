<template>
  <div class="employee-container">
    <div class="search-bar">
      <div class="search-wrapper">
        <i class="fas fa-search search-icon"></i>
        <input
          type="text"
          v-model="keyword"
          placeholder="Tìm kiếm mã hoặc tên nhân viên..."
          @keyup.enter="searchNhanVien"
        />
        <button @click="searchNhanVien" class="btn-search-submit">Tìm kiếm</button>
      </div>
      <div class="action-group">
        <button @click="resetSearch" class="btn-reset" title="Làm mới dữ liệu">
          <i class="fas fa-sync-alt"></i> Làm mới
        </button>

        <button @click="handleExportExecl" class="btn-export" title="Xuất file Excel">
          <i class="fas fa-file-excel"></i> Xuất Excel
        </button>
        <button class="btn-add" @click="$router.push('/nhan-vien/add')">
          <i class="fas fa-plus"></i> Thêm nhân viên
        </button>
      </div>
    </div>

    <div class="table-card">
      <div class="table-header-title">
        <h2>Danh Sách Nhân Viên</h2>
      </div>

      <div class="table-responsive">
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Ảnh</th>
              <!-- <th>Mã NV</th> -->
              <th>Tên nhân viên</th>
              <th>Vai trò</th>
              <th>Địa chỉ</th>
              <th>Ngày Sinh</th>
              <th>Giới tính</th>
              <th>Trạng thái</th>
              <th class="text-center">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in listNhanVien" :key="item.id">
              <td>{{ pageNo * pageSize + index + 1 }}</td>
              <td>
                <div class="avatar-wrapper" v-if="item.anh">
                  <img
                    :src="`http://localhost:8080/api/shop-ban-quan-ao/nhan-vien/uploads/${item.anh}`"
                    class="avatar-img"
                  />
                </div>
                <span v-else class="no-avatar">Chưa có ảnh</span>
              </td>
              <!-- <td class="font-bold text-nowrap">{{ item.maNhanVien }}</td> -->
              <td class="font-medium text-nowrap">{{ item.tenNhanVien }}</td>
              <td class="text-nowrap">
                <span class="role-badge">{{ item.tenVaiTro }}</span>
              </td>
              <td class="address-cell">{{ item.diaChi }}</td>
              <td class="text-nowrap">{{ item.ngaySinh }}</td>
              <td class="text-nowrap">
                <span :class="['gender-tag', item.gioiTinh ? 'male' : 'female']">
                  {{ item.gioiTinh ? 'Nam' : 'Nữ' }}
                </span>
              </td>
              <td class="text-nowrap">
                <span :class="['status-badge', item.trangThai ? 'active' : 'inactive']">
                  {{ item.trangThai ? 'Đang làm' : 'Nghỉ việc' }}
                </span>
              </td>
              <td class="text-nowrap">
                <div class="action-buttons">
                  <button class="btn-icon edit" @click="detailNhanVien(item.id)" title="Sửa">
                    <i class="fas fa-edit"></i> Sửa
                  </button>
                  <button class="btn-icon delete" @click="deleteNhanVien(item)" title="Xoá">
                    <i class="fas fa-trash"></i> Xoá
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="listNhanVien.length === 0">
              <td colspan="10" class="text-center no-data">Không có dữ liệu nhân viên</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="pagination">
        <div class="pagination-info">
          Hiển thị
          <select v-model="pageSize" @change="handleSizeChange" class="select-page">
            <option :value="5">5</option>
            <option :value="10">10</option>
            <option :value="20">20</option>
          </select>
          nhân viên / trang
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
            :disabled="listNhanVien.length < pageSize"
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
import {
  deleteNhanVienService,
  exportNhanVienExcelService,
  phanTrangNhanVienService,
  searchNhanVienService,
} from '@/service/NhanVienService'
import { onMounted, reactive, ref } from 'vue'
import { useToast } from 'vue-toastification'
import Swal from 'sweetalert2'
import router from '@/router'
// Phân trang
const pageNo = ref(0)
const pageSize = ref(5)
const listNhanVien = ref([])
const toast = useToast()

const phanTrangNhanVien = async () => {
  try {
    listNhanVien.value = await phanTrangNhanVienService(pageNo.value, pageSize.value)
  } catch (error) {
    console.error('Lỗi load dữ liệu', error)
  }
}

const changePage = (step) => {
  pageNo.value += step
  phanTrangNhanVien()
}

const handleSizeChange = () => {
  pageNo.value = 0
  phanTrangNhanVien()
}

// Xoá
const deleteNhanVien = async (item) => {
  const result = await Swal.fire({
    title: 'Xác nhận xóa?',
    text: `Bạn có chắc chắn muốn xóa nhân viên ${item.tenNhanVien}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Đồng ý xóa',
    cancelButtonText: 'Hủy',
  })

  if (result.isConfirmed) {
    try {
      await deleteNhanVienService(item.id)
      toast.success('Xóa nhân viên thành công!')
      await phanTrangNhanVien()
    } catch (error) {
      toast.error('Có lỗi xảy ra khi xóa!')
    }
  }
}

// Tìm kiếm
const keyword = ref('')
const searchNhanVien = async () => {
  try {
    listNhanVien.value = await searchNhanVienService(keyword.value)
    toast.success('Tìm kiếm thành công!')
  } catch (error) {
    toast.error('Có lỗi xảy ra khi tìm kiếm!')
  }
}

const resetSearch = () => {
  keyword.value = ''
  pageNo.value = 0
  phanTrangNhanVien()
}

// Excel
const handleExportExecl = async () => {
  const result = await Swal.fire({
    title: 'Xác nhận xuất danh sách?',
    text: 'Bạn có chắc chắn muốn xuất danh sách nhân viên ra file Excel không?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Đồng ý',
    cancelButtonText: 'Hủy',
  })
  if (result.isConfirmed) {
    try {
      await exportNhanVienExcelService(keyword.value || '', '', '')
      toast.success('Xuất danh sách nhân viên thành công!')
    } catch (error) {
      toast.error('Có lỗi xảy ra khi xuất danh sách!')
    }
  }
}

// Detail
const detailNhanVien = (id) => {
  router.push(`/nhan-vien/edit/${id}`)
}

onMounted(phanTrangNhanVien)
</script>

<style scoped>
/* Reset font & container */
.employee-container {
  padding: 24px;
  background-color: #f8fafc;
  min-height: 100vh;
  font-family: 'Inter', sans-serif;
}

/* FIX LỖI NHẢY DÒNG */
.text-nowrap {
  white-space: nowrap !important;
}

/* Địa chỉ cho phép ngắt dòng để không đẩy bảng quá rộng */
.address-cell {
  min-width: 250px;
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
  text-transform: capitalize;
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
}

td {
  padding: 14px 20px;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
  font-size: 0.9rem;
}

.font-bold {
  font-weight: 700;
  color: #1e293b;
}
.font-medium {
  font-weight: 500;
  color: #334155;
}

/* Avatar */
.avatar-wrapper {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  overflow: hidden;
  border: 1px solid #e2e8f0;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Tags & Badges */
.role-badge {
  background: #f1f5f9;
  color: #475569;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 0.8rem;
}

.gender-tag,
.status-badge {
  padding: 4px 12px;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
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
  transition: all 0.2s ease;
  font-size: 0.8rem;
  font-weight: 600;
}

.btn-icon.edit {
  background-color: #f0f7ff;
  color: #2563eb;
}
.btn-icon.edit:hover {
  background-color: #2563eb;
  color: #ffffff;
}

.btn-icon.delete {
  background-color: #fff1f2;
  color: #ef4444;
}
.btn-icon.delete:hover {
  background-color: #ef4444;
  color: #ffffff;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: #ffffff;
}

.select-page {
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
  margin: 0 5px;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 12px;
}

.btn-page {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: white;
  cursor: pointer;
  font-size: 0.85rem;
  transition: 0.2s;
}

.btn-page:not(:disabled):hover {
  border-color: #2563eb;
  color: #2563eb;
}
.btn-page:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-indicator span {
  font-weight: 700;
  color: #2563eb;
}
/* --- Cập nhật phần ảnh to hơn --- */

/* Căn giữa nội dung trong ô chứa Ảnh (ô thứ 2) */
td:nth-child(2) {
  text-align: center;
  vertical-align: middle;
  padding-left: 10px; /* Giảm padding một chút để dành không gian cho ảnh to */
  padding-right: 10px;
}

/* Khung bao quanh ảnh - Tăng kích thước */
.avatar-wrapper {
  width: 80px; /* Đã tăng lên 80px, bạn có thể chỉnh thành 100px nếu muốn to hơn nữa */
  height: 80px; /* Phải bằng width để tạo hình tròn */
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #e2e8f0; /* Tăng độ dày viền một chút cho cân đối */
  display: inline-block;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15); /* Thêm đổ bóng rõ hơn */
  transition: transform 0.2s ease-in-out; /* Thêm hiệu ứng rẽ nhẹ khi hover */
}

/* Hiệu ứng khi di chuột vào ảnh */
.avatar-wrapper:hover {
  transform: scale(1.05); /* Phóng to nhẹ một chút */
  border-color: #2563eb; /* Đổi màu viền sang màu chủ đạo */
}

/* Ảnh bên trong - Giữ nguyên object-fit để không bị méo */
.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

/* Style cho dòng "Chưa có ảnh" */
.no-avatar {
  font-size: 0.8rem;
  color: #94a3b8;
  font-style: italic;
  display: inline-block;
  width: 80px; /* Bằng chiều rộng ảnh để căn chỉnh đều */
  text-align: center;
}
/* Đảm bảo nội dung bên trong ô TD luôn nằm ngang */
td.text-nowrap {
  white-space: nowrap !important;
}

/* Tinh chỉnh Tag Giới tính */
.gender-tag {
  display: inline-flex; /* Chuyển sang flex để kiểm soát nội dung bên trong */
  align-items: center;
  justify-content: center;
  padding: 4px 12px;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  min-width: 60px; /* Đảm bảo độ dài đồng nhất */
  white-space: nowrap; /* Ép không xuống dòng */
}

/* Tinh chỉnh Badge Trạng thái */
.status-badge {
  display: inline-flex; /* Chuyển sang flex */
  align-items: center;
  justify-content: center;
  padding: 4px 12px;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  min-width: 90px; /* Đang làm/Nghỉ việc có độ dài khác nhau nên đặt min-width */
  white-space: nowrap; /* Ép không xuống dòng */
}
th {
  background: #f8fafc;
  padding: 14px 20px;
  text-align: left;
  font-size: 0.75rem;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  /* Thêm dòng này để tiêu đề "GIỚI TÍNH" nằm ngang */
  white-space: nowrap;
}
/* Ép cột thứ 7 (Giới tính) và thứ 8 (Trạng thái) rộng ra một chút */
td:nth-child(7),
th:nth-child(7) {
  min-width: 100px; /* Đảm bảo đủ chỗ cho chữ "GIỚI TÍNH" */
  text-align: center; /* Căn giữa cho đẹp vì tag thường ngắn */
}

td:nth-child(8),
th:nth-child(8) {
  min-width: 120px; /* Cho cột Trạng thái */
  text-align: center;
}
</style>
