import React, { FC } from 'react';
import { EventState } from '../../states/EventState';
import EventItem from '../molecules/Eventitem';

type Props = {
  events: EventState[];
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
