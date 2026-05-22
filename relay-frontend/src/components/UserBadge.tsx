import type { UserResponse } from '../types';

interface Props {
  user: UserResponse;
  onLogout: () => void;
}

export default function UserBadge({ user, onLogout }: Props) {
  return (
    <div className="user-badge">
      <span className="display-name">{user.displayName}</span>
      <button className="logout-btn" onClick={onLogout}>
        Sign out
      </button>
    </div>
  );
}