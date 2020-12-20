import React from 'react';
import { useParams } from 'react-router-dom';
import useUsers from '../../hooks/use-users';
import UserList from '../organisms/UserList';

type ParamType = {
  roomId: string;
};

const Room = () => {
  const { roomId } = useParams<ParamType>();

  const [users] = useUsers({ roomId });

  return (
    <>
      <UserList users={users} />
    </>
  );
};

export default Room;
