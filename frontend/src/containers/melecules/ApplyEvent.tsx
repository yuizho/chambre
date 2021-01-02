import React, { FC, useState } from 'react';
import {
  useEventSource,
  useEventSourceListener,
} from 'react-use-event-source-ts';
import ApplyEventComponent from '../../components/molecules/ApplyEvent';
import useAuth from '../../hooks/use-auth';

type Props = {
  roomId: string;
  userId: string;
};

const ApplyEvent: FC<Props> = ({ roomId, userId }) => {
  const [authToken, setAuthToken] = useState('');

  const [eventSource] = useEventSource(
    `/api/subscribe/unapproved?roomId=${roomId}&userId=${userId}`,
  );
  useEventSourceListener(
    eventSource,
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

  useAuth({ authToken, roomId });

  return <ApplyEventComponent />;
};

export default ApplyEvent;
