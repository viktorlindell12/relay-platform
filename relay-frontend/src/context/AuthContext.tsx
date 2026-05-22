import { createContext, useContext, useState, type ReactNode } from 'react';

interface AuthState {
  token: string | null;
  userId: number | null;
}

interface AuthContextValue extends AuthState {
  login: (token: string, userId: number) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState<AuthState>(() => {
    const token = localStorage.getItem('token');
    const raw = localStorage.getItem('userId');
    return { token, userId: raw ? Number(raw) : null };
  });

  const login = (token: string, userId: number) => {
    localStorage.setItem('token', token);
    localStorage.setItem('userId', String(userId));
    setState({ token, userId });
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    setState({ token: null, userId: null });
  };

  return (
    <AuthContext.Provider value={{ ...state, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}