import { Box, Button, Text } from '@chakra-ui/react';
import React, { FC, useState } from 'react';
import useApprove from '../../hooks/use-approve';
import { EventState } from '../../states/EventState';

type Prop = {
  event: EventState;
};

const EventItem: FC<Prop> = ({ event }) => {
  const [useApproveProp, setUseApproveProp] = useState({
    userId: '',
    userName: '',
  });

  useApprove(useApproveProp);

  return (
    <>
      <Box key={event.eventId} display="flex" alignItems="center" flexGrow={1}>
        <Text key={event.userId}>
          {event.name} が部屋の参加の承認をもとめています
        </Text>
        <Button
          mt={3}
          size="xs"
          colorScheme="teal"
          onClick={() =>
            setUseApproveProp({
              userId: event.userId,
              userName: event.name,
            })
          }
        >
          approve
        </Button>
      </Box>
    </>
  );
};

export default EventItem;
