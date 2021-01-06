import React from 'react';
import { ChakraProvider } from '@chakra-ui/react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import ErrorAlert, { Prop } from './ErrorAlert';

export default {
  title: 'atoms/ErrorAlert',
  component: ErrorAlert,
} as Meta;

const Template: Story<Prop> = (args) => (
  <ChakraProvider>
    <ErrorAlert {...args} />
  </ChakraProvider>
);

export const Error = Template.bind({});
Error.args = {
  error: { message: 'this is error message.' },
  onCloseHandler: () => void console.log('close button clicked'),
};

export const NoError = Template.bind({});
NoError.args = {
  error: undefined,
  onCloseHandler: () => void console.log('close button clicked'),
};
