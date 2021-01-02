import React, { FC, useState } from 'react';
import { useParams } from 'react-router-dom';
import RoomEntryForm from '../../components/pages/RoomEntryForm';
import useApply from '../../hooks/use-apply';
import ApplyEvent from '../melecules/ApplyEvent';

type ParamType = {
  roomId: string;
};

const RoomCreator: FC = () => {
  const { roomId } = useParams<ParamType>();
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [applyProp, setApplyProp] = useState({
    userName: '',
    password: '',
  });

  const { applied, userId } = useApply({ roomId, ...applyProp });

  const onUserNameChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    void setUserName(event.target.value);
  const onPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) =>
    void setPassword(event.target.value);
  const onClickSubmitButton = () => void setApplyProp({ userName, password });

  const buttonLabel = 'apply';

  return (
    <>
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
      {applied && <ApplyEvent roomId={roomId} userId={userId} />}
    </>
  );
};

export default RoomCreator;
