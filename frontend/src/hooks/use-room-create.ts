import { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import fetchAuth from '../api/Auth';
import fetchRoomCreate from '../api/RoomCreate';

type Props = {
  userName: string;
  password: string;
};

const useRoomCreate = ({ userName, password }: Props) => {
  const history = useHistory();
  useEffect(() => {
    if (!userName || !password) {
      return;
    }
    void fetchRoomCreate(userName, password).then((roomCreateResult) => {
      //  TODO: save  results to recoil
      void fetchAuth(roomCreateResult.authToken).then(() => history.push('/'));
    });
  }, [userName, password, history]);
};

export default useRoomCreate;
