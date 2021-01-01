import React, { FC, useState } from 'react';
import { useParams } from 'react-router-dom';
import useUsers from '../../hooks/use-users';
import RoomComponent from '../../components/pages/Room';
import useRoomStatus from '../../hooks/use-room-status';

type ParamType = {
  roomId: string;
};

const Room: FC = () => {
  const { roomId } = useParams<ParamType>();
  const [joinnedCount, setJoinnedCount] = useState(1);

  const [isOpened] = useRoomStatus({ roomId });
  const [users] = useUsers({ roomId, joinnedCount, isOpened });

  return <RoomComponent {...{ roomId, isOpened, users, setJoinnedCount }} />;
};

export default Room;
