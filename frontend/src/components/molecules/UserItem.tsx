import { Avatar, Badge, Box, Text } from '@chakra-ui/react';
import React, { FC } from 'react';
import { User } from '../../api/Users';

type Prop = {
  user: User;
  key: string;
};

const UserItem: FC<Prop> = ({ user }) => (
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
);

export default UserItem;
