import { Button, Form, Input } from 'antd';
import { useNavigate } from 'react-router-dom';
import { sm3 } from 'sm-crypto';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './Login.css';
import login from './api';
import { useState } from 'react';

export default function Login() {
  const navigate = useNavigate();

  const [isLoading, setIsLoading] = useState(false)
  const onFinish = async (values: any) => {
    const username = values.username;
    const password = sm3(values.password);
    setIsLoading(true);
    try {
      const data = await login({ username, password });

      if (!data) {
        toast.error('登录失败');
        return;
      }

      if (data.code !== 200) {
        // alert(data.message);
        toast.error(data.message);
        return;
      }

      console.log('Success:', data);
      localStorage.setItem(import.meta.env.VITE_LOCAL_STORAGE_KEY_PREFIX + "_" + import.meta.env.VITE_USER_INFO_KEY, JSON.stringify(data.data));
      toast.success('登录成功');
      navigate('/dashboard');
    } catch (e) {
      toast.error('登录失败');
    } finally {
      setIsLoading(false);
    }

  };

  return (
    <div className="login-container">
      <img
        src="/background.png"
        alt="background"
        className="background-image"
      />
      <div style={{ maxWidth: 300, minWidth: 300 }} className='login-form'>
        <h3 style={{ display: 'flex', justifyContent: 'center' }}>AI Document Cloud</h3>
        <Form onFinish={onFinish} >
          <Form.Item name="username" rules={[{ required: true }]}>
            <Input placeholder="用户名" />
          </Form.Item>
          <Form.Item name="password" rules={[{ required: true }]}>
            <Input.Password placeholder="密码" />
          </Form.Item>
          <Form.Item>
            {
              isLoading ? (
                <Button type="primary" htmlType="submit" block disabled>
                  登录中...
                </Button>
              ) : (
                <Button type="primary" htmlType="submit" block >
                  登录
                </Button>
              )
            }

          </Form.Item>
        </Form>
      </div>
    </div>


  );
}
