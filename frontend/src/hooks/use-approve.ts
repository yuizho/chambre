import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import fetchApprove from '../api/Approve';
import { errorState } from '../states/FetchState';

type Props = {
  userId: string;
  userName: string;
};

const useApprove = ({ userId, userName }: Props) => {
  const setError = useSetRecoilState(errorState);

  useEffect(() => {
    if (!userId || !userName) {
      return;
    }
    const load = async (): Promise<void> => {
      try {
        void (await fetchApprove(userId, userName));
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      }
    };
    void load();
  }, [userId, userName, setError]);
};

export default useApprove;
