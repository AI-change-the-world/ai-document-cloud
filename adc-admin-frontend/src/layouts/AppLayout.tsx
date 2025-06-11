import { Layout, Menu } from 'antd';
import { Outlet, useNavigate } from 'react-router-dom';

const { Header, Sider, Content } = Layout;

export default function AppLayout() {
  const navigate = useNavigate();

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 16px', height: '50px' }}>AI Document Cloud</Header>
      <Layout>
        <Sider width={200} style={{ background: '#fff' }}>
          <Menu
            mode="inline"
            defaultSelectedKeys={['org']}
            style={{ height: '100%' }}
            items={[
              { key: 'dashboard', label: 'Dashboard', onClick: () => navigate('/dashboard') },
              { key: 'org', label: '组织管理', onClick: () => navigate('/org') },
              { key: 'user', label: '用户管理', onClick: () => navigate('/user') },
              { key: 'knowledge', label: '知识库管理', onClick: () => navigate('/knowledge') },
              { key: 'documents', label: '文档管理', onClick: () => navigate('/documents') },
            ]}
          />
        </Sider>
        <Content style={{ background: '#f5f5f5', padding: 16, flex: 1 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
