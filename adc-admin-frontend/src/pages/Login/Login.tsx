import { Button, Form, Input } from 'antd';
import { useNavigate } from 'react-router-dom';
import './Login.css';

export default function Login() {
  const navigate = useNavigate();
  const onFinish = (values: any) => {
    console.log('Success:', values);
    navigate('/org');
  };

  return (
    <div className="login-container">
      <img
        src="/public/background.png"
        alt="background"
        className="background-image"
      />
      <div style={{ maxWidth: 300, minWidth: 300 }} className='login-form'>
        <Form onFinish={onFinish} >
          <Form.Item name="username" rules={[{ required: true }]}>
            <Input placeholder="用户名" />
          </Form.Item>
          <Form.Item name="password" rules={[{ required: true }]}>
            <Input.Password placeholder="密码" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block >
              登录
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>


  );
}
