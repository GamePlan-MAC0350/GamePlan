import { createContext, useState, useEffect } from 'react';

export const TimeContext = createContext();

export function TimeProvider({ children }) {
  const [timeId, setTimeIdState] = useState(() => {
    // Recupera do localStorage ao iniciar
    const saved = localStorage.getItem('timeId');
    return saved ? parseInt(saved) : null;
  });

  // Sempre que mudar, salva no localStorage
  useEffect(() => {
    if (timeId !== null) {
      localStorage.setItem('timeId', timeId);
    } else {
      localStorage.removeItem('timeId');
    }
  }, [timeId]);

  // Setter que também salva
  const setTimeId = (id) => {
    setTimeIdState(id);
    if (id !== null) {
      localStorage.setItem('timeId', id);
    } else {
      localStorage.removeItem('timeId');
    }
  };

  return (
    <TimeContext.Provider value={{ timeId, setTimeId }}>
      {children}
    </TimeContext.Provider>
  );
}
