import { Text } from '@chakra-ui/react';
import React, { FC, useState } from 'react';
import { useParams } from 'react-router-dom';
import RoomEntryForm from '../../components/pages/RoomEntryForm';
import useApply from '../../hooks/use-apply';

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

  const [isWaitingGmOperation] = useApply({ roomId, ...applyProp });

  // TODO: when isWaitngGmOperationTrue, event subscription is approved

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
      {isWaitingGmOperation && <Text>waiting hosts operation...</Text>}
    </>
  );
};

export default RoomCreator;
