import React, { FC } from 'react';
import { PushedMessage } from '../../hooks/use-event';
import EventItem from '../molecules/Eventitem';

type Props = {
  events: PushedMessage[];
};

const EventList: FC<Props> = ({ events }) => {
  return (
    <>
      {events
        .filter((event) => event.type === 'approved')
        .map((event) => (
          <EventItem event={event} />
        ))}
    </>
  );
};

export default EventList;
