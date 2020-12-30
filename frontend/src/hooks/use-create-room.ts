import { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import fetchAuth from '../api/Auth';
import fetchCreateRoom from '../api/CreateRoom';
import { errorState } from '../states/FetchState';

type Props = {
  userName: string;
  password: string;
};

const useCreateRoom = ({ userName, password }: Props) => {
  const history = useHistory();
  const setError = useSetRecoilState(errorState);
  useEffect(() => {
    if (!userName || !password) {
      return;
    }
    const load = async (): Promise<void> => {
      try {
        const roomCreateResult = await fetchCreateRoom(userName, password);
        void (await fetchAuth(roomCreateResult.authToken));
        history.push(`/room/${roomCreateResult.roomId}`);
      } catch (e) {
        if (e instanceof Error) {
          setError({ message: e.message });
        }
      }
    };

    void load();
  }, [userName, password, setError, history]);
};

export default useCreateRoom;
