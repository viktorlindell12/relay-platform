import client from './client';
import type { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse } from '../types';

export const login = (data: LoginRequest): Promise<LoginResponse> =>
  client.post<LoginResponse>('/auth/login', data).then(r => r.data);

export const register = (data: RegisterRequest): Promise<RegisterResponse> =>
  client.post<RegisterResponse>('/auth/register', data).then(r => r.data);