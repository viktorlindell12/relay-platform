import client from './client';
import type { UserResponse } from '../types';

export const getUser = (id: number): Promise<UserResponse> =>
  client.get<UserResponse>(`/users/${id}`).then(r => r.data);