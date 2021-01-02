import React, { FC, useState } from 'react';
import { useRecoilState } from 'recoil';
import useApprove from '../../hooks/use-approve';
import { eventState, EventState } from '../../states/EventState';
import EventItemComponent from '../../components/molecules/EventItem';

type Prop = {
  event: EventState;
};

const EventItem: FC<Prop> = ({ event }) => {
  const [useApproveProp, setUseApproveProp] = useState({
    userName: '',
    userId: '',
  });
  const [events, setEvents] = useRecoilState(eventState);

  useApprove(useApproveProp);

  const approve = () => {
    setUseApproveProp({
      userName: event.name,
      userId: event.userId,
    });

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

  return <EventItemComponent event={event} onApprove={approve} />;
};

export default EventItem;
