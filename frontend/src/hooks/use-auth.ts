import { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import fetchAuth from '../api/Auth';
import { errorState, loadingState } from '../states/FetchState';

type Props = {
  authToken: string;
  roomId: string;
};

const useAuth = ({ authToken, roomId }: Props) => {
  const history = useHistory();
  const setError = useSetRecoilState(errorState);
  const setLoading = useSetRecoilState(loadingState);

  useEffect(() => {
    if (!authToken) {
      return;
    }
    const load = async (): Promise<void> => {
      setLoading(true);
      try {
        void (await fetchAuth(authToken));

        // transition to room
        history.push(`/room/${roomId}`);
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      } finally {
        setLoading(false);
      }
    };

    void load();
  }, [authToken, roomId, setError, setLoading, history]);
};

export default useAuth;
