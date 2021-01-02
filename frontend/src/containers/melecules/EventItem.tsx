import React, { FC, useState } from 'react';
import { useRecoilState } from 'recoil';
import useApprove from '../../hooks/use-approve';
import { eventState, EventState } from '../../states/EventState';
import EventItemComponent from '../../components/molecules/EventItem';
import useReject from '../../hooks/use-reject';

type Prop = {
  event: EventState;
};

const removeHandledEventFromState = (
  events: EventState[],
  event: EventState,
  setEvents: (e: EventState[]) => void,
) => {
  const index = events.findIndex((e) => e.eventId === event?.eventId);
  setEvents([
    ...events.slice(0, index),
    {
      ...event,
      isHandled: true,
    },
    ...events.slice(index + 1),
  ]);
};

const EventItem: FC<Prop> = ({ event }) => {
  const [useApproveProp, setUseApproveProp] = useState({
    userName: '',
    userId: '',
  });
  const [useRejectProp, setUseRejectProp] = useState({ userId: '' });
  const [events, setEvents] = useRecoilState(eventState);

  useApprove(useApproveProp);

  useReject(useRejectProp);

  const approve = () => {
    setUseApproveProp({
      userName: event.name,
      userId: event.userId,
    });

    removeHandledEventFromState(events, event, setEvents);
  };

  const reject = () => {
    setUseRejectProp({ userId: event.userId });

    removeHandledEventFromState(events, event, setEvents);
  };

  return (
    <EventItemComponent event={event} onApprove={approve} onReject={reject} />
  );
};

export default EventItem;
