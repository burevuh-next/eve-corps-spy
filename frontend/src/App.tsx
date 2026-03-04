import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import ChooseCorporationPage from './pages/ChooseCorporationPage';
import Dashboard from './pages/Dashboard';
import MissionsPage from './pages/MissionsPage';
import CurrentMissionPage from './pages/CurrentMissionPage';
import SkillsPage from './pages/SkillsPage';
import type { JSX } from 'react';

// Заглушка для дашборда
const DashboardPage = () => (
    <div style={{ color: '#00FF9D', background: '#0A0A0A', minHeight: '100vh', padding: '20px' }}>
        <h1>Главный дашборд (в разработке)</h1>
        <button onClick={() => {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }} style={{ background: '#FF3A3A', color: 'white', padding: '10px' }}>
            Выйти
        </button>
    </div>
);

// Защищённый маршрут
const PrivateRoute = ({ children }: { children: JSX.Element }) => {
    const token = localStorage.getItem('token');
    return token ? children : <Navigate to="/login" />;
};

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/choose-corporation" element={
                    <PrivateRoute>
                        <ChooseCorporationPage />
                    </PrivateRoute>
                } />
                <Route path="/dashboard" element={
                    <PrivateRoute>
                        <Dashboard />
                    </PrivateRoute>
                } />
                <Route path="/missions" element={
                    <PrivateRoute>
                        <MissionsPage />
                    </PrivateRoute>
                } />
                <Route path="/missions/current" element={
                    <PrivateRoute>
                        <CurrentMissionPage />
                    </PrivateRoute>
                } />
                <Route path="/skills" element={
                    <PrivateRoute>
                        <SkillsPage />
                    </PrivateRoute>
                } />
                <Route path="/" element={<Navigate to="/register" />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;