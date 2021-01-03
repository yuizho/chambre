import React, { FC } from 'react';
import { CircularProgress, Text } from '@chakra-ui/react';

const ApplyEvent: FC = () => (
  <>
    <Text>
      ルームマスターにルームへの参加申請を行いました。ルームマスターの操作を待っています…
    </Text>
    <CircularProgress mt={2} isIndeterminate />
  </>
);

export default ApplyEvent;
