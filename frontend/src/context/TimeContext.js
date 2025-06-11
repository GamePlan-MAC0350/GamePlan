import { createContext, useState } from 'react';

export const TimeContext = createContext();

export function TimeProvider({ children }) {
  const [timeId, setTimeId] = useState(null);
  return (
    <TimeContext.Provider value={{ timeId, setTimeId }}>
      {children}
    </TimeContext.Provider>
  );
}
