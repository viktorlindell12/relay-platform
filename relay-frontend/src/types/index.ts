export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
}

export interface RegisterRequest {
  email: string;
  displayName: string;
  password: string;
}

export interface RegisterResponse {
  userId: number;
}

export interface UserResponse {
  id: number;
  displayName: string;
  email: string;
  createdAt: string;
}

export interface SendMessageRequest {
  channel: string;
  content: string;
}

export interface MessageResponse {
  id: number;
  senderDisplayName: string;
  channel: string;
  content: string;
  createdAt: string;
}

export interface ErrorResponse {
  status: number;
  message: string;
  timestamp: string;
}