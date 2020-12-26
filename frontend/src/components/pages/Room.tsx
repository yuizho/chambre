import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  useEventSource,
  useEventSourceListener,
} from 'react-use-event-source-ts';
import { Box, Button, Text, useToast } from '@chakra-ui/react';
import useUsers from '../../hooks/use-users';
import UserList from '../organisms/UserList';
import useApprove from '../../hooks/use-approve';

type ParamType = {
  roomId: string;
};

type PushedMessage = {
  eventId: string;
  userId: string;
  name: string;
  type: 'approved' | 'joined';
};

const Room = () => {
  const { roomId } = useParams<ParamType>();
  const [events, setEvents] = useState<PushedMessage[]>([]);
  const [joinnedCount, setjoinnedCount] = useState(1);
  const [users] = useUsers({ roomId, joinnedCount });
  const toast = useToast();
  const [useApproveProp, setUseApproveProp] = useState({
    userId: '',
    userName: '',
  });

  useApprove(useApproveProp);

  const showToast = (description: string) => {
    toast({
      description,
    });
  };

  const [eventSource] = useEventSource('/api/subscribe/approved', true);
  useEventSourceListener(
    eventSource,
    ['APPLIED'],
    (event) => {
      const applied = JSON.parse(event.data) as PushedMessage;
      applied.type = 'approved';
      setEvents([...events, applied]);
      showToast(
        `${applied.name} がルームに参加したいようです。現在あなたの承認を待っています。`,
      );
    },
    [events],
  );

  useEventSourceListener(
    eventSource,
    ['JOINED'],
    (event) => {
      const joined = JSON.parse(event.data) as PushedMessage;
      joined.type = 'joined';
      setEvents([...events, joined]);
      setjoinnedCount(joinnedCount + 1);
      showToast(`${joined.name} がルームに参加しました。`);
    },
    [events],
  );

  return (
    <>
      <UserList users={users} />
      <br />
      {events
        .filter((event) => event.type === 'approved')
        .map((event) => (
          <Box
            key={event.eventId}
            display="flex"
            alignItems="center"
            flexGrow={1}
          >
            <Text key={event.userId}>
              {event.name} が部屋の参加の承認をもとめています
            </Text>
            <Button
              mt={3}
              size="xs"
              colorScheme="teal"
              onClick={() =>
                setUseApproveProp({
                  userId: event.userId,
                  userName: event.name,
                })
              }
            >
              approve
            </Button>
          </Box>
        ))}
    </>
  );
};

export default Room;
