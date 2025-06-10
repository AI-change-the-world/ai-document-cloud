import axios from 'axios';

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL, // Vite
    // baseURL: process.env.REACT_APP_API_BASE_URL, // CRA
});

export default apiClient;