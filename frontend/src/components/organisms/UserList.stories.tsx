import React, { FC } from 'react';
import { ChakraProvider } from '@chakra-ui/react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import UserList, { Prop } from './UserList';
import { RecoilRoot, useSetRecoilState } from 'recoil';
import { userState } from '../../states/UserState';

export default {
  title: 'organisms/UserList',
  component: UserList,
} as Meta;

const TestUserList: FC<Prop> = ({ users }) => {
  const setUserState = useSetRecoilState(userState);
  setUserState({ id: '3', name: 'your-user', role: 0 });

  return <UserList {...{ users }} />;
};

const Template: Story<Prop> = (args) => (
  <ChakraProvider>
    <RecoilRoot>
      <TestUserList users={args.users} />
    </RecoilRoot>
  </ChakraProvider>
);

export const Users = Template.bind({});
Users.args = {
  users: [
    { id: '1', name: 'normal', role: 0 },
    { id: '2', name: 'room-master', role: 1 },
    { id: '3', name: 'your-user', role: 0 },
  ],
};
