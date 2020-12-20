import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  useEventSource,
  useEventSourceListener,
} from 'react-use-event-source-ts';
import { Text, useToast } from '@chakra-ui/react';
import useUsers from '../../hooks/use-users';
import UserList from '../organisms/UserList';

type ParamType = {
  roomId: string;
};

const Room = () => {
  const { roomId } = useParams<ParamType>();
  const [events, setEvents] = useState<string[]>([]);
  const [users] = useUsers({ roomId });
  const toast = useToast();

  const showToast = (userName: string) => {
    toast({
      description: `${userName} がルームに入りました。現在あなたの承認を待っています。`,
    });
  };

  const [eventSource] = useEventSource('/api/subscribe/approved', true);
  useEventSourceListener(
    eventSource,
    ['APPLIED'],
    (event) => {
      showToast(event.data);
      setEvents([...events, event.data]);
    },
    [events],
  );

  return (
    <>
      <UserList users={users} />
      <br />
      {events.map((event) => (
        <Text>{event}</Text>
      ))}
    </>
  );
};

export default Room;
