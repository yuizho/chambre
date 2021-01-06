import React from 'react';
import { ChakraProvider } from '@chakra-ui/react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import ProgressBar, { Prop } from './ProgressBar';

export default {
  title: 'atoms/ProgressBar',
  component: ProgressBar,
} as Meta;

const Template: Story<Prop> = (args) => (
  <ChakraProvider>
    <ProgressBar {...args} />
  </ChakraProvider>
);

export const Loading = Template.bind({});
Loading.args = {
  isLoading: true,
};

export const UnLoading = Template.bind({});
UnLoading.args = {
  isLoading: false,
};
