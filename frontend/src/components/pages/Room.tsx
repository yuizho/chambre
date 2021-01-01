import React, { FC, useState } from 'react';
import UserList from '../organisms/UserList';
import EventList from '../organisms/EventList';
import { EventState } from '../../states/EventState';
import { User } from '../../api/Users';

type Prop = {
  roomId: string;
  users: User[];
  events: EventState[];
};

const Room: FC<Prop> = ({ roomId, users, events }) => {
  return (
    <>
      <UserList users={users} />
      <br />
      <EventList events={events.filter((e) => e.roomId === roomId)} />
    </>
  );
};

export default Room;
