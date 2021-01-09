import React from 'react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import UserList, { Prop } from './UserList';
import { useSetRecoilState } from 'recoil';
import { userState } from '../../states/UserState';

export default {
  title: 'organisms/UserList',
  component: UserList,
} as Meta;

const Template: Story<Prop> = (args) => {
  const setUserState = useSetRecoilState(userState);
  setUserState({ id: '3', name: 'your-user', role: 0 });

  return <UserList {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  users: [
    { id: '1', name: 'normal', role: 0 },
    { id: '2', name: 'room-master', role: 1 },
    { id: '3', name: 'your-user', role: 0 },
  ],
};

export const BlankList = Template.bind({});
BlankList.args = {
  users: [],
};
