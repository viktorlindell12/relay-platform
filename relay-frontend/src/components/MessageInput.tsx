import { useState, type FormEvent, type KeyboardEvent } from 'react';

interface Props {
  onSend: (content: string) => Promise<void>;
}

export default function MessageInput({ onSend }: Props) {
  const [content, setContent] = useState('');
  const [sending, setSending] = useState(false);

  const submit = async () => {
    const trimmed = content.trim();
    if (!trimmed || sending) return;
    setSending(true);
    try {
      await onSend(trimmed);
      setContent('');
    } finally {
      setSending(false);
    }
  };

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    void submit();
  };

  const handleKeyDown = (e: KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      void submit();
    }
  };

  return (
    <form className="message-input" onSubmit={handleSubmit}>
      <textarea
        value={content}
        onChange={e => setContent(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Message #general — Enter to send, Shift+Enter for new line"
        rows={2}
        disabled={sending}
      />
      <button type="submit" disabled={sending || !content.trim()}>
        Send
      </button>
    </form>
  );
}