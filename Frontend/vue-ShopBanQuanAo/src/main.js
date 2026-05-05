import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'
// Import thư viện và CSS
import Toast from "vue-toastification";
import "vue-toastification/dist/index.css";

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
//cấu hình
const options = {
    position: "top-right", // Góc trên bên phải
    timeout: 1000,         // Tự đóng sau 3 giây
    closeOnClick: true,
    pauseOnFocusLoss: true,
    pauseOnHover: true,
    draggable: true,
    draggablePercent: 0.6,
    showCloseButtonOnHover: false,
    hideProgressBar: true, // Ẩn thanh chạy bên dưới cho sạch giống ảnh
    closeButton: "button",
    icon: true,
    rtl: false
};
const app = createApp(App)
app.use(Toast, options)
app.use(router)

app.mount('#app')
