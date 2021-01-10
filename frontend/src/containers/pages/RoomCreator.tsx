import React, { FC, useState } from 'react';
import { RoomEntryFormData } from '../../components/organisms/RoomEntryForm';
import RoomCreatorComponent from '../../components/pages/RoomCreator';
import useCreateRoom from '../../hooks/use-create-room';

const RoomCreator: FC = () => {
  const [createRoomProp, setCreateRoomProp] = useState({
    userName: '',
    password: '',
  });

  useCreateRoom(createRoomProp);

  const onClickSubmitButton = ({ userName, password }: RoomEntryFormData) =>
    void setCreateRoomProp({ userName, password });

  const buttonLabel = 'create';

  return (
    <RoomCreatorComponent
      {...{
        buttonLabel,
        onClickSubmitButton,
      }}
    />
  );
};

export default RoomCreator;
