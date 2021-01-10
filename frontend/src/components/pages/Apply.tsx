import React, { FC } from 'react';
import RoomEntryForm from '../../containers/organisms/RoomEntryForm';
import PageFrame from '../templates/PageFrame';
import ApplyEvent from '../../containers/melecules/ApplyEvent';
import { RoomEntryFormData } from '../organisms/RoomEntryForm';

type Prop = {
  buttonLabel: string;
  onSubmit: (values: RoomEntryFormData) => void;
  applied: boolean;
  roomId: string;
  userId: string;
};

const Apply: FC<Prop> = ({
  buttonLabel,
  onSubmit,
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
          onSubmit,
        }}
      />
    )}
  </PageFrame>
);

export default Apply;
