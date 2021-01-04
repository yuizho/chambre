import React, { FC } from 'react';
import { useForm } from 'react-hook-form';
import RoomTntryFormComponent from '../../components/organisms/RoomEntryForm';

type Prop = {
  userName: string;
  onUserNameChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  password: string;
  onPasswordChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  buttonLabel: string;
  onClickSubmitButton: () => void;
};

const RoomEntryForm: FC<Prop> = ({
  userName,
  onUserNameChange,
  password,
  onPasswordChange,
  buttonLabel,
  onClickSubmitButton,
}) => {
  // reference: https://github.com/react-hook-form/react-hook-form/issues/2887
  /* eslint-disable  @typescript-eslint/unbound-method */
  const formMethods = useForm();

  return (
    <RoomTntryFormComponent
      {...{
        userName,
        onUserNameChange,
        password,
        onPasswordChange,
        buttonLabel,
        onClickSubmitButton,
        formMethods,
      }}
    />
  );
};

export default RoomEntryForm;
