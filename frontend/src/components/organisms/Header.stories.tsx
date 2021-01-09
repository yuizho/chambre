import React from 'react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import Header, { Props } from './Header';

export default {
  title: 'organisms/Header',
  component: Header,
} as Meta;

const Template: Story<Props> = (args) => <Header {...args} />;

export const Default = Template.bind({});

export const Loading = Template.bind({});
Loading.args = {
  isLoading: true,
};

export const Error = Template.bind({});
Error.args = {
  error: { message: 'error message' },
  setError: (e) => void alert(`${e} is set to error.`),
};
