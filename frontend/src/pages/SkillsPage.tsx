import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface Skill {
    skillId: number;
    skillName: string;
    level: number;
    experience: number;
    maxLevel: number;
}

const SkillsPage: React.FC = () => {
    const [skills, setSkills] = useState<Skill[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSkills = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
                return;
            }

            try {
                const response = await fetch('/api/agent/skills', {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                const data = await response.json();
                if (response.ok) {
                    setSkills(data);
                } else {
                    setError(data.message || 'Ошибка загрузки навыков');
                }
            } catch (err) {
                setError('Ошибка связи с сервером');
            } finally {
                setLoading(false);
            }
        };

        fetchSkills();
    }, [navigate]);

    if (loading) {
        return (
            <div style={styles.container}>
                <div style={styles.terminal}>Загрузка навыков...</div>
            </div>
        );
    }

    return (
        <div style={styles.container}>
            <div style={styles.terminal}>
                <h1 style={styles.title}>&gt; НАВЫКИ АГЕНТА</h1>

                {error && <div style={{ color: '#FF3A3A', marginBottom: '15px' }}>{error}</div>}

                {skills.length === 0 ? (
                    <p style={{ color: '#4A4A4A' }}>Нет данных о навыках</p>
                ) : (
                    <div>
                        {skills.map(skill => {
                            const progress = (skill.experience / (skill.level * 100)) * 100;
                            return (
                                <div key={skill.skillId} style={styles.skillCard}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                                        <span style={{ color: '#00CCFF', fontWeight: 'bold' }}>{skill.skillName}</span>
                                        <span style={{ color: '#00FF9D' }}>Уровень {skill.level}/{skill.maxLevel}</span>
                                    </div>
                                    <div style={styles.progressBar}>
                                        <div style={{
                                            width: `${progress}%`,
                                            height: '100%',
                                            background: '#00FF9D',
                                            borderRadius: '2px'
                                        }} />
                                    </div>
                                    <div style={{ color: '#4A4A4A', fontSize: '12px', marginTop: '5px' }}>
                                        Опыт: {skill.experience} / {skill.level * 100}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}

                <div style={{ marginTop: '20px', textAlign: 'center' }}>
                    <button
                        onClick={() => navigate('/dashboard')}
                        style={styles.button}
                        onMouseOver={(e) => (e.currentTarget.style.background = '#00CCFF')}
                        onMouseOut={(e) => (e.currentTarget.style.background = '#00FF9D')}
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
        width: '500px',
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
    skillCard: {
        border: '1px solid #2A2A2A',
        padding: '16px',
        marginBottom: '16px',
        borderRadius: '2px',
    },
    progressBar: {
        width: '100%',
        height: '6px',
        background: '#2A2A2A',
        borderRadius: '2px',
        overflow: 'hidden',
        marginTop: '8px',
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

export default SkillsPage;