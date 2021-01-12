import React, { FC } from 'react';
import { EventState } from '../../states/EventState';
import EventItem from '../../containers/melecules/EventItem';

type Props = {
  events: EventState[];
};

const EventList: FC<Props> = ({ events }) => (
  <>
    {events
      .filter((event) => !event.isHandled)
      .map((event) => (
        <EventItem key={event.eventId} event={event} />
      ))}
  </>
);

export default EventList;
