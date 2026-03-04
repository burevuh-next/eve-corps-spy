import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface Step {
    stepNumber: number;
    description: string;
    completed: boolean;
}

interface Mission {
    id: number;
    name: string;
    description: string;
    currentStep: number;
    totalSteps: number;
    riskAccumulated: number;
    reward: number;
    steps: Step[];
}

const CurrentMissionPage: React.FC = () => {
    const [mission, setMission] = useState<Mission | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const fetchMission = async () => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }

        try {
            const response = await fetch('/api/missions/current', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();
            if (response.ok) {
                setMission(data);
            } else {
                // Если нет активной миссии, перенаправляем на список миссий
                navigate('/missions');
            }
        } catch (err) {
            setError('Ошибка загрузки');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchMission();
    }, []);

    const executeStep = async () => {
        if (!mission) return;
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`/api/missions/step?missionId=${mission.id}`, {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();
            if (response.ok) {
                setMission(data);
            } else {
                setError(data.message || 'Ошибка выполнения шага');
            }
        } catch (err) {
            setError('Ошибка связи');
        }
    };

    if (loading) return <div style={styles.container}>Загрузка...</div>;
    if (!mission) return null;

    const completedSteps = mission.steps.filter(s => s.completed).length;
    const progress = (completedSteps / mission.totalSteps) * 100;

    return (
        <div style={styles.container}>
            <div style={styles.terminal}>
                <h1 style={styles.title}>&gt; {mission.name}</h1>
                <p style={{ color: '#4A4A4A', marginBottom: '20px' }}>{mission.description}</p>

                <div style={{ marginBottom: '20px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                        <span style={styles.label}>Прогресс</span>
                        <span style={{ color: '#00FF9D' }}>{completedSteps}/{mission.totalSteps}</span>
                    </div>
                    <div style={styles.progressBar}>
                        <div style={{ width: `${progress}%`, height: '100%', background: '#00FF9D', borderRadius: '2px' }} />
                    </div>
                </div>

                <div style={{ marginBottom: '20px' }}>
                    <div style={styles.label}>Накопленный риск: <span style={{ color: '#FFB800' }}>{mission.riskAccumulated}%</span></div>
                    <div style={styles.label}>Награда: <span style={{ color: '#00FF9D' }}>{mission.reward} ISK</span></div>
                </div>

                <div style={{ marginBottom: '30px' }}>
                    <h3 style={{ color: '#00CCFF', fontSize: '18px', marginBottom: '10px' }}>ШАГИ:</h3>
                    {mission.steps.map(step => (
                        <div key={step.stepNumber} style={{ display: 'flex', alignItems: 'center', marginBottom: '8px' }}>
                            <span style={{ color: step.completed ? '#4A4A4A' : '#00FF9D', width: '20px' }}>
                                {step.completed ? '✔' : '○'}
                            </span>
                            <span style={{
                                color: step.completed ? '#4A4A4A' : '#00FF9D',
                                textDecoration: step.completed ? 'line-through' : 'none'
                            }}>
                                {step.description}
                            </span>
                        </div>
                    ))}
                </div>

                {completedSteps < mission.totalSteps ? (
                    <button
                        onClick={executeStep}
                        style={styles.button}
                        onMouseOver={(e) => (e.currentTarget.style.background = '#00CCFF')}
                        onMouseOut={(e) => (e.currentTarget.style.background = '#00FF9D')}
                    >
                        [ ВЫПОЛНИТЬ ШАГ ]
                    </button>
                ) : (
                    <div>
                        <p style={{ color: '#00FF9D', marginBottom: '15px' }}>МИССИЯ ВЫПОЛНЕНА!</p>
                        <button
                            onClick={() => navigate('/dashboard')}
                            style={{ ...styles.button, background: '#4A4A4A' }}
                        >
                            НА ДАШБОРД
                        </button>
                    </div>
                )}

                {error && <div style={{ color: '#FF3A3A', marginTop: '15px' }}>{error}</div>}
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
        marginBottom: '20px',
    },
    label: {
        color: '#4A4A4A',
        fontSize: '14px',
        marginBottom: '5px',
    },
    progressBar: {
        width: '100%',
        height: '8px',
        background: '#2A2A2A',
        borderRadius: '2px',
        overflow: 'hidden',
        marginTop: '5px',
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
        width: '100%',
    },
};

export default CurrentMissionPage;