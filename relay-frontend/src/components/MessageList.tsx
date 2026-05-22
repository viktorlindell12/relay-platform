import type { MessageResponse } from '../types';

interface Props {
  messages: MessageResponse[];
}

export default function MessageList({ messages }: Props) {
  return (
    <ul className="message-list">
      {messages.map(msg => (
        <li key={msg.id} className="message">
          <span className="sender">{msg.senderDisplayName}</span>
          <span className="content">{msg.content}</span>
          <time className="timestamp">{new Date(msg.createdAt).toLocaleTimeString()}</time>
        </li>
      ))}
    </ul>
  );
}