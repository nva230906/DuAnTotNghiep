<template>
  <div class="employee-container">
    <div class="form-card">
      <div class="card-header">
        <h3 class="title">| {{ isEdit ? 'CẬP NHẬT KHÁCH HÀNG' : 'THÔNG TIN KHÁCH HÀNG' }}</h3>
        <span class="id-badge" v-if="isEdit && newKhachHang.maKhachHang">
          ID: {{ newKhachHang.maKhachHang }}
        </span>
      </div>

      <div class="card-body">
        <!-- SIDEBAR (ẢNH & THÔNG TIN CƠ BẢN) -->
        <div class="form-sidebar">
          <div class="avatar-section">
            <div class="avatar-wrapper" @click="$refs.fileInputRef.click()">
              <img
                v-if="previewUrl || newKhachHang.anh"
                :src="
                  previewUrl ||
                  `http://localhost:8080/api/shop-ban-quan-ao/khach-hang/uploads/${newKhachHang.anh}`
                "
                class="avatar-img"
              />
              <div v-else class="placeholder">
                <i class="fas fa-camera"></i>
                <span>Tải ảnh lên</span>
              </div>
              <div class="overlay" v-if="previewUrl || newKhachHang.anh">
                <span>Thay đổi ảnh</span>
              </div>
            </div>
            <p class="avatar-hint">JPG, PNG • Tối đa 2MB</p>
            <input type="file" hidden ref="fileInputRef" accept="image/*" @change="handleFile" />
          </div>

          <div class="input-group">
            <label>Họ và tên <span class="required">*</span></label>
            <input
              type="text"
              v-model="newKhachHang.hoTen"
              :class="{ 'input-error': errors.hoTen }"
              placeholder="Nguyễn Văn A"
            />
            <span class="error-text">{{ errors.hoTen }}</span>
          </div>

          <div class="input-group">
            <label>Ngày sinh <span class="required">*</span></label>
            <input
              type="date"
              v-model="newKhachHang.ngaySinh"
              :class="{ 'input-error': errors.ngaySinh }"
            />
            <span class="error-text">{{ errors.ngaySinh }}</span>
          </div>

          <div class="input-group">
            <label>Giới tính <span class="required">*</span></label>
            <div class="radio-group">
              <label class="radio-label">
                <input type="radio" :value="true" v-model="newKhachHang.gioiTinh" /> Nam
              </label>
              <label class="radio-label">
                <input type="radio" :value="false" v-model="newKhachHang.gioiTinh" /> Nữ
              </label>
            </div>
          </div>
        </div>

        <!-- MAIN FORM -->
        <div class="form-main">
          <div class="form-section">
            <h4 class="section-title">LIÊN HỆ & TÀI KHOẢN</h4>

            <div class="input-group" style="margin-bottom: 15px">
              <label>Tên tài khoản (Username) <span class="required">*</span></label>
              <input
                type="text"
                v-model="newKhachHang.tenTaiKhoan"
                :disabled="isEdit"
                :class="{ 'input-error': errors.tenTaiKhoan, 'input-disabled': isEdit }"
                placeholder="nhập_ten_tai_khoan_viet_lien"
              />
              <span class="error-text">{{ errors.tenTaiKhoan }}</span>
            </div>

            <div class="grid-row">
              <div class="input-group">
                <label>Số điện thoại <span class="required">*</span></label>
                <input
                  type="text"
                  v-model="newKhachHang.soDienThoai"
                  :class="{ 'input-error': errors.soDienThoai }"
                  placeholder="09xxx"
                />
                <span class="error-text">{{ errors.soDienThoai }}</span>
              </div>
              <div class="input-group">
                <label>Email <span class="required">*</span></label>
                <input
                  type="email"
                  v-model="newKhachHang.email"
                  :class="{ 'input-error': errors.email }"
                  placeholder="example@gmail.com"
                />
                <span class="error-text">{{ errors.email }}</span>
              </div>
            </div>
          </div>

          <div class="form-section">
            <h4 class="section-title">TRẠNG THÁI TÀI KHOẢN</h4>
            <div class="grid-row">
              <div class="input-group">
                <label>Trạng thái</label>
                <div class="status-toggle">
                  <button
                    type="button"
                    :class="['btn-status', { active: newKhachHang.trangThai === 1 }]"
                    @click="newKhachHang.trangThai = 1"
                  >
                    <span class="dot"></span> Hoạt động
                  </button>
                  <button
                    type="button"
                    :class="['btn-status', { active: newKhachHang.trangThai === 0 }]"
                    @click="newKhachHang.trangThai = 0"
                  >
                    <span class="dot"></span> Khóa
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div class="form-section no-border">
            <h4 class="section-title">ĐỊA CHỈ GIAO HÀNG</h4>
            <div class="grid-row three-cols">
              <div class="input-group">
                <label>Tỉnh/Thành phố <span class="required">*</span></label>
                <select
                  v-model="newKhachHang.thanhPho"
                  @change="onProvinceChange(true)"
                  :class="{ 'input-error': errors.thanhPho }"
                >
                  <option value="">-- Chọn Tỉnh/Thành --</option>
                  <option v-for="p in provinces" :key="p.code" :value="p.name">
                    {{ p.name }}
                  </option>
                </select>
                <span class="error-text">{{ errors.thanhPho }}</span>
              </div>

              <div class="input-group">
                <label>Quận/Huyện <span class="required">*</span></label>
                <select
                  v-model="newKhachHang.quan"
                  @change="onDistrictChange(true)"
                  :disabled="!districts.length"
                  :class="{ 'input-error': errors.quan }"
                >
                  <option value="">-- Chọn Quận/Huyện --</option>
                  <option v-for="d in districts" :key="d.code" :value="d.name">
                    {{ d.name }}
                  </option>
                </select>
                <span class="error-text">{{ errors.quan }}</span>
              </div>

              <div class="input-group">
                <label>Phường/Xã <span class="required">*</span></label>
                <select
                  v-model="newKhachHang.phuong"
                  :disabled="!wards.length"
                  :class="{ 'input-error': errors.phuong }"
                >
                  <option value="">-- Chọn Phường/Xã --</option>
                  <option v-for="w in wards" :key="w.code" :value="w.name">
                    {{ w.name }}
                  </option>
                </select>
                <span class="error-text">{{ errors.phuong }}</span>
              </div>
            </div>

            <div class="input-group" style="margin-top: 15px">
              <label>Số nhà/Đường cụ thể <span class="required">*</span></label>
              <input
                type="text"
                v-model="newKhachHang.diaChiCuThe"
                placeholder="Ví dụ: Số 10, ngõ 2, đường Nguyễn Trãi"
                :class="{ 'input-error': errors.diaChiCuThe }"
              />
              <span class="error-text">{{ errors.diaChiCuThe }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer">
        <span class="footer-note">
          Các trường có dấu <span class="required">*</span> là bắt buộc
        </span>
        <div class="button-group">
          <button type="button" class="btn-cancel" @click="goBack">Hủy</button>
          <button type="button" class="btn-submit" @click="handleSave">
            {{ isEdit ? 'Lưu cập nhật' : 'Thêm khách hàng' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import axios from 'axios'
import {
  addKhachHangService,
  detailKhachHangService,
  updateKhachHangService,
} from '@/service/KhachHangService'

const route = useRoute()
const router = useRouter()
const toast = useToast()

const isEdit = ref(false)
const editId = ref(null)

const provinces = ref([])
const districts = ref([])
const wards = ref([])

const previewUrl = ref(null)
const fileSelected = ref(null)

const newKhachHang = ref({
  anh: '',
  maKhachHang: '',
  tenTaiKhoan: '',
  hoTen: '',
  ngaySinh: '',
  gioiTinh: true,
  soDienThoai: '',
  email: '',
  trangThai: 1,
  thanhPho: '',
  quan: '',
  phuong: '',
  diaChiCuThe: '',
  diaChi: '',
})

const errors = ref({
  tenTaiKhoan: '',
  hoTen: '',
  ngaySinh: '',
  soDienThoai: '',
  email: '',
  thanhPho: '',
  quan: '',
  phuong: '',
  diaChiCuThe: '',
})

// --- BIỂU THỨC CHÍNH QUY (VALIDATION REGEX) ---
const validateForm = () => {
  let isValid = true
  Object.keys(errors.value).forEach((key) => (errors.value[key] = ''))

  if (!isEdit.value && !newKhachHang.value.tenTaiKhoan?.trim()) {
    errors.value.tenTaiKhoan = 'Tên tài khoản không được để trống'
    isValid = false
  }

  if (!newKhachHang.value.hoTen?.trim()) {
    errors.value.hoTen = 'Họ và tên không được để trống'
    isValid = false
  }

  if (!newKhachHang.value.ngaySinh) {
    errors.value.ngaySinh = 'Vui lòng chọn ngày sinh'
    isValid = false
  }

  const phoneRegex = /^(0[3|5|7|8|9])([0-9]{8})$/
  if (!newKhachHang.value.soDienThoai) {
    errors.value.soDienThoai = 'Số điện thoại không được để trống'
    isValid = false
  } else if (!phoneRegex.test(newKhachHang.value.soDienThoai)) {
    errors.value.soDienThoai = 'SĐT không hợp lệ (10 số, bắt đầu bằng 0)'
    isValid = false
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!newKhachHang.value.email) {
    errors.value.email = 'Email không được để trống'
    isValid = false
  } else if (!emailRegex.test(newKhachHang.value.email)) {
    errors.value.email = 'Email không đúng định dạng'
    isValid = false
  }

  if (!newKhachHang.value.thanhPho) {
    errors.value.thanhPho = 'Vui lòng chọn Tỉnh/Thành'
    isValid = false
  }
  if (!newKhachHang.value.quan) {
    errors.value.quan = 'Vui lòng chọn Quận/Huyện'
    isValid = false
  }
  if (!newKhachHang.value.phuong) {
    errors.value.phuong = 'Vui lòng chọn Phường/Xã'
    isValid = false
  }
  if (!newKhachHang.value.diaChiCuThe?.trim()) {
    errors.value.diaChiCuThe = 'Vui lòng nhập số nhà, tên đường'
    isValid = false
  }

  return isValid
}

// Watch xóa text báo lỗi khi gõ dữ liệu mới
watch(
  () => newKhachHang.value,
  () => {
    Object.keys(errors.value).forEach((key) => {
      if (newKhachHang.value[key]) errors.value[key] = ''
    })
  },
  { deep: true },
)

// --- XỬ LÝ FILE ẢNH ---
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

// --- LOGIC ĐỊA CHỈ (API TỈNH THÀNH VÀ PHÂN CẤP) ---
const fetchProvinces = async () => {
  try {
    const res = await axios.get('https://provinces.open-api.vn/api/p/')
    provinces.value = res.data
  } catch (error) {
    console.error('Lỗi lấy tỉnh thành:', error)
  }
}

const onProvinceChange = async (isManual = true) => {
  if (isManual) {
    newKhachHang.value.quan = ''
    newKhachHang.value.phuong = ''
    districts.value = []
    wards.value = []
  }
  const province = provinces.value.find((p) => p.name === newKhachHang.value.thanhPho)
  if (province) {
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${province.code}?depth=2`)
    districts.value = res.data.districts
  }
}

const onDistrictChange = async (isManual = true) => {
  if (isManual) {
    newKhachHang.value.phuong = ''
    wards.value = []
  }
  const district = districts.value.find((d) => d.name === newKhachHang.value.quan)
  if (district) {
    const res = await axios.get(`https://provinces.open-api.vn/api/d/${district.code}?depth=2`)
    wards.value = res.data.wards
  }
}

const reloadFullAddress = async () => {
  if (!newKhachHang.value.thanhPho) return
  await onProvinceChange(false)
  if (!newKhachHang.value.quan) return
  await onDistrictChange(false)
}

const goBack = () => router.push('/khach-hang')

// --- LƯU DỮ LIỆU ---
const handleSave = async () => {
  if (!validateForm()) {
    toast.error('Vui lòng kiểm tra lại thông tin!')
    return
  }

  // Ghép chuỗi địa chỉ đầy đủ trước khi gửi sang Backend
  const parts = [
    newKhachHang.value.diaChiCuThe,
    newKhachHang.value.phuong,
    newKhachHang.value.quan,
    newKhachHang.value.thanhPho,
  ].filter((p) => p)
  newKhachHang.value.diaChi = parts.join(', ')

  try {
    if (isEdit.value) {
      await updateKhachHangService(editId.value, newKhachHang.value, fileSelected.value)
      toast.success('Cập nhật khách hàng thành công!')
    } else {
      await addKhachHangService(newKhachHang.value, fileSelected.value)
      toast.success('Thêm khách hàng thành công!')
    }
    goBack()
  } catch (error) {
    toast.error('Thao tác thất bại: ' + (error.response?.data?.message || error.message))
  }
}

onMounted(async () => {
  await fetchProvinces()

  if (route.params.id) {
    isEdit.value = true
    editId.value = route.params.id
    try {
      const data = await detailKhachHangService(editId.value)
      if (data) {
        newKhachHang.value = data
        if (newKhachHang.value.ngaySinh) {
          newKhachHang.value.ngaySinh = newKhachHang.value.ngaySinh.split('T')[0]
        }
        await reloadFullAddress()
      }
    } catch (err) {
      toast.error('Lỗi khi tải thông tin chi tiết khách hàng!')
      console.error(err)
    }
  }
})
</script>

<style scoped>
.employee-container {
  padding: 40px 20px;
  background-color: #f8fafc;
  min-height: 100vh;
  display: flex;
  justify-content: center;
}

.form-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 1050px;
  padding: 35px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 35px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f1f5f9;
}

.card-header .title {
  color: #1e3a8a;
  font-weight: 700;
  font-size: 22px;
  margin: 0;
}

.id-badge {
  background-color: #e2e8f0;
  color: #4a5568;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
}

.card-body {
  display: flex;
  gap: 50px;
}

.form-sidebar {
  width: 260px;
  flex-shrink: 0;
}

.form-main {
  flex: 1;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar-wrapper {
  width: 160px;
  height: 160px;
  border-radius: 50%;
  border: 2px dashed #cbd5e1;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  background-color: #f8fafc;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.radio-group {
  display: flex;
  gap: 25px;
  padding: 10px 0;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
}

.section-title {
  color: #b45309;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 20px;
  text-transform: uppercase;
}

.form-section {
  border-bottom: 1px solid #f1f5f9;
  padding-bottom: 25px;
  margin-bottom: 25px;
}

.no-border {
  border: none;
}

.grid-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 15px;
}

.three-cols {
  grid-template-columns: 1fr 1fr 1fr;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
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
  height: 42px;
  padding: 0 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  outline: none;
  background-color: #ffffff;
}

.input-disabled {
  background-color: #f1f5f9 !important;
  color: #64748b;
  cursor: not-allowed;
}

.input-error {
  border-color: #ef4444 !important;
  background-color: #fef2f2;
}

.error-text {
  color: #ef4444;
  font-size: 11px;
  margin-top: 2px;
  min-height: 15px;
  font-weight: 500;
}

.status-toggle {
  display: flex;
  gap: 12px;
}

.btn-status {
  flex: 1;
  height: 42px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #64748b;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-status.active {
  background: #f0fdf4;
  border-color: #22c55e;
  color: #166534;
}

.btn-status .dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #cbd5e1;
}

.btn-status.active .dot {
  background: #22c55e;
}

.card-footer {
  margin-top: 20px;
  padding-top: 25px;
  border-top: 2px solid #f1f5f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.btn-cancel {
  padding: 10px 25px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  cursor: pointer;
}

.btn-submit {
  padding: 10px 35px;
  background: #1e3a8a;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}

/* --- OVERLAY CHO AVATAR TRÒN --- */
.avatar-wrapper:hover .overlay {
  opacity: 1;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s;
  color: white;
}

.overlay span {
  display: none !important;
}

.overlay::after {
  content: '\f030';
  font-family: 'Font Awesome 5 Free';
  font-weight: 900;
  font-size: 24px;
}
</style>
