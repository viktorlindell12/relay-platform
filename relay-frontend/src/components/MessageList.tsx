import type { MessageResponse } from '../types';

interface Props {
  messages: MessageResponse[];
  currentUserId: number | null;
  onTogglePin: (id: number) => Promise<void>;
}

export default function MessageList({ messages, currentUserId, onTogglePin }: Props) {
  if (messages.length === 0) {
    return <p className="empty-state">No messages yet. Say something!</p>;
  }

  return (
    <ul className="message-list">
      {messages.map(msg => (
        <li key={msg.id} className={`message${msg.pinned ? ' pinned' : ''}`}>
          <span className="sender">{msg.senderDisplayName}</span>
          <span className="content">{msg.content}</span>
          <span className="message-meta">
            <time className="timestamp">{new Date(msg.createdAt).toLocaleTimeString()}</time>
            {msg.senderId === currentUserId && (
              <button
                className={`pin-btn${msg.pinned ? ' active' : ''}`}
                onClick={() => void onTogglePin(msg.id)}
                title={msg.pinned ? 'Unpin message' : 'Pin message'}
              >
                {msg.pinned ? '★' : '☆'}
              </button>
            )}
          </span>
        </li>
      ))}
    </ul>
  );
}