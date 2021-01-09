import React from 'react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import ErrorAlert, { Prop } from './ErrorAlert';

export default {
  title: 'atoms/ErrorAlert',
  component: ErrorAlert,
} as Meta;

const Template: Story<Prop> = (args) => <ErrorAlert {...args} />;

export const Default = Template.bind({});
Default.args = {
  error: undefined,
  onCloseHandler: () => void console.log('close button clicked'),
};

export const Error = Template.bind({});
Error.args = {
  error: { message: 'this is error message.' },
  onCloseHandler: () => void alert('close button clicked'),
};
