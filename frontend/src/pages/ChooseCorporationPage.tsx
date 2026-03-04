import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

// Временный список корпораций
const CORPORATIONS = [
    "Titan Industries",
    "Nebula Dynamics",
    "Quantum Core",
    "Aegis Security",
    "Crimson Legion",
    "Stellar Mining Corp",
    "Icarus Technologies"
];

const ChooseCorporationPage: React.FC = () => {
    const [name, setName] = useState('');
    const [corporation, setCorporation] = useState(CORPORATIONS[0]);
    const [specialization, setSpecialization] = useState('hacker');
    const [bio, setBio] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    // Проверяем, есть ли уже агент
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }

        // Проверяем, есть ли у пользователя агент
        fetch('/api/agent/me', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(res => {
            if (res.ok) {
                // Если агент уже есть, перенаправляем на дашборд
                navigate('/dashboard');
            }
        }).catch(err => console.error(err));
    }, [navigate]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }

        try {
            const response = await fetch('/api/agent/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    name,
                    corporation,
                    specialization,
                    bio: bio || undefined
                })
            });

            // Получаем текст ответа
            const responseText = await response.text();
            console.log('Create agent response status:', response.status);
            console.log('Create agent response body:', responseText);

            // Пытаемся распарсить JSON, если возможно
            let data;
            try {
                data = JSON.parse(responseText);
            } catch {
                data = { message: responseText };
            }

            if (response.ok) {
                navigate('/dashboard');
            } else {
                setError(data.message || 'Ошибка создания агента');
            }
        } catch (err) {
            console.error('Network error:', err);
            setError('Ошибка связи, повторите позже');
        } finally {
            setLoading(false);
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
            padding: '20px'
        }}>
            <div style={{
                background: '#141414',
                border: '1px solid #2A2A2A',
                padding: '40px',
                width: '500px',
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
                    &gt; ВЫБОР ЦЕЛИ
                </h1>

                {error && (
                    <div style={{ color: '#FF3A3A', marginBottom: '15px' }}>
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div style={{ marginBottom: '15px' }}>
                        <label style={{ color: '#4A4A4A' }}>Имя агента</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
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
                        <label style={{ color: '#4A4A4A' }}>Корпорация</label>
                        <select
                            value={corporation}
                            onChange={(e) => setCorporation(e.target.value)}
                            style={{
                                width: '100%',
                                background: '#0A0A0A',
                                border: '1px solid #2A2A2A',
                                color: '#00FF9D',
                                padding: '8px',
                                fontFamily: 'inherit',
                                marginTop: '5px',
                            }}
                        >
                            {CORPORATIONS.map(corp => (
                                <option key={corp} value={corp}>{corp}</option>
                            ))}
                        </select>
                    </div>

                    <div style={{ marginBottom: '15px' }}>
                        <label style={{ color: '#4A4A4A' }}>Специализация</label>
                        <select
                            value={specialization}
                            onChange={(e) => setSpecialization(e.target.value)}
                            style={{
                                width: '100%',
                                background: '#0A0A0A',
                                border: '1px solid #2A2A2A',
                                color: '#00FF9D',
                                padding: '8px',
                                fontFamily: 'inherit',
                                marginTop: '5px',
                            }}
                        >
                            <option value="hacker">Хакер</option>
                            <option value="social">Социальный инженер</option>
                            <option value="saboteur">Диверсант</option>
                            <option value="analyst">Аналитик</option>
                        </select>
                    </div>

                    <div style={{ marginBottom: '20px' }}>
                        <label style={{ color: '#4A4A4A' }}>Биография (опционально)</label>
                        <textarea
                            value={bio}
                            onChange={(e) => setBio(e.target.value)}
                            rows={3}
                            style={{
                                width: '100%',
                                background: '#0A0A0A',
                                border: '1px solid #2A2A2A',
                                color: '#00FF9D',
                                padding: '8px',
                                fontFamily: 'inherit',
                                marginTop: '5px',
                                resize: 'vertical'
                            }}
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        style={{
                            width: '100%',
                            background: loading ? '#4A4A4A' : '#00FF9D',
                            border: 'none',
                            color: loading ? '#0A0A0A' : '#0A0A0A',
                            padding: '10px',
                            fontFamily: 'inherit',
                            fontWeight: 'bold',
                            cursor: loading ? 'not-allowed' : 'pointer',
                            transition: '0.3s',
                        }}
                        onMouseOver={(e) => {
                            if (!loading) e.currentTarget.style.background = '#00CCFF';
                        }}
                        onMouseOut={(e) => {
                            if (!loading) e.currentTarget.style.background = '#00FF9D';
                        }}
                    >
                        {loading ? 'СОЗДАНИЕ...' : '[ ВНЕДРИТЬСЯ ]'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default ChooseCorporationPage;