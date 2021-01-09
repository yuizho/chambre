import React from 'react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import UserItem, { Prop } from './UserItem';

export default {
  title: 'molecules/UserItem',
  component: UserItem,
} as Meta;

const Template: Story<Prop> = (args) => <UserItem {...args} />;

export const NormalUser = Template.bind({});
NormalUser.args = {
  user: { id: '1', name: 'normal', role: 0 },
  key: '1',
  margin: 1,
  isYou: false,
};

export const RoomMaster = Template.bind({});
RoomMaster.args = {
  user: { id: '1', name: 'room-master', role: 1 },
  key: '1',
  margin: 1,
  isYou: false,
};

export const YourUser = Template.bind({});
YourUser.args = {
  user: { id: '1', name: 'your-user', role: 0 },
  key: '1',
  margin: 1,
  isYou: true,
};
