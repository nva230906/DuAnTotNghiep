<template>
  <div class="employee-container">
    <div class="form-card">
      <div class="card-header">
        <h3 class="title">| {{ isEdit ? 'CẬP NHẬT NHÂN VIÊN' : 'THÔNG TIN NHÂN VIÊN' }}</h3>
        <span class="id-badge" v-if="newNhanVien.maNhanVien">ID: {{ newNhanVien.maNhanVien }}</span>
      </div>

      <div class="card-body">
        <div class="form-sidebar">
          <div class="avatar-section">
            <div class="avatar-circle">
              <img
                v-if="previewUrl || newNhanVien.anh"
                :src="previewUrl || getImageUrl(newNhanVien.anh)"
                @error="handleImageError"
              />
              <div v-else class="upload-placeholder">
                <i class="fas fa-camera"></i>
              </div>
              <input
                type="file"
                id="avatarUpload"
                hidden
                @change="handleFileChange"
                accept="image/*"
              />
              <label for="avatarUpload" class="upload-label"></label>
            </div>
            <p class="upload-hint">Định dạng JPG, PNG dưới 2MB</p>
          </div>

          <div class="input-group">
            <label>Họ và tên <span class="required">*</span></label>
            <input type="text" v-model="newNhanVien.tenNhanVien" placeholder="Nguyễn Văn A" />
          </div>

          <div class="input-group">
            <label>Ngày sinh <span class="required">*</span></label>
            <input type="date" v-model="newNhanVien.ngaySinh" />
          </div>

          <div class="input-group">
            <label>Giới tính <span class="required">*</span></label>
            <div class="radio-group">
              <label class="radio-label">
                <input type="radio" :value="true" v-model="newNhanVien.gioiTinh" /> Nam
              </label>
              <label class="radio-label">
                <input type="radio" :value="false" v-model="newNhanVien.gioiTinh" /> Nữ
              </label>
            </div>
          </div>
        </div>

        <div class="form-main">
          <div class="form-section">
            <h4 class="section-title">LIÊN HỆ & TÀI KHOẢN</h4>
            <div class="grid-row">
              <div class="input-group">
                <label>Số điện thoại <span class="required">*</span></label>
                <input type="text" v-model="newNhanVien.soDienThoai" />
              </div>
              <div class="input-group">
                <label>Email <span class="required">*</span></label>
                <input type="email" v-model="newNhanVien.email" />
              </div>
            </div>
            <div class="grid-row">
              <div class="input-group">
                <label>CCCD <span class="required">*</span></label>
                <input type="text" v-model="newNhanVien.canCuocCongDan" />
              </div>
              <div class="input-group">
                <label>Mật khẩu <span class="required">*</span></label>
                <input type="password" v-model="newNhanVien.matKhau" />
              </div>
            </div>
          </div>

          <div class="form-section">
            <h4 class="section-title">PHÂN QUYỀN & TRẠNG THÁI</h4>
            <div class="grid-row">
              <div class="input-group">
                <label>Quyền hạn <span class="required">*</span></label>
                <select v-model="newNhanVien.idVaiTro">
                  <option v-for="role in listVaiTro" :key="role.id" :value="role.id">
                    {{ role.tenVaiTro }}
                  </option>
                </select>
              </div>
              <div class="input-group">
                <label>Trạng thái</label>
                <div class="status-toggle">
                  <button
                    :class="['btn-status', { active: newNhanVien.trangThai === 1 }]"
                    @click="newNhanVien.trangThai = 1"
                  >
                    <span class="dot"></span> Đang làm việc
                  </button>
                  <button
                    :class="['btn-status', { active: newNhanVien.trangThai === 0 }]"
                    @click="newNhanVien.trangThai = 0"
                  >
                    <span class="dot"></span> Nghỉ việc
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div class="form-section no-border">
            <h4 class="section-title">ĐỊA CHỈ</h4>
            <div class="grid-row three-cols">
              <select v-model="selectedTinh" @change="handleProvinceChange">
                <option value="">Tỉnh/Thành</option>
                <option v-for="p in provinces" :key="p.code" :value="p.name">{{ p.name }}</option>
              </select>
              <select v-model="selectedHuyen" @change="handleDistrictChange">
                <option value="">Quận/Huyện</option>
                <option v-for="d in districts" :key="d.code" :value="d.name">{{ d.name }}</option>
              </select>
              <select v-model="selectedXa">
                <option value="">Xã/Phường</option>
                <option v-for="w in wards" :key="w.code" :value="w.name">{{ w.name }}</option>
              </select>
            </div>
            <div class="input-group full-width mt-15">
              <label>Địa chỉ cụ thể <span class="required">*</span></label>
              <input type="text" v-model="diaChiChiTiet" placeholder="Số nhà, ngõ..." />
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer">
        <span class="footer-note"
          >Các trường có dấu <span class="required">*</span> là bắt buộc</span
        >
        <div class="button-group">
          <button class="btn-cancel" @click="$router.back()">Hủy</button>
          <button class="btn-submit" @click="saveEmployee">
            {{ isEdit ? 'Lưu cập nhật' : 'Thêm nhân viên' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import axios from 'axios'
import {
  addNhanVienService,
  detailNhanVienService,
  updateNhanVienService,
} from '@/service/NhanVienService'
import { getAllVaiTroService } from '@/service/VaiTroService'

const route = useRoute()
const router = useRouter()
const toast = useToast()

const SEPARATOR = ', '
const isEdit = ref(false)
const editId = ref(null)
const showPassword = ref(false)

const listVaiTro = ref([])
const provinces = ref([])
const districts = ref([])
const wards = ref([])

const selectedTinh = ref('')
const selectedHuyen = ref('')
const selectedXa = ref('')
const diaChiChiTiet = ref('')

const newNhanVien = ref({
  anh: '',
  maNhanVien: '',
  tenNhanVien: '',
  idVaiTro: null,
  gioiTinh: true,
  ngaySinh: '',
  email: '',
  soDienThoai: '',
  canCuocCongDan: '',
  matKhau: '',
  diaChi: '',
  trangThai: 1,
})

const previewUrl = ref(null)
const fileSelected = ref(null)

// --- XỬ LÝ FILE ---
const handleFile = (event) => {
  const file = event.target.files[0]
  if (file) {
    if (file.size > 2 * 1024 * 1024) {
      toast.warning('Ảnh không được vượt quá 2MB')
      return
    }
    fileSelected.value = file
    previewUrl.value = URL.createObjectURL(file)
  }
}

// --- API TỈNH THÀNH ---
const fetchProvinces = async () => {
  try {
    const res = await axios.get('https://provinces.open-api.vn/api/p/')
    provinces.value = res.data
  } catch (e) {
    console.error('Lỗi tải tỉnh thành:', e)
  }
}

const onProvinceChange = async (isManual = true) => {
  if (isManual) {
    selectedHuyen.value = ''
    selectedXa.value = ''
    districts.value = []
    wards.value = []
  }
  const p = provinces.value.find((i) => i.name === selectedTinh.value)
  if (p) {
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${p.code}?depth=2`)
    districts.value = res.data.districts
  }
}

const onDistrictChange = async (isManual = true) => {
  if (isManual) {
    selectedXa.value = ''
    wards.value = []
  }
  const d = districts.value.find((i) => i.name === selectedHuyen.value)
  if (d) {
    const res = await axios.get(`https://provinces.open-api.vn/api/d/${d.code}?depth=2`)
    wards.value = res.data.wards
  }
}

// --- LƯU DỮ LIỆU ---
const handleSave = async () => {
  // Kiểm tra validate cơ bản
  if (!newNhanVien.value.tenNhanVien || !newNhanVien.value.email || !diaChiChiTiet.value) {
    toast.warning('Vui lòng điền đầy đủ các trường bắt buộc')
    return
  }

  // Ghép địa chỉ
  const parts = [
    diaChiChiTiet.value,
    selectedXa.value,
    selectedHuyen.value,
    selectedTinh.value,
  ].filter((p) => p)
  newNhanVien.value.diaChi = parts.join(SEPARATOR)

  try {
    if (isEdit.value) {
      await updateNhanVienService(editId.value, newNhanVien.value, fileSelected.value)
      toast.success('Cập nhật nhân viên thành công!')
    } else {
      await addNhanVienService(newNhanVien.value, fileSelected.value)
      toast.success('Thêm mới nhân viên thành công!')
    }
    router.push('/nhan-vien')
  } catch (e) {
    console.error(e)
    toast.error(e.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại!')
  }
}

onMounted(async () => {
  // Load danh mục trước
  const roles = await getAllVaiTroService()
  listVaiTro.value = roles
  await fetchProvinces()

  // Nếu là chế độ Edit
  if (route.params.id) {
    isEdit.value = true
    editId.value = route.params.id
    try {
      const data = await detailNhanVienService(route.params.id)
      newNhanVien.value = data

      // Xử lý tách địa chỉ
      if (data.diaChi) {
        const parts = data.diaChi.split(SEPARATOR).map((p) => p.trim())
        // Giả sử: [Số nhà, Xã, Huyện, Tỉnh]
        if (parts.length >= 4) {
          selectedTinh.value = parts.pop()
          selectedHuyen.value = parts.pop()
          selectedXa.value = parts.pop()
          diaChiChiTiet.value = parts.join(SEPARATOR)

          // Re-load dropdowns
          await onProvinceChange(false)
          await onDistrictChange(false)
        } else {
          diaChiChiTiet.value = data.diaChi // Nếu format lạ thì đổ hết vào ô chi tiết
        }
      }
    } catch (error) {
      toast.error('Không thể tải thông tin nhân viên')
    }
  }
})
</script>
<style scoped>
/* Toàn bộ container sử dụng border-box để tránh input tràn khung */
* {
  box-sizing: border-box;
}

.employee-container {
  background-color: #f8fafc;
  padding: 40px 20px;
  display: flex;
  justify-content: center;
  min-height: 100vh;
}

.form-card {
  background: white;
  width: 100%;
  max-width: 1000px; /* Giới hạn chiều rộng thẻ trắng */
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

/* Header */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.title {
  color: #1e3a8a;
  font-weight: 700;
  font-size: 20px;
  margin: 0;
}

.id-badge {
  background: #f1f5f9;
  color: #64748b;
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
}

/* Layout */
.card-body {
  display: flex;
  gap: 40px;
}

.form-sidebar {
  width: 240px;
  flex-shrink: 0;
}

.form-main {
  flex: 1;
  min-width: 0; /* Quan trọng để flex child không bị tràn */
}

/* Avatar */
.avatar-section {
  text-align: center;
  margin-bottom: 25px;
}

.avatar-circle {
  width: 150px;
  height: 150px;
  border: 1px dashed #cbd5e1;
  border-radius: 50%;
  margin: 0 auto 10px;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fcfcfc;
}

.avatar-circle img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-label {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.upload-hint {
  font-size: 11px;
  color: #94a3b8;
}

/* Form Section */
.section-title {
  color: #b45309;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 20px;
  letter-spacing: 0.5px;
}

.form-section {
  border-bottom: 1px solid #f1f5f9;
  padding-bottom: 25px;
  margin-bottom: 25px;
}

.no-border {
  border: none;
}

/* Grid Layout fix overflow */
.grid-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.three-cols {
  grid-template-columns: 1fr 1fr 1fr;
}

/* Input Styles */
.input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-group label {
  font-size: 13px;
  font-weight: 600;
  color: #334155;
}

.required {
  color: #ef4444;
}

input,
select {
  width: 100%; /* Sẽ không tràn nhờ box-sizing: border-box */
  height: 42px;
  padding: 0 14px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s;
}

input:focus,
select:focus {
  border-color: #3b82f6;
  outline: none;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* Status Buttons */
.status-toggle {
  display: flex;
  gap: 10px;
}

.btn-status {
  flex: 1;
  height: 42px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #64748b;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-status .dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #cbd5e1;
}

.btn-status.active {
  background: #f0fdf4;
  border-color: #22c55e;
  color: #166534;
}

.btn-status.active .dot {
  background: #22c55e;
}

/* Footer */
.card-footer {
  margin-top: 20px;
  padding-top: 25px;
  border-top: 1px solid #f1f5f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-note {
  font-size: 12px;
  color: #94a3b8;
}

.button-group {
  display: flex;
  gap: 12px;
}

.btn-cancel {
  padding: 10px 25px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #64748b;
  font-weight: 600;
  cursor: pointer;
}

.btn-submit {
  padding: 10px 30px;
  background: #1e3a8a;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
}

.mt-15 {
  margin-top: 15px;
}
</style>
