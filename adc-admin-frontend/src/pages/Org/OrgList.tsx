import { useEffect, useMemo, useState } from "react";
import { type Organization, type NewOrgRequest, OrgActionType, getOrganization } from "./api";
import { toast } from 'react-toastify';
import { Tree, Button, Input, Space, Modal, Spin, Form, Select } from 'antd';
import type { DataNode } from 'antd/es/tree';
import {
    PlusOutlined,
    EditOutlined,
    DeleteOutlined,
    SearchOutlined,
} from '@ant-design/icons';
import { type UserSummary, getUserSummary } from "../User/api";

export default function OrgList() {
    const [treeData, setTreeData] = useState<Organization[]>([]);
    const [expandedKeys, setExpandedKeys] = useState<React.Key[]>([]);
    const [searchValue, setSearchValue] = useState('');
    const [autoExpandParent, setAutoExpandParent] = useState(true);
    const [loading, setLoading] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [event, setEvent] = useState('');
    const [form] = Form.useForm();
    const [currentOrg, setCurrentOrg] = useState<Organization | null>(null);
    const [users, setUsers] = useState<UserSummary[]>([]);

    const handleEvent = async (current: Organization, event: string) => {
        setModalVisible(true);
        setCurrentOrg(current);
        setEvent(event);
    };

    const handleAddDepartment = async (current: Organization, values: any) => {
        try {
            console.log('values:', values);

            // 这里替换为你的API调用
            console.log('添加部门:', {
                name: values.name,
                parentId: current.id
            });

            toast.success('部门添加成功');
            setModalVisible(false);
            // 这里应该重新获取部门树数据
        } catch (error) {
            toast.error('添加部门出错');
        }
    };

    const fetchData = async () => {
        try {
            setLoading(true);
            const result = await getOrganization();
            const users = await getUserSummary();
            console.log('获取users数据:', users);
            if (result && result.data) {
                setTreeData(result.data);
                // 默认展开第一级
                if (result.data.length > 0) {
                    setExpandedKeys([result.data[0].id]);
                }
            }
            setUsers(users);
        } catch (error) {
            toast.error('获取组织数据/人员信息失败');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    const onExpand = (newExpandedKeys: React.Key[]) => {
        setExpandedKeys(newExpandedKeys);
        setAutoExpandParent(false);
    };

    // 搜索功能
    const onSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = e.target;
        setSearchValue(value);

        // if (!value) {
        //     setExpandedKeys([]);
        //     return;
        // }

        // 获取从根节点到匹配节点的所有父级key
        const getAllParentKeys = (node: Organization, tree: Organization[]): React.Key[] => {
            const parentKeys: React.Key[] = [];

            const findParents = (currentNode: Organization, currentPath: React.Key[]) => {
                if (currentNode.id === node.id) {
                    parentKeys.push(...currentPath);
                    return;
                }

                if (currentNode.children) {
                    currentNode.children.forEach(child => {
                        findParents(child, [...currentPath, currentNode.id]);
                    });
                }
            };

            tree.forEach(rootNode => {
                findParents(rootNode, []);
            });

            return parentKeys;
        };

        // 查找所有匹配的节点
        const findMatchingNodes = (tree: Organization[]): Organization[] => {
            let matches: Organization[] = [];

            tree.forEach(node => {
                if (node.name.toLowerCase().includes(value.toLowerCase())) {
                    matches.push(node);
                }

                if (node.children) {
                    matches = matches.concat(findMatchingNodes(node.children));
                }
            });

            return matches;
        };

        const matchingNodes = findMatchingNodes(treeData);

        // 收集所有需要展开的父级key
        const allExpandedKeys = matchingNodes.reduce((keys, node) => {
            const parentKeys = getAllParentKeys(node, treeData);
            return [...keys, ...parentKeys];
        }, [] as React.Key[]);

        // 去重
        const uniqueExpandedKeys = Array.from(new Set(allExpandedKeys));

        if (uniqueExpandedKeys.length === 0) {
            return;
        }

        setExpandedKeys(uniqueExpandedKeys);
        setAutoExpandParent(true);
    };

    // 渲染树节点标题
    const renderTitle = (node: Organization) => {
        const index = node.name.indexOf(searchValue);
        const beforeStr = node.name.substring(0, index);
        const afterStr = node.name.substring(index + searchValue.length);
        return (
            <div style={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                <span>
                    {index > -1 ? (
                        <span>
                            {beforeStr}
                            <span style={{ color: '#f50' }}>{searchValue}</span>
                            {afterStr}
                        </span>
                    ) : (
                        <span>{node.name}</span>
                    )}
                </span>
                <Space>
                    <Button
                        type="text"
                        size="small"
                        icon={<PlusOutlined />}
                        onClick={(e) => {
                            e.stopPropagation();
                            handleEvent(node, OrgActionType.ADD);
                        }}
                    />

                    {node.id === 1 ? null : <Button
                        type="text"
                        size="small"
                        icon={<EditOutlined />}
                        onClick={(e) => {
                            e.stopPropagation();
                            // handleEdit(node);
                        }}
                    />}

                    {node.id === 1 ? null : <Button
                        type="text"
                        size="small"
                        icon={<DeleteOutlined />}
                        onClick={(e) => {
                            e.stopPropagation();
                            // handleDelete(node);
                            handleEvent(node, OrgActionType.DELETE);
                        }}
                    />}

                </Space>
            </div>
        );
    };

    // 转换数据为AntD Tree需要的格式
    const treeNodes = useMemo(() => {
        const generateTreeNodes = (data: Organization[]): DataNode[] => {
            return data.map(item => ({
                key: item.id,
                title: renderTitle(item),
                children: item.children ? generateTreeNodes(item.children) : undefined,
            }));
        };
        return generateTreeNodes(treeData);
    }, [treeData, searchValue]);


    return (

        loading ? (<div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100%',  // 或指定具体高度
        }}>
            <Spin />
        </div>) : (< div style={{ padding: 8 }
        }>
            <div style={{ marginBottom: 16 }}>
                <Space>
                    <Input
                        placeholder="搜索组织"
                        suffix={<SearchOutlined />}
                        value={searchValue}
                        onChange={onSearch}
                        style={{ width: 300 }}
                    />
                </Space>
            </div>

            <Tree
                showLine
                showIcon
                expandedKeys={expandedKeys}
                autoExpandParent={autoExpandParent}
                onExpand={onExpand}
                treeData={treeNodes}
            />

            <Modal
                title={"添加子部门"}
                open={modalVisible && event === OrgActionType.ADD}
                okText="保存"
                cancelText="取消"
                onOk={() => {
                    form.validateFields().then(values => {
                        if (!currentOrg) return;
                        if (!values) return;
                        handleAddDepartment(currentOrg, values)
                    });
                }}
                onCancel={() => setModalVisible(false)}
                destroyOnHidden
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="name"
                        label="部门名称"
                        rules={[{ required: true, message: '请输入部门名称' }]}
                    >
                        <Input placeholder="请输入部门名称" />
                    </Form.Item>

                    <Form.Item
                        name="leader"
                        label="负责人"
                        rules={[{ required: true, message: '请选择负责人' }]}
                    >
                        {/* <Input placeholder="请选择负责人" /> */}
                        <Select
                            showSearch
                            // mode="multiple"
                            options={users ? users.map((user) => {
                                return { value: user.id, label: user.name }
                            }) : []}
                            filterOption={(input, option) =>
                                (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                            }
                            placeholder="请选择或输入负责人"
                            style={{ width: '100%' }}

                        />
                    </Form.Item>
                </Form>
            </Modal>

            <Modal
                title={"删除部门"}
                open={modalVisible && event === OrgActionType.DELETE}
                okText="确定"
                cancelText="取消"
                onOk={() => {
                    if (!currentOrg) return;
                    //  handleAddDepartment(currentOrg, values)
                }}
                onCancel={() => setModalVisible(false)}
                destroyOnHidden
            >
                <Form form={form} layout="vertical">
                    确定要删除{currentOrg?.name}吗？
                </Form>
            </Modal>
        </div >)



    );
}