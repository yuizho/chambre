import React, { FC } from 'react';
import UserList from '../organisms/UserList';
import EventList from '../organisms/EventList';
import { EventState } from '../../states/EventState';
import { User } from '../../api/Users';

type Prop = {
  roomId: string;
  isOpened: boolean;
  users: User[];
  events: EventState[];
};

const Room: FC<Prop> = ({ roomId, users, events, isOpened }) => (
  <>
    {isOpened ? (
      <>
        <UserList users={users} />
        <br />
        <EventList events={events.filter((e) => e.roomId === roomId)} />
      </>
    ) : (
      <h1>this room is closed</h1>
    )}
  </>
);

export default Room;
