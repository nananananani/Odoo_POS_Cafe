import React, { createContext, useState } from 'react';
import { User } from '../types/user';
import { setAuthToken } from '../api/client';

interface AuthContextType {
  token: string | null;
  user: User | null;
  isAuthenticated: boolean;
  login: (token: string, user: User) => void;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [token, setTokenState] = useState<string | null>(null);
  const [user, setUser] = useState<User | null>(null);

  const login = (newToken: string, newUser: User) => {
    setTokenState(newToken);
    setUser(newUser);
    setAuthToken(newToken);
  };

  const logout = () => {
    setTokenState(null);
    setUser(null);
    setAuthToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, user, isAuthenticated: !!token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
