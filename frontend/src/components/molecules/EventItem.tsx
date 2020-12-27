import { Box, Button, Text } from '@chakra-ui/react';
import React, { FC, useState } from 'react';
import { useRecoilState } from 'recoil';
import useApprove from '../../hooks/use-approve';
import { eventState, EventState } from '../../states/EventState';

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

  return (
    <>
      <Box key={event.eventId} display="flex" alignItems="center" flexGrow={1}>
        <Text key={event.userId}>
          {event.name} が部屋の参加の承認をもとめています
        </Text>
        <Button mt={3} size="xs" colorScheme="teal" onClick={() => approve()}>
          approve
        </Button>
      </Box>
    </>
  );
};

export default EventItem;
