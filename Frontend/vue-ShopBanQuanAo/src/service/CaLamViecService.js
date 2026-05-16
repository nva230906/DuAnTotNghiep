const Api = "http://localhost:8080/api/shop-ban-quan-ao/ca-lam-viec";

const phanTrangCaLamViecService = async (pageNo, pageSize) => {
    const res = await fetch(`${Api}/page?pageNo=${pageNo}&end=${pageSize}`);
    if (!res.ok) {
        throw new Error("Lỗi phân trang")
    }
    return await res.json();
}