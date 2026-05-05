const Api = "http://localhost:8080/api/shop-ban-quan-ao/nhan-vien";

export const phanTrangNhanVienService = async (pageNo, pageSize) => {
    const res = await fetch(`${Api}/page?pageNo=${pageNo}&end=${pageSize}`);
    if (!res.ok) {
        throw new Error("Lỗi phân trang")
    }
    return await res.json();
}

export const deleteNhanVienService = async (id) => {
    const res = await fetch(`${Api}/delete/${id}`, {
        method: 'DELETE',
    });
    if (!res.ok) {
        throw new Error("Lỗi xoá")
    }
}

export const searchNhanVienService = async (keyword) => {
    const res = await fetch(`${Api}/search?keyword=${encodeURIComponent(keyword)}`)
    if (!res.ok) {
        throw new Error("lỗi search")
    }
    return await res.json();
}

const buildFormData = (data, file) => {
    const formData = new FormData();
    formData.append("data", new Blob([JSON.stringify(data)], { type: "application/json" }));
    if (file) {
        formData.append("file", file);
    }
    return formData;
};

export const addNhanVienService = async (data, file) => {
    const res = await fetch(`${Api}/add`, {
        method: 'POST',
        body: buildFormData(data, file)
    });
    if (!res.ok) {
        throw new Error("lỗi khi thêm nhân viên")
    }
}

export const updateNhanVienService = async (id, data, file) => {
    const res = await fetch(`${Api}/update/${id}`, {
        method: 'PUT',
        body: buildFormData(data, file)
    });
    if (!res.ok) {
        throw new Error("lỗi khi cập nhập nhân viên")
    }
}

export const detailNhanVienService = async (id) => {
    const res = await fetch(`${Api}/detail/${id}`);
    if (!res.ok) {
        throw new Error("lỗi khi lấy dữ liệu");
    }
    return await res.json();
}

export const exportNhanVienExcelService = async (keyword, gender, status) => {
    const params = new URLSearchParams();
    if (keyword) params.append("keyword", keyword);
    if (gender !== undefined && gender !== null) params.append("gender", gender);
    if (status !== undefined && status !== null) params.append("status", status);
    const res = await fetch(`${Api}/export-excel?${params.toString()}`, {
        method: 'GET',
    });
    if (!res.ok) {
        throw new Error("Lỗi khi xuất file Excel");
    }
    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `DanhSachNhanVien_${new Date().getTime()}.xlsx`; // Đặt tên file
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);
};
