import { useEffect, useState } from 'react';
import fetchUsers, { User } from '../api/Users';

type Props = {
  roomId: string;
  joinnedCount: number;
};

const useUsers = ({ roomId, joinnedCount }: Props) => {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    // TODO: check if the room exists and the user in this room

    void fetchUsers().then((u) => setUsers(u));
  }, [roomId, joinnedCount]);

  return [users];
};

export default useUsers;
