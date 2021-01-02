import { Box, Button, Text } from '@chakra-ui/react';
import React, { FC } from 'react';
import { EventState } from '../../states/EventState';

type Prop = {
  event: EventState;
  onApprove: () => void;
  onReject: () => void;
};

const EventItem: FC<Prop> = ({ event, onApprove, onReject }) => (
  <>
    <Box key={event.eventId} display="flex" alignItems="center" flexGrow={1}>
      <Text key={event.userId}>
        {event.name} が部屋の参加の承認をもとめています
      </Text>
      <Button mt={3} size="xs" colorScheme="teal" onClick={onApprove}>
        approve
      </Button>
      <Button mt={3} size="xs" colorScheme="red" onClick={onReject}>
        reject
      </Button>
    </Box>
  </>
);

export default EventItem;
