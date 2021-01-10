import React, { FC } from 'react';
import RoomEntryForm from '../../containers/organisms/RoomEntryForm';
import { RoomEntryFormData } from '../organisms/RoomEntryForm';
import PageFrame from '../templates/PageFrame';

type Prop = {
  buttonLabel: string;
  onClickSubmitButton: (values: RoomEntryFormData) => void;
};

const RoomCreator: FC<Prop> = ({ buttonLabel, onClickSubmitButton }) => (
  <PageFrame>
    <RoomEntryForm
      {...{
        buttonLabel,
        onClickSubmitButton,
      }}
    />
  </PageFrame>
);

export default RoomCreator;
