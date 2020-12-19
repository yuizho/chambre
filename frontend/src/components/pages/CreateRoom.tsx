import { Button, Container, Heading, Input } from '@chakra-ui/react';
import React, { FC, useState } from 'react';
import useRoomCreate from '../../hooks/use-room-create';

const CreateRoom: FC = () => {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [creatingRoomProp, setCreatingRoomProp] = useState({
    userName: '',
    password: '',
  });

  useRoomCreate(creatingRoomProp);

  return (
    <>
      <Heading>create room</Heading>
      <Container pt={4} pb={4} borderWidth="1px" borderRadius="lg">
        <Input
          mt={3}
          value={userName}
          placeholder="your name"
          onChange={(event) => setUserName(event.target.value)}
        />
        <Input
          type="password"
          mt={3}
          value={password}
          placeholder="password (6 digits)"
          onChange={(event) => setPassword(event.target.value)}
        />
        <Button
          mt={3}
          colorScheme="teal"
          onClick={() => setCreatingRoomProp({ userName, password })}
        >
          create
        </Button>
      </Container>
    </>
  );
};

export default CreateRoom;
