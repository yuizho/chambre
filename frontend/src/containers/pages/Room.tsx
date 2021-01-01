import React, { FC, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useEventSource } from 'react-use-event-source-ts';
import { useToast } from '@chakra-ui/react';
import { useRecoilState } from 'recoil';
import useUsers from '../../hooks/use-users';
import { useApprovedEvent, useJoinedEvent } from '../../hooks/use-event';
import { EventState, eventState } from '../../states/EventState';
import RoomComponent from '../../components/pages/Room';

type ParamType = {
  roomId: string;
};

const Room: FC = () => {
  const { roomId } = useParams<ParamType>();
  const [events, setEvents] = useRecoilState(eventState);
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
      const applied = JSON.parse(event.data) as EventState;
      if (events.find((e) => applied.eventId === e.eventId && e.isHandled)) {
        return;
      }
      applied.type = 'approved';
      applied.roomId = roomId;
      setEvents([
        ...events.filter((e) => e.eventId !== applied.eventId),
        applied,
      ]);
      showToast(
        `${applied.name} がルームに参加したいようです。現在あなたの承認を待っています。`,
      );
    },
    events,
  });

  useJoinedEvent({
    eventSource,
    listener: (event) => {
      const joined = JSON.parse(event.data) as EventState;
      if (events.find((e) => joined.eventId === e.eventId && e.isHandled)) {
        return;
      }
      joined.type = 'joined';
      joined.roomId = roomId;
      joined.isHandled = true;
      setEvents([
        ...events.filter((e) => e.eventId !== joined.eventId),
        joined,
      ]);
      setjoinnedCount(joinnedCount + 1);
      showToast(`${joined.name} がルームに参加しました。`);
    },
    events,
  });

  return <RoomComponent {...{ roomId, users, events }} />;
};

export default Room;
