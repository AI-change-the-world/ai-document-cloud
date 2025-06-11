import apiClient from "../../api/client";
import { PaginatedRequest, type ApiResponse, type PaginatedResponse } from "../../api/response";

interface UserSummary {
    id: number
    name: string
}
// {
//         "id": 1,
//         "username": "admin",
//         "roleId": null,
//         "orgId": 0,
//         "createdAt": "2025-06-07T05:34:12",
//         "updatedAt": "2025-06-07T05:34:12",
//         "orgName": null
//       }
interface User {
    id: number;
    username: string;
    roleId: number | null;
    orgId: number;
    createdAt: string;
    updatedAt: string;
    orgName: string | null;
}

const getUserSummary = async () => {
    const response = await apiClient.get('/user/list/summary');
    const result: ApiResponse<UserSummary[]> = await response.data;
    return result.data;
}

const getUserList = async (pageId: number, pageSize: number) => {
    const req = new PaginatedRequest(pageId, pageSize,);
    try {
        const response = await apiClient.post('/user/list', req);
        const result: ApiResponse<PaginatedResponse<User>> = await response.data;
        return result.data;
    } catch (error) {
        console.error("Error fetching user list:", error);
        throw error;
    }

}

export { getUserSummary, getUserList };
export type { UserSummary, User };