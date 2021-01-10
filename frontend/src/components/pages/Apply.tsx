import React, { FC } from 'react';
import RoomEntryForm from '../../containers/organisms/RoomEntryForm';
import PageFrame from '../templates/PageFrame';
import ApplyEvent from '../../containers/melecules/ApplyEvent';
import { RoomEntryFormData } from '../organisms/RoomEntryForm';

type Prop = {
  buttonLabel: string;
  onClickSubmitButton: (values: RoomEntryFormData) => void;
  applied: boolean;
  roomId: string;
  userId: string;
};

const Apply: FC<Prop> = ({
  buttonLabel,
  onClickSubmitButton,
  applied,
  roomId,
  userId,
}) => (
  <PageFrame>
    {applied ? (
      <ApplyEvent roomId={roomId} userId={userId} />
    ) : (
      <RoomEntryForm
        {...{
          buttonLabel,
          onClickSubmitButton,
        }}
      />
    )}
  </PageFrame>
);

export default Apply;
