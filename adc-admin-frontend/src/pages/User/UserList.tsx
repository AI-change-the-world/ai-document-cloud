import { useEffect, useState } from "react";
import { getUserList, type User } from "./api";
import { Table } from "antd";
import { toast } from "react-toastify";

const PAGE_SIZE = 10;

export default function UserList() {
    const [data, setData] = useState<User[]>([]);
    const [loading, setLoading] = useState(false);
    const [total, setTotal] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);

    const fetchData = async (page: number) => {
        setLoading(true);
        try {
            const response = await getUserList(page, PAGE_SIZE);

            setTotal(response.total);
            setData(response.records);
        } catch (error) {
            console.error("Error fetching user data:", error);
            setData([]);
            setTotal(0);
            toast.error("获取用户列表失败，请稍后重试。")
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData(currentPage);
    }, [currentPage]);

    const columns = [{ title: '用户名', dataIndex: 'username', key: 'username' },
    { title: '用户ID', dataIndex: 'id', key: 'id' }, { title: '组织ID', dataIndex: 'orgId', key: 'orgId' },
    { title: '组织名称', dataIndex: 'orgName', key: 'orgName' },
    { title: '角色ID', dataIndex: 'roleId', key: 'roleId' },
    { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
    { title: '更新时间', dataIndex: 'updatedAt', key: 'updatedAt' }];



    return (
        <Table
            columns={columns}
            dataSource={data}
            loading={loading}
            pagination={{
                current: currentPage,
                pageSize: PAGE_SIZE,
                total: total,
                onChange: (page) => setCurrentPage(page),
                showSizeChanger: false,
                style: { textAlign: 'right' }, // ✅ 右对齐分页
            }}
        />
    );
}


