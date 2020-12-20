import { Button, Container, Input } from '@chakra-ui/react';
import React, { FC, useState } from 'react';
import useCreateRoom from '../../hooks/use-create-room';

const RoomCreator: FC = () => {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [createRoomProp, setCreateRoomProp] = useState({
    userName: '',
    password: '',
  });

  useCreateRoom(createRoomProp);

  return (
    <>
      <Container mt={4} pt={4} pb={4} borderWidth="1px" borderRadius="lg">
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
          onClick={() => setCreateRoomProp({ userName, password })}
        >
          create
        </Button>
      </Container>
    </>
  );
};

export default RoomCreator;
