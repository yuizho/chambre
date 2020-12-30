import { useEffect, useState } from 'react';
import { useSetRecoilState } from 'recoil';
import fetchUsers, { User } from '../api/Users';
import { errorState } from '../states/FetchState';

type Props = {
  roomId: string;
  joinnedCount: number;
};

const useUsers = ({ roomId, joinnedCount }: Props) => {
  const [users, setUsers] = useState<User[]>([]);
  const setError = useSetRecoilState(errorState);

  useEffect(() => {
    // TODO: check if the room exists and the user in this room
    const load = async (): Promise<void> => {
      try {
        setUsers(await fetchUsers());
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      }
    };

    void load();
  }, [roomId, joinnedCount, setError]);

  return [users];
};

export default useUsers;
