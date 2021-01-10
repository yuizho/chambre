import React from 'react';
// also exported from '@storybook/react' if you can deal with breaking changes in 6.1
import { Story, Meta } from '@storybook/react/types-6-0';
import { useForm } from 'react-hook-form';
import RoomEntryForm, { RoomEntryFormData } from './RoomEntryForm';

export default {
  title: 'organisms/RoomEntryForm',
  component: RoomEntryForm,
} as Meta;

const Template: Story<{
  buttonLabel: string;
  onSubmit: (values: RoomEntryFormData) => void;
}> = (args) => {
  // TODO: how to mock this methods?
  const formMethods = useForm<RoomEntryFormData>();

  return (
    <RoomEntryForm
      buttonLabel={args.buttonLabel}
      onSubmit={args.onSubmit}
      formMethods={formMethods}
    />
  );
};

export const Default = Template.bind({});
Default.args = {
  buttonLabel: 'create',
  onSubmit: (values: RoomEntryFormData) => void alert(values),
};
