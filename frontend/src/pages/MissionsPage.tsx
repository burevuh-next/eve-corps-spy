import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface MissionTemplate {
    id: number;
    type: string;
    name: string;
    description: string;
    baseReward: number;
    baseRisk: number;
    minHackingLevel: number;
    minSocialLevel: number;
    minStealthLevel: number;
    minAnalysisLevel: number;
    stepCount: number;
}

const MissionsPage: React.FC = () => {
    const [missions, setMissions] = useState<MissionTemplate[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMissions = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
                return;
            }

            try {
                const response = await fetch('/api/missions/available', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                const data = await response.json();
                if (response.ok) {
                    setMissions(data);
                } else {
                    setError(data.message || 'Ошибка загрузки миссий');
                }
            } catch (err) {
                setError('Ошибка связи с сервером');
            } finally {
                setLoading(false);
            }
        };

        fetchMissions();
    }, [navigate]);

    const acceptMission = async (templateId: number) => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`/api/missions/accept/${templateId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            const data = await response.json();
            if (response.ok) {
                navigate('/missions/current');
            } else {
                setError(data.message || 'Не удалось принять миссию');
            }
        } catch (err) {
            setError('Ошибка связи');
        }
    };

    if (loading) return <div style={styles.container}>Загрузка...</div>;

    return (
        <div style={styles.container}>
            <div style={styles.terminal}>
                <h1 style={styles.title}>&gt; ДОСТУПНЫЕ ОПЕРАЦИИ</h1>
                {error && <div style={{ color: '#FF3A3A', marginBottom: '15px' }}>{error}</div>}
                {missions.length === 0 ? (
                    <p style={{ color: '#4A4A4A' }}>Нет доступных миссий</p>
                ) : (
                    missions.map(m => (
                        <div key={m.id} style={styles.card}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <h3 style={{ color: '#00CCFF', margin: '0 0 8px 0' }}>{m.name}</h3>
                                <span style={{ color: '#00FF9D', fontSize: '14px' }}>награда: {m.baseReward} ISK</span>
                            </div>
                            <p style={{ color: '#4A4A4A', fontSize: '14px', marginBottom: '12px' }}>{m.description}</p>
                            <div style={{ display: 'flex', gap: '16px', marginBottom: '12px', flexWrap: 'wrap' }}>
                                {m.minHackingLevel > 0 && <span style={styles.requirement}>🔹 Хак {m.minHackingLevel}</span>}
                                {m.minSocialLevel > 0 && <span style={styles.requirement}>🗣️ Соц {m.minSocialLevel}</span>}
                                {m.minStealthLevel > 0 && <span style={styles.requirement}>👤 Скрыт {m.minStealthLevel}</span>}
                                {m.minAnalysisLevel > 0 && <span style={styles.requirement}>📊 Анал {m.minAnalysisLevel}</span>}
                                <span style={styles.requirement}>⚡ шагов: {m.stepCount}</span>
                                <span style={styles.requirement}>⚠️ риск: +{m.baseRisk}%</span>
                            </div>
                            <button
                                onClick={() => acceptMission(m.id)}
                                style={styles.button}
                                onMouseOver={(e) => (e.currentTarget.style.background = '#00CCFF')}
                                onMouseOut={(e) => (e.currentTarget.style.background = '#00FF9D')}
                            >
                                [ ПРИНЯТЬ ]
                            </button>
                        </div>
                    ))
                )}
                <div style={{ marginTop: '20px', textAlign: 'center' }}>
                    <button
                        onClick={() => navigate('/dashboard')}
                        style={{ ...styles.button, background: '#4A4A4A' }}
                    >
                        НАЗАД
                    </button>
                </div>
            </div>
        </div>
    );
};

const styles: { [key: string]: React.CSSProperties } = {
    container: {
        background: '#0A0A0A',
        color: '#00FF9D',
        fontFamily: 'JetBrains Mono, monospace',
        minHeight: '100vh',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        padding: '20px',
    },
    terminal: {
        background: '#141414',
        border: '1px solid #2A2A2A',
        padding: '40px',
        width: '800px',
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
    card: {
        border: '1px solid #2A2A2A',
        padding: '16px',
        marginBottom: '16px',
        borderRadius: '2px',
    },
    requirement: {
        color: '#4A4A4A',
        fontSize: '13px',
    },
    button: {
        background: '#00FF9D',
        border: 'none',
        color: '#0A0A0A',
        padding: '8px 16px',
        fontFamily: 'inherit',
        fontWeight: 'bold',
        cursor: 'pointer',
        transition: '0.3s',
    },
};

export default MissionsPage;