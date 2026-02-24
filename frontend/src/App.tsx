import './App.css'
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import RegisterPage from './pages/RegisterPage';


//Временная страничка для выбора корпорации
const ChooseCorporatePage = () => (
  <div style={{ color: '#00FF9D', background: '#0A0A0A', minHeight: '100vh', padding: '20px' }}>
        <h1>Выбор корпорации (в разработке)</h1>
    </div>
);

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<RegisterPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/choose-corporation" element={<ChooseCorporatePage />} />
      </Routes>
    </BrowserRouter>
  ) ;
}
  

export default App
