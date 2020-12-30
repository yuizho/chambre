import { useEffect } from 'react';
import { useResetRecoilState, useSetRecoilState } from 'recoil';
import fetchApprove from '../api/Approve';
import { errorState, loadingState } from '../states/FetchState';

type Props = {
  userId: string;
  userName: string;
};

const useApprove = ({ userId, userName }: Props) => {
  const setError = useSetRecoilState(errorState);
  const setLoading = useSetRecoilState(loadingState);

  useEffect(() => {
    if (!userId || !userName) {
      return;
    }
    const load = async (): Promise<void> => {
      setLoading(true);
      try {
        void (await fetchApprove(userId, userName));
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, [userId, userName, setError, setLoading]);
};

export default useApprove;
