import { useEffect, useState } from 'react';
import { useSetRecoilState } from 'recoil';
import fetchApply from '../api/Apply';
import { errorState, loadingState } from '../states/FetchState';
import { userState } from '../states/UserState';

type Props = {
  roomId: string;
  userName: string;
  password: string;
};

const useApply = ({ roomId, userName, password }: Props) => {
  const [applied, setApplied] = useState(false);
  const setError = useSetRecoilState(errorState);
  const setLoading = useSetRecoilState(loadingState);
  const setUserState = useSetRecoilState(userState);

  useEffect(() => {
    if (!userName || !password) {
      return;
    }
    const load = async (): Promise<void> => {
      setLoading(true);
      try {
        const applyResult = await fetchApply(roomId, userName, password);
        setUserState({
          id: applyResult.userId,
          name: applyResult.userName,
          role: applyResult.role,
        });

        setApplied(true);
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      } finally {
        setLoading(false);
      }
    };

    void load();
  }, [roomId, userName, password, setError, setLoading, setUserState]);

  return [applied];
};

export default useApply;
