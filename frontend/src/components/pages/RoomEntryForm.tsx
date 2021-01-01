import { Button, Container, Input } from '@chakra-ui/react';
import React, { FC } from 'react';

type Prop = {
  userName: string;
  onUserNameChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  password: string;
  onPasswordChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  buttonLabel: string;
  onClickSubmitButton: () => void;
};

const RoomEntryForm: FC<Prop> = ({
  userName,
  onUserNameChange,
  password,
  onPasswordChange,
  buttonLabel,
  onClickSubmitButton,
}) => {
  return (
    <>
      <Container mt={4} pt={4} pb={4} borderWidth="1px" borderRadius="lg">
        <Input
          mt={3}
          value={userName}
          placeholder="your name"
          onChange={onUserNameChange}
        />
        <Input
          type="password"
          mt={3}
          value={password}
          placeholder="password (6 digits)"
          onChange={onPasswordChange}
        />
        <Button mt={3} colorScheme="teal" onClick={onClickSubmitButton}>
          {buttonLabel}
        </Button>
      </Container>
    </>
  );
};

export default RoomEntryForm;
