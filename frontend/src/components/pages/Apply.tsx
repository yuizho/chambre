import React, { FC } from 'react';
import RoomEntryForm from '../../containers/organisms/RoomEntryForm';
import PageFrame from '../templates/PageFrame';
import ApplyEvent from '../../containers/melecules/ApplyEvent';

type Prop = {
  userName: string;
  onUserNameChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  password: string;
  onPasswordChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  buttonLabel: string;
  onClickSubmitButton: () => void;
  applied: boolean;
  roomId: string;
  userId: string;
};

const Apply: FC<Prop> = ({
  userName,
  onUserNameChange,
  password,
  onPasswordChange,
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
          userName,
          onUserNameChange,
          password,
          onPasswordChange,
          buttonLabel,
          onClickSubmitButton,
        }}
      />
    )}
  </PageFrame>
);

export default Apply;
