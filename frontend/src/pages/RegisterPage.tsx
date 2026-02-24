import React, {useState} from "react";
import {useNavigate} from 'react-router-dom';



const RegisterPage:React.FC = () => {
  console.log('RegisterPage rendered');

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    //Валидация
    if (!email || !password || !confirmPassword) {
      setError ('Все поля должны быть заполнены')
      return;
    }
    if (password.length < 6) {
      setError ('Пароль должен быть больше 6 символов')
      return;
    }
    if (password != confirmPassword) {
      setError ('Пароли не совпадают')
    }

    setError('');
    
    try {
      console.log('Sending request to /api/auth/register');
      const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify({ email, password}),
      });

      console.log('Response status:', response.status);
      console.log('Response headers:', response.headers.get('content-type'));
      
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        const data = await response.json();
        console.log('Server response:', response.status, data);
      
        if ( response.ok) {
          navigate('/choose-corporation');
        } else {
          let errorMessage = typeof data === 'string' ? data : data.message || 'Регистрация провалена';
          
          if (typeof data === 'string') {
            errorMessage = data;
          } else if (data.message) {
            errorMessage = data.message;
          } else if (data.errors) {
            // Если сервер вернул объект с ошибками валидации
            errorMessage = Object.values(data.errors).join(', ');
          }
          setError(errorMessage);
        }
      } else {
        const text = await response.text();
        console.error('Текст ответа:', text);
       
        setError(text || `Ошибка сервера: ${response.status}`);
      }
    } catch (err) {
      console.error('Ошибка связи:', err);
      setError('Ошибка связи, повторите позже')
    }
  };

  return (
        <div style={{
            background: '#0A0A0A',
            color: '#00FF9D',
            fontFamily: 'JetBrains Mono, monospace',
            minHeight: '100vh',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        }}>
            <div style={{
                background: '#141414',
                border: '1px solid #2A2A2A',
                padding: '40px',
                width: '400px',
                boxShadow: '0 0 20px rgba(0,255,157,0.2)',
            }}>
                <h1 style={{
                    fontSize: '24px',
                    fontWeight: 'bold',
                    background: 'linear-gradient(90deg, #00FF9D, #00CCFF)',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                    marginBottom: '20px',
                }}>
                    &gt; РЕГИСТРАЦИЯ
                </h1>

                {error && (
                    <div style={{ color: '#FF3A3A', marginBottom: '15px' }}>
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div style={{ marginBottom: '15px' }}>
                        <label style={{ color: '#4A4A4A' }}>Email</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            style={{
                                width: '100%',
                                background: '#0A0A0A',
                                border: '1px solid #2A2A2A',
                                color: '#00FF9D',
                                padding: '8px',
                                fontFamily: 'inherit',
                                marginTop: '5px',
                            }}
                        />
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label style={{ color: '#4A4A4A' }}>Пароль</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            style={{
                                width: '100%',
                                background: '#0A0A0A',
                                border: '1px solid #2A2A2A',
                                color: '#00FF9D',
                                padding: '8px',
                                fontFamily: 'inherit',
                                marginTop: '5px',
                            }}
                        />
                    </div>

                    <div style={{ marginBottom: '20px' }}>
                        <label style={{ color: '#4A4A4A' }}>Подтвердите пароль</label>
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            style={{
                                width: '100%',
                                background: '#0A0A0A',
                                border: '1px solid #2A2A2A',
                                color: '#00FF9D',
                                padding: '8px',
                                fontFamily: 'inherit',
                                marginTop: '5px',
                            }}
                        />
                    </div>

                    <button
                        type="submit"
                        style={{
                            width: '100%',
                            background: '#00FF9D',
                            border: 'none',
                            color: '#0A0A0A',
                            padding: '10px',
                            fontFamily: 'inherit',
                            fontWeight: 'bold',
                            cursor: 'pointer',
                            transition: '0.3s',
                        }}
                        onMouseOver={(e) => (e.currentTarget.style.background = '#00CCFF')}
                        onMouseOut={(e) => (e.currentTarget.style.background = '#00FF9D')}
                    >
                        [ ЗАРЕГИСТРИРОВАТЬСЯ ]
                    </button>
                </form>

                <div style={{ marginTop: '15px', textAlign: 'center' }}>
                    <span style={{ color: '#4A4A4A' }}>Уже есть аккаунт? </span>
                    <a href="/login" style={{ color: '#00CCFF', textDecoration: 'none' }}>
                        Вход
                    </a>
                </div>
            </div>
        </div>
    );
};


export default RegisterPage;