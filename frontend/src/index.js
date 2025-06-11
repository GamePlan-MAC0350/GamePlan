import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { TimeProvider } from './context/TimeContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <TimeProvider>
      <App />
    </TimeProvider>
  </React.StrictMode>
);

