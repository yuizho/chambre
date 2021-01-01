import { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import fetchAuth from '../api/Auth';
import fetchCreateRoom from '../api/CreateRoom';
import fetchUserStatus from '../api/UserStatus';
import { errorState, loadingState } from '../states/FetchState';
import { userState } from '../states/UserState';

type Props = {
  userName: string;
  password: string;
};

const useCreateRoom = ({ userName, password }: Props) => {
  const history = useHistory();
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
        const roomCreateResult = await fetchCreateRoom(userName, password);
        void (await fetchAuth(roomCreateResult.authToken));

        // store user state
        const userStatus = await fetchUserStatus();
        setUserState(userStatus);

        // transition to room
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
  }, [userName, password, setError, setLoading, setUserState, history]);
};

export default useCreateRoom;
