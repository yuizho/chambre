import React, { FC } from 'react';
import { useRecoilState } from 'recoil';
import { useToast } from '@chakra-ui/react';
import { useEventSource } from 'react-use-event-source-ts';
import { eventState, EventState } from '../../states/EventState';
import EventListComponent from '../../components/organisms/EventList';
import { useAppliedEvent, useJoinedEvent } from '../../hooks/use-event';

type Props = {
  roomId: string;
  setJoinnedCount: (f: (n: number) => number) => void;
};

const EventList: FC<Props> = ({ roomId, setJoinnedCount }) => {
  const [events, setEvents] = useRecoilState(eventState);

  const toast = useToast();

  const showToast = (description: string) => {
    toast({
      description,
    });
  };

  const [eventSource] = useEventSource('/api/subscribe/approved', true);
  useAppliedEvent({
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
      setJoinnedCount((n) => n + 1);
      showToast(`${joined.name} がルームに参加しました。`);
    },
    events,
  });

  return (
    <EventListComponent events={events.filter((e) => e.roomId === roomId)} />
  );
};

export default EventList;
