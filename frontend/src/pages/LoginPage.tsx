import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';

const LoginPage: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!email || !password) {
            setError('Все поля должны быть заполнены');
            return;
        }

        setError('');

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password }),
            });

            const data = await response.json();

            if (response.ok) {
                // Сохраняем токен в localStorage
                localStorage.setItem('token', data.token);
                localStorage.setItem('user', JSON.stringify({
                    id: data.id,
                    email: data.email
                }));
                
                navigate('/dashboard');
            } else {
                setError(data.message || 'Ошибка входа');
            }
        } catch (err) {
            setError('Ошибка связи, повторите позже');
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
                    &gt; ВХОД
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

                    <div style={{ marginBottom: '20px' }}>
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
                        [ ВОЙТИ ]
                    </button>
                </form>

                <div style={{ marginTop: '15px', textAlign: 'center' }}>
                    <span style={{ color: '#4A4A4A' }}>Нет аккаунта? </span>
                    <a href="/register" style={{ color: '#00CCFF', textDecoration: 'none' }}>
                        Регистрация
                    </a>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;