import { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import fetchAuth from '../api/Auth';
import fetchCreateRoom from '../api/CreateRoom';
import { errorState, loadingState } from '../states/FetchState';

type Props = {
  userName: string;
  password: string;
};

const useCreateRoom = ({ userName, password }: Props) => {
  const history = useHistory();
  const setError = useSetRecoilState(errorState);
  const setLoading = useSetRecoilState(loadingState);

  useEffect(() => {
    if (!userName || !password) {
      return;
    }
    const load = async (): Promise<void> => {
      setLoading(true);
      try {
        const roomCreateResult = await fetchCreateRoom(userName, password);
        void (await fetchAuth(roomCreateResult.authToken));
        history.push(`/room/${roomCreateResult.roomId}`);
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      } finally {
        setLoading(false);
      }
    };

    void load();
  }, [userName, password, setError, setLoading, history]);
};

export default useCreateRoom;
