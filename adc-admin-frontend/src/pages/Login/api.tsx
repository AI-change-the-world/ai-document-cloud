import apiClient from "../../api/client";
import type { ApiResponse } from "../../api/response";

interface AuthRequest {
    username: string,
    password: string
}

interface UserToken {
    token: string,
    userId: number,
    role: string
}

const login = async (data: AuthRequest) => {
    try {
        const response = await apiClient.post('/auth/login', data);
        const result: ApiResponse<UserToken> = await response.data;
        return result;
    } catch (error) {
        console.log(error);
        throw new Error('登录失败');
    }

}

export default login;