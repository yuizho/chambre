import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useEventSource } from 'react-use-event-source-ts';
import { useToast } from '@chakra-ui/react';
import useUsers from '../../hooks/use-users';
import UserList from '../organisms/UserList';
import {
  PushedMessage,
  useApprovedEvent,
  useJoinedEvent,
} from '../../hooks/use-event';
import EventList from '../organisms/EventList';

type ParamType = {
  roomId: string;
};

const Room = () => {
  const { roomId } = useParams<ParamType>();
  const [events, setEvents] = useState<PushedMessage[]>([]);
  const [joinnedCount, setjoinnedCount] = useState(1);
  const [users] = useUsers({ roomId, joinnedCount });
  const toast = useToast();

  const showToast = (description: string) => {
    toast({
      description,
    });
  };

  const [eventSource] = useEventSource('/api/subscribe/approved', true);
  useApprovedEvent({
    eventSource,
    listener: (event) => {
      const applied = JSON.parse(event.data) as PushedMessage;
      applied.type = 'approved';
      setEvents([...events, applied]);
      showToast(
        `${applied.name} がルームに参加したいようです。現在あなたの承認を待っています。`,
      );
    },
    events,
  });
  useJoinedEvent({
    eventSource,
    listener: (event) => {
      const joined = JSON.parse(event.data) as PushedMessage;
      joined.type = 'joined';
      setEvents([...events, joined]);
      setjoinnedCount(joinnedCount + 1);
      showToast(`${joined.name} がルームに参加しました。`);
    },
    events,
  });

  return (
    <>
      <UserList users={users} />
      <br />
      <EventList events={events} />
    </>
  );
};

export default Room;
