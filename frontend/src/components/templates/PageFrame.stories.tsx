import React from 'react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import PageFrame from './PageFrame';

export default {
  title: 'templates/PageFrame',
  component: PageFrame,
} as Meta;

const Template: Story = () => <PageFrame>child components</PageFrame>;

export const Default = Template.bind({});
