import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface AgentInfo {
    id: number;
    name: string;
    corporationName: string;
    specialization: string;
    suspicionLevel: number;
    credits: number;
    bio: string;
}

const Dashboard: React.FC = () => {
    const [agent, setAgent] = useState<AgentInfo | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // Состояние для текущей миссии
    const [currentMission, setCurrentMission] = useState<{ id: number; name: string; currentStep: number; totalSteps: number } | null>(null);

    useEffect(() => {
        const fetchAgent = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
                return;
            }

            try {
                const response = await fetch('/api/agent', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                const data = await response.json();
                if (response.ok) {
                    setAgent(data);
                } else {
                    setError(data.message || 'Ошибка загрузки данных агента');
                }
            } catch (err) {
                setError('Ошибка связи с сервером');
            } finally {
                setLoading(false);
            }
        };
        fetchAgent();
    }, [navigate]);

    // Загрузка текущей миссии
    useEffect(() => {
        const fetchCurrentMission = async () => {
            const token = localStorage.getItem('token');
            try {
                const response = await fetch('/api/missions/current', {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                if (response.ok) {
                    const data = await response.json();
                    setCurrentMission({
                        id: data.id,
                        name: data.name,
                        currentStep: data.currentStep,
                        totalSteps: data.totalSteps
                    });
                }
            } catch (err) {
                // игнорируем, если нет миссии
            }
        };
        fetchCurrentMission();
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
    };

    if (loading) {
        return (
            <div style={styles.container}>
                <div style={styles.terminal}>
                    <p style={{ color: '#00FF9D' }}>Загрузка данных агента...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div style={styles.container}>
                <div style={styles.terminal}>
                    <h1 style={styles.title}>ОШИБКА</h1>
                    <p style={{ color: '#FF3A3A' }}>{error}</p>
                    <button onClick={handleLogout} style={styles.button}>Выйти</button>
                </div>
            </div>
        );
    }

    if (!agent) return null;

    const suspicionColor = agent.suspicionLevel > 70 ? '#FF3A3A' : agent.suspicionLevel > 40 ? '#FFB800' : '#00FF9D';

    return (
        <div style={styles.container}>
            <div style={styles.terminal}>
                <h1 style={styles.title}>&gt; ДАШБОРД АГЕНТА</h1>
                
                <div style={styles.infoGrid}>
                    <div style={styles.infoItem}>
                        <span style={styles.label}>Имя:</span>
                        <span style={styles.value}>{agent.name}</span>
                    </div>
                    <div style={styles.infoItem}>
                        <span style={styles.label}>Корпорация:</span>
                        <span style={styles.value}>{agent.corporationName}</span>
                    </div>
                    <div style={styles.infoItem}>
                        <span style={styles.label}>Специализация:</span>
                        <span style={styles.value}>{agent.specialization}</span>
                    </div>
                    <div style={styles.infoItem}>
                        <span style={styles.label}>Кредиты:</span>
                        <span style={styles.value}>{agent.credits} ISK</span>
                    </div>
                </div>

                <div style={styles.suspicionSection}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '5px' }}>
                        <span style={styles.label}>Уровень подозрения:</span>
                        <span style={{ color: suspicionColor }}>{agent.suspicionLevel}%</span>
                    </div>
                    <div style={styles.progressBar}>
                        <div style={{
                            width: `${agent.suspicionLevel}%`,
                            height: '100%',
                            background: suspicionColor,
                            borderRadius: '2px',
                            transition: 'width 0.3s ease'
                        }} />
                    </div>
                </div>

                {agent.bio && (
                    <div style={styles.bioSection}>
                        <span style={styles.label}>Биография:</span>
                        <p style={{ color: '#00FF9D', marginTop: '5px' }}>{agent.bio}</p>
                    </div>
                )}

                {/* Блок текущей миссии */}
                {currentMission && (
                    <div style={{ marginTop: '20px', borderTop: '1px solid #2A2A2A', paddingTop: '15px' }}>
                        <h3 style={{ color: '#00CCFF', fontSize: '18px', marginBottom: '10px' }}>ТЕКУЩАЯ МИССИЯ</h3>
                        <p style={{ color: '#00FF9D', marginBottom: '10px' }}>{currentMission.name}</p>
                        <div style={{ marginBottom: '10px' }}>
                            <span style={styles.label}>Прогресс:</span>
                            <div style={styles.progressBar}>
                                <div style={{
                                    width: `${(currentMission.currentStep / currentMission.totalSteps) * 100}%`,
                                    height: '100%',
                                    background: '#00FF9D',
                                    borderRadius: '2px'
                                }} />
                            </div>
                        </div>
                        <button
                            onClick={() => navigate('/missions/current')}
                            style={styles.button}
                        >
                            ПРОДОЛЖИТЬ
                        </button>
                    </div>
                )}

                <div style={{ marginTop: '30px', display: 'flex', gap: '10px', justifyContent: 'center' }}>
                    <button onClick={() => navigate('/missions')} style={styles.button}>НОВЫЕ МИССИИ</button>
                    <button onClick={() => navigate('/skills')} style={styles.button}>НАВЫКИ</button>
                    <button onClick={handleLogout} style={{ ...styles.button, background: '#FF3A3A' }}>ВЫЙТИ</button>
                </div>
            </div>
        </div>
    );
};

const styles: { [key: string]: React.CSSProperties } = {
    container: {
        background: '#0A0A0A',
        minHeight: '100vh',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        fontFamily: 'JetBrains Mono, monospace',
        padding: '20px',
    },
    terminal: {
        background: '#141414',
        border: '1px solid #2A2A2A',
        padding: '40px',
        width: '600px',
        maxWidth: '100%',
        boxShadow: '0 0 20px rgba(0,255,157,0.2)',
    },
    title: {
        fontSize: '24px',
        fontWeight: 'bold',
        background: 'linear-gradient(90deg, #00FF9D, #00CCFF)',
        WebkitBackgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
        marginBottom: '30px',
    },
    infoGrid: {
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '15px',
        marginBottom: '20px',
    },
    infoItem: {
        borderBottom: '1px solid #2A2A2A',
        paddingBottom: '5px',
    },
    label: {
        color: '#4A4A4A',
        fontSize: '14px',
        display: 'block',
    },
    value: {
        color: '#00FF9D',
        fontSize: '18px',
        fontWeight: 'bold',
    },
    suspicionSection: {
        margin: '20px 0',
    },
    progressBar: {
        width: '100%',
        height: '8px',
        background: '#2A2A2A',
        borderRadius: '2px',
        overflow: 'hidden',
    },
    bioSection: {
        marginTop: '20px',
        borderTop: '1px solid #2A2A2A',
        paddingTop: '15px',
    },
    button: {
        background: '#00FF9D',
        border: 'none',
        color: '#0A0A0A',
        padding: '10px 20px',
        fontFamily: 'inherit',
        fontWeight: 'bold',
        cursor: 'pointer',
        transition: '0.3s',
    },
};

export default Dashboard;