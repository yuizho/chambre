import { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import fetchUsers, { User } from '../api/Users';
import HttpError from '../lib/error/HttpError';
import { errorState, loadingState } from '../states/FetchState';

type Props = {
  roomId: string;
  joinnedCount: number;
  isOpened: boolean;
};

const useUsers = ({ roomId, joinnedCount, isOpened }: Props) => {
  const history = useHistory();
  const [users, setUsers] = useState<User[]>([]);
  const setError = useSetRecoilState(errorState);
  const setLoading = useSetRecoilState(loadingState);

  useEffect(() => {
    if (!isOpened) {
      return;
    }
    const load = async (): Promise<void> => {
      setLoading(true);
      try {
        setUsers(await fetchUsers());
      } catch (e) {
        if (e instanceof HttpError && e.response.status === 403) {
          // transition to room
          history.push(`/apply/${roomId}`);
        } else if (e instanceof Error) {
          setError({ message: e.message });
        }
      } finally {
        setLoading(false);
      }
    };

    void load();
  }, [roomId, joinnedCount, setError, setLoading, isOpened, history]);

  return [users];
};

export default useUsers;
