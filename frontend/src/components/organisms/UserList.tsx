import { Flex } from '@chakra-ui/react';
import React, { FC } from 'react';
import { User } from '../../api/Users';
import UserItem from '../molecules/UserItem';

type Prop = {
  users: User[];
};

const UserList: FC<Prop> = ({ users }) => (
  <Flex>
    {users.map((user) => (
      <UserItem user={user} />
    ))}
  </Flex>
);

export default UserList;
