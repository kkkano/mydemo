import {
  LockOutlined,
  UserOutlined,
} from '@ant-design/icons';
import { Alert, message, Tabs } from 'antd';
import React, { useState } from 'react';
import { ProFormText, LoginForm } from '@ant-design/pro-form';
import { history, useModel } from 'umi';
import Footer from '@/components/Footer';
import styles from './index.less';
import { SYSTEM_LOGO } from '@/components';
import { register } from '@/services/ant-design-pro/api';
const LoginMessage: React.FC<{
  content: string;
}> = ({ content }) => (
  <Alert
    style={{
      marginBottom: 24,
    }}
    message={content}
    type="error"
    showIcon
  />
);
const Register: React.FC = () => {
  const [userLoginState, setUserLoginState] = useState<API.LoginResult>({});
  const [type, setType] = useState<string>('account');
  const { initialState, setInitialState } = useModel('@@initialState');
  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    if (userInfo) {
      await setInitialState((s) => ({
        ...s,
        currentUser: userInfo,
      }));
    }
  };
  const handleSubmit = async (values: API.RegisterParams) => {
    const {userPassword,checkPassword} = values;
    //校验
    if (userPassword !== checkPassword){
      message.error('两次输入的密码不一致');
      return;
    }

   try {
  // 注册
  const id = await register(values);

  if (id ) {
    const defaultLoginSuccessMessage = '注册成功！';
    message.success(defaultLoginSuccessMessage);


    /** 此方法会跳转到 redirect 参数所在的位置 */

    if (!history) return;
    const {query} = history.location;
    history.push({
      pathname:'user/login',
      query
    });
    return;
  }else {
    //记录下id的值，方便以后排错
    throw new Error('register error id = ${id}')
  }

} catch (error: any) {
  const defaultLoginFailureMessage = '注册失败，请重试！';
  message.error(defaultLoginFailureMessage);
}
  };
  const { status, type: loginType } = userLoginState;
  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <LoginForm
        submitter={{
          searchConfig: {
            submitText: "注册"
          }
        }}

        
          logo={<img alt="logo" src={SYSTEM_LOGO} />}
          title="用户中心"
          subTitle={'欢迎注册捏'}
          initialValues={{
            autoLogin: true,
          }}
   
          onFinish={async (values) => {
            await handleSubmit(values as API.RegisterParams);
          }}
        >
          <Tabs activeKey={type} onChange={setType}>
            <Tabs.TabPane key="account" tab={'账户密码注册'} />
            
          </Tabs>

          {status === 'error' && loginType === 'account' && (
            <LoginMessage content={'错误的账号和密码(请输入账号)'} />
          )}
          {type === 'account' && (
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder={'请输入账号'}
                rules={[
                  {
                    required: true,
                    message: '账号是必填项！',
                  },
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon} />,
                }}
                placeholder={'请输入密码'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                  {
                    min:8,
                    type:'string',
                    message:'长度不能小于8',
                  }
                ]}
              />
               <ProFormText.Password
              name="checkPassword"
              fieldProps={{
                size: 'large',
                prefix: <LockOutlined className={styles.prefixIcon} />,
              }}
                placeholder={'请确认密码'}
                rules={[
                  {
                    required: true,
                    message: '确认密码是必填项！',
                  },
                  {
                    min :8,
                    type :'string',
                    message: '密码长度不能低于8位！',
                  },
                ]}
              />
                <ProFormText
                name="planetCode"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder={'请输入星球编号'}
                rules={[
                  {
                    required: true,
                    message: '星球编号是必填项！',
                  },
                ]}
              />

            </>
          )}


        
          <div
            style={{
              marginBottom: 24,
            }}
          >
           
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Register;
