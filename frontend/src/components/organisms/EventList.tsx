import React, { FC } from 'react';
import { EventState } from '../../states/EventState';
import EventItem from '../molecules/Eventitem';

type Props = {
  events: EventState[];
};

const EventList: FC<Props> = ({ events }) => (
  <>
    {events
      .filter((event) => !event.isHandled)
      .map((event) => (
        <EventItem event={event} />
      ))}
  </>
);

export default EventList;
