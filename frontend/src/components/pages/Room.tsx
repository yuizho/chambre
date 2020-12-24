import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  useEventSource,
  useEventSourceListener,
} from 'react-use-event-source-ts';
import { Button, Text, useToast } from '@chakra-ui/react';
import useUsers from '../../hooks/use-users';
import UserList from '../organisms/UserList';
import useApprove from '../../hooks/use-approve';

type ParamType = {
  roomId: string;
};

type AppliedResult = {
  id: string;
  name: string;
};

const Room = () => {
  const { roomId } = useParams<ParamType>();
  const [events, setEvents] = useState<AppliedResult[]>([]);
  const [users] = useUsers({ roomId });
  const toast = useToast();
  const [useApproveProp, setUseApproveProp] = useState({
    userId: '',
    userName: '',
  });

  useApprove(useApproveProp);

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
      const applied = JSON.parse(event.data) as AppliedResult;
      setEvents([...events, applied]);
      showToast(applied.name);
    },
    [events],
  );

  return (
    <>
      <UserList users={users} />
      <br />
      {events.map((event) => (
        <>
          <Text key={event.id}>{event.name} applied to this room</Text>
          <Button
            mt={3}
            colorScheme="teal"
            onClick={() =>
              setUseApproveProp({ userId: event.id, userName: event.name })
            }
          >
            approve
          </Button>
        </>
      ))}
    </>
  );
};

export default Room;
