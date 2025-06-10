import apiClient from "../../api/client";
import type { ApiResponse } from "../../api/response";

interface UserSummary {
    id: number
    name: string
}

const getUserSummary = async () => {
    const response = await apiClient.get('/user/list/summary');
    const result: ApiResponse<UserSummary[]> = await response.data;
    return result.data;
}

export { getUserSummary };
export type { UserSummary };