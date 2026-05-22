import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getMessages, sendMessage } from '../api/messages';
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

  useEffect(() => {
    if (userId == null) return;
    getUser(userId).then(setCurrentUser).catch(() => {
      logout();
      navigate('/login', { replace: true });
    });
  }, [userId, logout, navigate]);

  useEffect(() => {
    const fetchMessages = () => {
      getMessages(CHANNEL).then(setMessages).catch(() => {});
    };
    fetchMessages();
    const id = setInterval(fetchMessages, POLL_INTERVAL_MS);
    return () => clearInterval(id);
  }, []);

  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTop = listRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSend = async (content: string) => {
    const msg = await sendMessage({ channel: CHANNEL, content });
    setMessages(prev => [...prev, msg]);
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
        <MessageList messages={messages} />
      </div>
      <MessageInput onSend={handleSend} />
    </div>
  );
}