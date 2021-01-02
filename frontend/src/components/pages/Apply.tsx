import React, { FC } from 'react';
import RoomEntryForm from '../organisms/RoomEntryForm';
import PageFrame from '../templates/PageFrame';

type Prop = {
  userName: string;
  onUserNameChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  password: string;
  onPasswordChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  buttonLabel: string;
  onClickSubmitButton: () => void;
};

const Apply: FC<Prop> = (prop) => (
  <PageFrame>
    <RoomEntryForm {...prop} />
  </PageFrame>
);

export default Apply;
