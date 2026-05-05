import axios from 'axios'

const BASE_URL = 'http://localhost:8080/api/shop-ban-quan-ao/vai-tro'

export const getAllVaiTroService = async () => {
    const res = await axios.get(BASE_URL)
    return res.data
}