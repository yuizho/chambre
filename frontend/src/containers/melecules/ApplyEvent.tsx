import { useToast } from '@chakra-ui/react';
import React, { FC, useEffect, useRef, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useEventSourceListener } from 'react-use-event-source-ts';
import ApplyEventComponent from '../../components/molecules/ApplyEvent';
import useAuth from '../../hooks/use-auth';

type Props = {
  roomId: string;
  userId: string;
};

const ApplyEvent: FC<Props> = ({ roomId, userId }) => {
  const history = useHistory();
  const [authToken, setAuthToken] = useState('');
  const toast = useToast();

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

  useEventSourceListener(
    eventSource.current,
    ['REJECTED'],
    () => {
      console.log('rejected');
      toast({
        description:
          'ルームへの参加申請が却下されました。ルームマスターと直接連絡をとってから再度やり直して見てください。',
      });
      history.push('/');
    },
    [],
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
