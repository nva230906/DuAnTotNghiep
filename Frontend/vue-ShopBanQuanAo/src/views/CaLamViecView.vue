<template>
  <table>
    <thead>
      <tr>
        <th>#</th>
        <th>Mã Ca</th>
        <th>Tên Ca</th>
        <th>Giờ Bắt Đầu</th>
        <th>Giờ Kết Thúc</th>
        <th>Trạng Thái</th>
        <th>Hành Động</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(item, index) in listCaLamViec" :key="item.id">
        <td>{{ pageNo * pageSize + index + 1 }}</td>
        <td>item.maCa</td>
        <td>item.tenCa</td>
        <td>item.gioBatDau</td>
        <td>item.gioKetThuc</td>
        <td>item.trangThai</td>
      </tr>
    </tbody>
  </table>
  <div class="pagination">
    <div class="pagination-info">
      Hiển thị
      <select v-model="pageSize" @change="handleSizeChange" class="select-page">
        <option :value="5">5</option>
        <option :value="10">10</option>
        <option :value="20">20</option>
      </select>
      Ca làm việc / trang
    </div>

    <div class="pagination-controls">
      <button class="btn-page" :disabled="pageNo === 0" @click="changePage(-1)">
        <i class="fas fa-chevron-left"></i> Trước
      </button>

      <div class="page-indicator">
        Trang <span>{{ pageNo + 1 }}</span>
      </div>

      <button class="btn-page" :disabled="listCaLamViec.length < pageSize" @click="changePage(1)">
        Sau <i class="fas fa-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { phanTrangCaLamViecService } from '@/service/CaLamViecService'
const pageNo = ref(0)
const pageSize = ref(5)
const listCaLamViec = ref([])
const toast = useToast()

const handleFilterChange = () => {
  currentPage.value = 0
  fetchEmployees()
}

const phanTrangCaLamViec = async () => {
  try {
    listCaLamViec.value = await phanTrangCaLamViecService(pageNo.value, pageSize.value)
  } catch (error) {
    console.error('Lỗi load dữ liệu', error)
  }
}

const changePage = (step) => {
  pageNo.value += step
  phanTrangCaLamViec()
}

const handleSizeChange = () => {
  pageNo.value = 0
  phanTrangCaLamViec()
}
onMounted(phanTrangCaLamViec)
</script>

<style lang="scss" scoped></style>
