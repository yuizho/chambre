import {
  Alert,
  AlertDescription,
  AlertIcon,
  CloseButton,
} from '@chakra-ui/react';
import React, { FC } from 'react';
import { ErrorState } from '../../states/FetchState';

type Prop = {
  error: ErrorState | undefined;
  onCloseHandler: () => void;
};

const ErrorAlert: FC<Prop> = ({ error, onCloseHandler }) => (
  <>
    {error && (
      <Alert status="error">
        <AlertIcon />
        <AlertDescription>{error.message}</AlertDescription>
        <CloseButton
          onClick={onCloseHandler}
          position="absolute"
          right="8px"
          top="8px"
        />
      </Alert>
    )}
  </>
);

export default ErrorAlert;
