import React, { FC, useEffect, useRef, useState } from 'react';
import { useEventSourceListener } from 'react-use-event-source-ts';
import ApplyEventComponent from '../../components/molecules/ApplyEvent';
import useAuth from '../../hooks/use-auth';

type Props = {
  roomId: string;
  userId: string;
};

const ApplyEvent: FC<Props> = ({ roomId, userId }) => {
  const [authToken, setAuthToken] = useState('');

  const eventSource = useRef<EventSource>(
    new EventSource(
      `/api/subscribe/unapproved?roomId=${roomId}&userId=${userId}`,
    ),
  );

  useEventSourceListener(
    eventSource.current,
    ['APPROVED'],
    (event) => {
      const approved = JSON.parse(event.data) as {
        eventId: string;
        token: string;
      };
      console.log(`Approved: ${approved.token}`);
      setAuthToken(approved.token);
    },
    [authToken],
  );

  // close EventSource when this page is closed.
  useEffect(
    () => () => {
      console.log('close EventSource of ApplyEvent');
      eventSource.current.close();
    },
    [],
  );

  useAuth({ authToken, roomId });

  return <ApplyEventComponent />;
};

export default ApplyEvent;
