import { useEffect, useState } from 'react';
import { useSetRecoilState } from 'recoil';
import fetchRoomStatus from '../api/RoomStatus';
import HttpError from '../lib/error/HttpError';
import { errorState, loadingState } from '../states/FetchState';

type Props = {
  roomId: string;
};

const useRoomStatus = ({ roomId }: Props) => {
  const [opened, setOpened] = useState(false);
  const setError = useSetRecoilState(errorState);
  const setLoading = useSetRecoilState(loadingState);

  useEffect(() => {
    const load = async (): Promise<void> => {
      setLoading(true);
      try {
        const roomStatus = await fetchRoomStatus(roomId);
        setOpened(roomStatus.status === 0);
      } catch (e) {
        if (e instanceof HttpError) {
          setOpened(false);
        } else if (e instanceof Error) {
          setError({ message: e.message });
        }
      } finally {
        setLoading(false);
      }
    };

    void load();
  }, [roomId, setError, setLoading]);

  return [opened];
};

export default useRoomStatus;
