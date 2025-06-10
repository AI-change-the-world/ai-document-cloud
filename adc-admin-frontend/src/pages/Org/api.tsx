import { toast } from "react-toastify"
import apiClient from "../../api/client"
import type { ApiResponse } from "../../api/response"

interface Organization {
    id: number
    name: string
    parentId: number | null
    adminId: number | null
    createdAt: string
    updatedAt: string
    children: Organization[]
}

interface NewOrgRequest {
    name: string
    parentId: number
    leaderId: number | null
}

const OrgActionType = {
    ADD: 'ADD',
    UPDATE: 'UPDATE',
    DELETE: 'DELETE'
} as const;

const getOrganization = async () => {
    try {
        const response = await apiClient.get('/organization/tree');
        const result: ApiResponse<Organization[]> = await response.data;
        return result;
    } catch (error) {
        console.log(error);
        toast.error('获取组织列表失败');
    }

}

export { getOrganization, OrgActionType }
export type { Organization }
export type { NewOrgRequest }
