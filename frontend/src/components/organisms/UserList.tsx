import { Flex } from '@chakra-ui/react';
import React, { FC } from 'react';
import { User } from '../../api/Users';
import UserItem from '../../containers/melecules/UserItem';

type Prop = {
  users: User[];
};

const UserList: FC<Prop> = ({ users }) => (
  <Flex direction="row" wrap="wrap">
    {users &&
      users.map((user) => <UserItem key={user.id} user={user} margin={1} />)}
  </Flex>
);

export default UserList;
