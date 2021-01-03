import React, { FC, useState } from 'react';
import RoomCreatorComponent from '../../components/pages/RoomCreator';
import useCreateRoom from '../../hooks/use-create-room';

const RoomCreator: FC = () => {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [createRoomProp, setCreateRoomProp] = useState({
    userName: '',
    password: '',
  });

  useCreateRoom(createRoomProp);

  const onUserNameChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    void setUserName(event.target.value);
  const onPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    void setPassword(event.target.value);
  const onClickSubmitButton = () =>
    void setCreateRoomProp({ userName, password });

  const buttonLabel = 'create';

  return (
    <RoomCreatorComponent
      {...{
        userName,
        onUserNameChange,
        password,
        onPasswordChange,
        buttonLabel,
        onClickSubmitButton,
      }}
    />
  );
};

export default RoomCreator;