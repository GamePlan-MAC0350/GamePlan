import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home/Home';
import Login from './pages/Login/Login';
import Forms from './pages/Forms/Forms';
import Home_Usuario from './pages/Home_Usuario/Home_Usuario';
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forms" element={<Forms />} />
        <Route path="/home_usuario" element={<Home_Usuario />} />
      </Routes>
    </Router>
  );
}

export default App;
