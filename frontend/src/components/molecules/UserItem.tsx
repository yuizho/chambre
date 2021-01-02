import { Avatar, Box, Text, Tooltip } from '@chakra-ui/react';
import React, { FC } from 'react';
import { User } from '../../api/Users';

type Prop = {
  user: User;
  key: string;
  margin: number;
  isYou: boolean;
};

const UserItem: FC<Prop> = ({ user, key, margin, isYou }) => {
  const userItemBox = (
    <Box
      key={key}
      d="flex"
      p={2}
      m={margin}
      w="6em"
      h="6em"
      borderWidth="1px"
      borderRadius="lg"
      overflow="hidden"
      flexDirection="column"
      justifyContent="center"
      alignItems="center"
    >
      <Avatar size="sm" bg={user.role === 1 ? 'teal.500' : 'gray.500'} />
      <Text mt={1} fontWeight="bold">
        {user.name}
      </Text>
      {isYou && <Text>(You)</Text>}
    </Box>
  );

  return user.role === 1 ? (
    <Tooltip label="Room Host" hasArrow>
      {userItemBox}
    </Tooltip>
  ) : (
    userItemBox
  );
};

export default UserItem;
