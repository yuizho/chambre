import React, { FC } from 'react';
import RoomEntryForm from '../../containers/organisms/RoomEntryForm';
import { RoomEntryFormData } from '../organisms/RoomEntryForm';
import PageFrame from '../templates/PageFrame';

type Prop = {
  buttonLabel: string;
  onSubmit: (values: RoomEntryFormData) => void;
};

const RoomCreator: FC<Prop> = ({ buttonLabel, onSubmit }) => (
  <PageFrame>
    <RoomEntryForm
      {...{
        buttonLabel,
        onSubmit,
      }}
    />
  </PageFrame>
);

export default RoomCreator;
