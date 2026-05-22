import client from './client';
import type { MessageResponse, SendMessageRequest } from '../types';

export const getMessages = (channel: string): Promise<MessageResponse[]> =>
  client.get<MessageResponse[]>('/messages', { params: { channel } }).then(r => r.data);

export const sendMessage = (data: SendMessageRequest): Promise<MessageResponse> =>
  client.post<MessageResponse>('/messages', data).then(r => r.data);