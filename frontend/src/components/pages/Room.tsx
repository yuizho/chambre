import React, { FC } from 'react';
import UserList from '../organisms/UserList';
import EventList from '../../containers/organisms/EventList';
import { User } from '../../api/Users';

type Prop = {
  roomId: string;
  isOpened: boolean;
  users: User[];
  setJoinnedCount: (f: (n: number) => number) => void;
};

const Room: FC<Prop> = ({ roomId, users, setJoinnedCount, isOpened }) => (
  <>
    {isOpened ? (
      <>
        <UserList users={users} />
        <br />
        <EventList {...{ roomId, setJoinnedCount }} />
      </>
    ) : (
      <h1>this room is closed</h1>
    )}
  </>
);

export default Room;
