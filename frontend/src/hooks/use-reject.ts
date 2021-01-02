import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import fetchReject from '../api/Reject';
import { errorState, loadingState } from '../states/FetchState';

type Props = {
  userId: string;
};

const useReject = ({ userId }: Props) => {
  const setError = useSetRecoilState(errorState);
  const setLoading = useSetRecoilState(loadingState);

  useEffect(() => {
    if (!userId) {
      return;
    }
    const load = async (): Promise<void> => {
      setLoading(true);
      try {
        void (await fetchReject(userId));
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, [userId, setError, setLoading]);
};

export default useReject;
