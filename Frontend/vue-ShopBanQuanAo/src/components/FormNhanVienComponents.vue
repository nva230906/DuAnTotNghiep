<template>
  <div class="employee-container">
    <div class="form-card">
      <div class="card-header">
        <h3 class="title">| {{ isEdit ? 'CẬP NHẬT NHÂN VIÊN' : 'THÔNG TIN NHÂN VIÊN' }}</h3>
        <span class="id-badge" v-if="newNhanVien.maNhanVien">ID: {{ newNhanVien.maNhanVien }}</span>
      </div>

      <div class="card-body">
        <!-- SIDEBAR -->
        <div class="form-sidebar">
          <div class="avatar-section">
            <div class="avatar-wrapper" @click="$refs.fileInputRef.click()">
              <img
                v-if="previewUrl || newNhanVien.anh"
                :src="
                  previewUrl ||
                  `http://localhost:8080/api/shop-ban-quan-ao/nhan-vien/uploads/${newNhanVien.anh}`
                "
                class="avatar-img"
              />
              <div v-else class="placeholder">
                <i class="fas fa-camera"></i>
                <span>Tải ảnh lên</span>
              </div>
              <div class="overlay" v-if="previewUrl || newNhanVien.anh">
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
              v-model="newNhanVien.tenNhanVien"
              :class="{ 'input-error': errors.tenNhanVien }"
              placeholder="Nguyễn Văn A"
            />
            <span class="error-text">{{ errors.tenNhanVien }}</span>
          </div>

          <div class="input-group">
            <label>Ngày sinh <span class="required">*</span></label>
            <input
              type="date"
              v-model="newNhanVien.ngaySinh"
              :class="{ 'input-error': errors.ngaySinh }"
            />
            <span class="error-text">{{ errors.ngaySinh }}</span>
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

        <!-- MAIN FORM -->
        <div class="form-main">
          <div class="form-section">
            <h4 class="section-title">LIÊN HỆ & TÀI KHOẢN</h4>
            <div class="grid-row">
              <div class="input-group">
                <label>Số điện thoại <span class="required">*</span></label>
                <input
                  type="text"
                  v-model="newNhanVien.soDienThoai"
                  :class="{ 'input-error': errors.soDienThoai }"
                  placeholder="09xxx"
                />
                <span class="error-text">{{ errors.soDienThoai }}</span>
              </div>
              <div class="input-group">
                <label>Email <span class="required">*</span></label>
                <input
                  type="email"
                  v-model="newNhanVien.email"
                  :class="{ 'input-error': errors.email }"
                  placeholder="example@gmail.com"
                />
                <span class="error-text">{{ errors.email }}</span>
              </div>
            </div>
            <div class="grid-row">
              <div class="input-group">
                <label>CCCD <span class="required">*</span></label>
                <input
                  type="text"
                  v-model="newNhanVien.canCuocCongDan"
                  :class="{ 'input-error': errors.canCuocCongDan }"
                  placeholder="12 chữ số"
                />
                <span class="error-text">{{ errors.canCuocCongDan }}</span>
              </div>
              <div class="input-group">
                <label>Mật khẩu <span class="required">*</span></label>
                <input
                  type="password"
                  v-model="newNhanVien.matKhau"
                  :class="{ 'input-error': errors.matKhau }"
                  placeholder="••••••"
                />
                <span class="error-text">{{ errors.matKhau }}</span>
              </div>
            </div>
          </div>

          <div class="form-section">
            <h4 class="section-title">PHÂN QUYỀN & TRẠNG THÁI</h4>
            <div class="grid-row">
              <div class="input-group">
                <label>Quyền hạn <span class="required">*</span></label>
                <select v-model="newNhanVien.idVaiTro" :class="{ 'input-error': errors.idVaiTro }">
                  <option :value="null">Chọn quyền hạn</option>
                  <option v-for="role in listVaiTro" :key="role.id" :value="role.id">
                    {{ role.tenVaiTro }}
                  </option>
                </select>
                <span class="error-text">{{ errors.idVaiTro }}</span>
              </div>
              <div class="input-group">
                <label>Trạng thái</label>
                <div class="status-toggle">
                  <button
                    type="button"
                    :class="['btn-status', { active: newNhanVien.trangThai === 1 }]"
                    @click="newNhanVien.trangThai = 1"
                  >
                    <span class="dot"></span> Đang làm việc
                  </button>
                  <button
                    type="button"
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
              <select
                v-model="selectedTinh"
                @change="onProvinceChange(true)"
                :class="{ 'input-error': errors.diaChi }"
              >
                <option value="">Tỉnh/Thành</option>
                <option v-for="p in provinces" :key="p.code" :value="p.name">{{ p.name }}</option>
              </select>
              <select
                v-model="selectedHuyen"
                @change="onDistrictChange(true)"
                :class="{ 'input-error': errors.diaChi }"
              >
                <option value="">Quận/Huyện</option>
                <option v-for="d in districts" :key="d.code" :value="d.name">{{ d.name }}</option>
              </select>
              <select v-model="selectedXa" :class="{ 'input-error': errors.diaChi }">
                <option value="">Xã/Phường</option>
                <option v-for="w in wards" :key="w.code" :value="w.name">{{ w.name }}</option>
              </select>
            </div>
            <div class="input-group full-width mt-15">
              <label>Địa chỉ cụ thể <span class="required">*</span></label>
              <input
                type="text"
                v-model="diaChiChiTiet"
                :class="{ 'input-error': errors.diaChiChiTiet }"
                placeholder="Số nhà, ngõ..."
              />
              <span class="error-text">{{ errors.diaChiChiTiet || errors.diaChi }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer">
        <span class="footer-note"
          >Các trường có dấu <span class="required">*</span> là bắt buộc</span
        >
        <div class="button-group">
          <button type="button" class="btn-cancel" @click="$router.back()">Hủy</button>
          <button type="button" class="btn-submit" @click="handleSave">
            {{ isEdit ? 'Lưu cập nhật' : 'Thêm nhân viên' }}
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

const errors = ref({
  tenNhanVien: '',
  ngaySinh: '',
  email: '',
  soDienThoai: '',
  canCuocCongDan: '',
  matKhau: '',
  idVaiTro: '',
  diaChi: '',
  diaChiChiTiet: '',
})

const previewUrl = ref(null)
const fileSelected = ref(null)

// --- VALIDATION LOGIC ---
const validateForm = () => {
  let isValid = true
  // Reset errors
  Object.keys(errors.value).forEach((key) => (errors.value[key] = ''))

  if (!newNhanVien.value.tenNhanVien?.trim()) {
    errors.value.tenNhanVien = 'Họ tên không được để trống'
    isValid = false
  }

  if (!newNhanVien.value.ngaySinh) {
    errors.value.ngaySinh = 'Vui lòng chọn ngày sinh'
    isValid = false
  } else {
    const age = new Date().getFullYear() - new Date(newNhanVien.value.ngaySinh).getFullYear()
    if (age < 18) {
      errors.value.ngaySinh = 'Nhân viên phải từ 18 tuổi trở lên'
      isValid = false
    }
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!newNhanVien.value.email) {
    errors.value.email = 'Email không được để trống'
    isValid = false
  } else if (!emailRegex.test(newNhanVien.value.email)) {
    errors.value.email = 'Email không đúng định dạng'
    isValid = false
  }

  const phoneRegex = /^(0[3|5|7|8|9])([0-9]{8})$/
  if (!newNhanVien.value.soDienThoai) {
    errors.value.soDienThoai = 'Số điện thoại không được để trống'
    isValid = false
  } else if (!phoneRegex.test(newNhanVien.value.soDienThoai)) {
    errors.value.soDienThoai = 'SĐT không hợp lệ (10 số, bắt đầu bằng 0)'
    isValid = false
  }

  if (!newNhanVien.value.canCuocCongDan) {
    errors.value.canCuocCongDan = 'CCCD không được để trống'
    isValid = false
  } else if (!/^\d{12}$/.test(newNhanVien.value.canCuocCongDan)) {
    errors.value.canCuocCongDan = 'CCCD phải gồm 12 ký tự số'
    isValid = false
  }

  if (!isEdit.value || newNhanVien.value.matKhau) {
    if (!newNhanVien.value.matKhau || newNhanVien.value.matKhau.length < 6) {
      errors.value.matKhau = 'Mật khẩu phải từ 6 ký tự'
      isValid = false
    }
  }

  if (!newNhanVien.value.idVaiTro) {
    errors.value.idVaiTro = 'Vui lòng chọn quyền hạn'
    isValid = false
  }

  if (!selectedTinh.value || !selectedHuyen.value || !selectedXa.value) {
    errors.value.diaChi = 'Vui lòng chọn đầy đủ địa chỉ'
    isValid = false
  }

  if (!diaChiChiTiet.value?.trim()) {
    errors.value.diaChiChiTiet = 'Vui lòng nhập địa chỉ cụ thể'
    isValid = false
  }

  return isValid
}

// Watch để xóa lỗi khi người dùng nhập liệu
watch(
  () => newNhanVien.value,
  () => {
    Object.keys(errors.value).forEach((key) => {
      if (newNhanVien.value[key]) errors.value[key] = ''
    })
  },
  { deep: true },
)

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
  if (!validateForm()) {
    toast.error('Vui lòng kiểm tra lại thông tin!')
    return
  }

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
      toast.success('Cập nhật thành công!')
    } else {
      await addNhanVienService(newNhanVien.value, fileSelected.value)
      toast.success('Thêm mới thành công!')
    }
    router.push('/nhan-vien')
  } catch (e) {
    toast.error(e.response?.data?.message || 'Có lỗi xảy ra!')
  }
}

onMounted(async () => {
  const roles = await getAllVaiTroService()
  listVaiTro.value = roles
  await fetchProvinces()

  if (route.params.id) {
    isEdit.value = true
    editId.value = route.params.id
    try {
      const data = await detailNhanVienService(route.params.id)
      newNhanVien.value = data
      if (data.diaChi) {
        const parts = data.diaChi.split(SEPARATOR).map((p) => p.trim())
        if (parts.length >= 4) {
          selectedTinh.value = parts.pop()
          await onProvinceChange(false)
          selectedHuyen.value = parts.pop()
          await onDistrictChange(false)
          selectedXa.value = parts.pop()
          diaChiChiTiet.value = parts.join(SEPARATOR)
        } else {
          diaChiChiTiet.value = data.diaChi
        }
      }
    } catch (error) {
      toast.error('Không thể tải thông tin!')
    }
  }
})
</script>

<style scoped>
/* Giữ nguyên CSS cũ của bạn và thêm phần dưới đây */
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
  max-width: 1050px;
  border-radius: 12px;
  padding: 35px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 35px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f1f5f9;
}

.title {
  color: #1e3a8a;
  font-weight: 700;
  font-size: 22px;
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
.mt-15 {
  margin-top: 15px;
}
/* --- CSS MỚI ĐỂ XÓA CHỮ 'THAY ĐỔI ẢNH' --- */

/* 1. Sửa overlay phủ kín và làm tối ảnh */
.overlay {
  position: absolute;
  top: 0; /* Bắt đầu từ đỉnh */
  left: 0;
  width: 100%;
  height: 100%; /* Phủ kín 100% chiều cao ảnh tròn */
  background: rgba(0, 0, 0, 0.5); /* Màu nền tối */
  border-radius: 50%; /* Bo tròn theo ảnh */

  /* Căn giữa icon camera */
  display: flex;
  justify-content: center;
  align-items: center;

  opacity: 0;
  transition: opacity 0.3s;
  color: white; /* Màu của icon */
}

/* 2. Ẩn thẻ span chứa chữ */
.overlay span {
  display: none !important; /* Thêm !important để chắc chắn ẩn chữ */
}

/* 3. Thêm icon Camera vào giữa (tùy chọn nhưng nên có để user biết là bấm vào để đổi) */
/* Nếu bạn chưa có font-awesome, hãy bỏ qua đoạn này */
.overlay::after {
  content: '\f030'; /* Mã icon camera của FontAwesome */
  font-family: 'Font Awesome 5 Free';
  font-weight: 900;
  font-size: 24px;
}
</style>
