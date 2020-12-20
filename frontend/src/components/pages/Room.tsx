import { Avatar, Text, Flex, Box, Badge } from '@chakra-ui/react';
import React from 'react';
import { useParams } from 'react-router-dom';
import useUsers from '../../hooks/use-users';

type ParamType = {
  roomId: string;
};

const Room = () => {
  const { roomId } = useParams<ParamType>();

  const [users] = useUsers({ roomId });

  return (
    <>
      <Flex>
        {users.map((user) => (
          <Box
            d="flex"
            p={2}
            w="6em"
            h="6em"
            borderWidth="1px"
            borderRadius="lg"
            overflow="hidden"
            flexDirection="column"
            justifyContent="center"
            alignItems="center"
          >
            <Avatar size="sm" />
            {user.role === 1 ? <Badge colorScheme="green">Host</Badge> : <></>}
            <Text mt={1} fontWeight="bold">
              {user.name}
            </Text>
          </Box>
        ))}
      </Flex>
    </>
  );
};

export default Room;
