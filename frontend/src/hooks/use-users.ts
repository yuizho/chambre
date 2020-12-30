import { useEffect, useState } from 'react';
import { useSetRecoilState } from 'recoil';
import fetchUsers, { User } from '../api/Users';
import { errorState, loadingState } from '../states/FetchState';

type Props = {
  roomId: string;
  joinnedCount: number;
};

const useUsers = ({ roomId, joinnedCount }: Props) => {
  const [users, setUsers] = useState<User[]>([]);
  const setError = useSetRecoilState(errorState);
  const setLoading = useSetRecoilState(loadingState);

  useEffect(() => {
    // TODO: check if the room exists and the user in this room
    const load = async (): Promise<void> => {
      setLoading(true);
      try {
        setUsers(await fetchUsers());
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      } finally {
        setLoading(false);
      }
    };

    void load();
  }, [roomId, joinnedCount, setError, setLoading]);

  return [users];
};

export default useUsers;
