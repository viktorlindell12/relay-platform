import { useState, useEffect, useRef, useCallback } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getMessages, sendMessage, togglePin } from '../api/messages';
import { getUser } from '../api/user';
import MessageList from '../components/MessageList';
import MessageInput from '../components/MessageInput';
import UserBadge from '../components/UserBadge';
import type { MessageResponse, UserResponse } from '../types';

const CHANNEL = 'general';
const POLL_INTERVAL_MS = 3000;

export default function Chat() {
  const { userId, logout } = useAuth();
  const navigate = useNavigate();
  const [messages, setMessages] = useState<MessageResponse[]>([]);
  const [currentUser, setCurrentUser] = useState<UserResponse | null>(null);
  const listRef = useRef<HTMLDivElement>(null);
  const fetchingRef = useRef(false);

  useEffect(() => {
    if (userId == null) return;
    getUser(userId).then(setCurrentUser).catch((err: unknown) => {
      const status = axios.isAxiosError(err) ? err.response?.status : null;
      if (status === 401 || status === 403) {
        logout();
        navigate('/login', { replace: true });
      }
    });
  }, [userId, logout, navigate]);

  const fetchMessages = useCallback(() => {
    if (fetchingRef.current) return;
    fetchingRef.current = true;
    getMessages(CHANNEL)
      .then(setMessages)
      .catch(() => {})
      .finally(() => { fetchingRef.current = false; });
  }, []);

  useEffect(() => {
    fetchMessages();
    const id = setInterval(fetchMessages, POLL_INTERVAL_MS);
    return () => clearInterval(id);
  }, [fetchMessages]);

  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTop = listRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSend = async (content: string) => {
    const msg = await sendMessage({ channel: CHANNEL, content });
    setMessages(prev => [...prev, msg]);
  };

  const handleTogglePin = async (messageId: number) => {
    const updated = await togglePin(messageId);
    setMessages(prev => prev.map(m => m.id === messageId ? updated : m));
  };

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
    <div className="chat-layout">
      <header className="chat-header">
        <span className="channel-name"># {CHANNEL}</span>
        {currentUser && <UserBadge user={currentUser} onLogout={handleLogout} />}
      </header>
      <div className="message-list-wrapper" ref={listRef}>
        <MessageList messages={messages} currentUserId={userId} onTogglePin={handleTogglePin} />
      </div>
      <MessageInput onSend={handleSend} />
    </div>
  );
}