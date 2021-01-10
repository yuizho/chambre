import React, { FC, useState } from 'react';
import { useParams } from 'react-router-dom';
import useApply from '../../hooks/use-apply';
import ApplyComponent from '../../components/pages/Apply';
import { RoomEntryFormData } from '../../components/organisms/RoomEntryForm';

type ParamType = {
  roomId: string;
};

const RoomCreator: FC = () => {
  const { roomId } = useParams<ParamType>();
  const [applyProp, setApplyProp] = useState({
    userName: '',
    password: '',
  });

  const { applied, userId } = useApply({ roomId, ...applyProp });

  const onClickSubmitButton = ({ userName, password }: RoomEntryFormData) =>
    void setApplyProp({ userName, password });

  const buttonLabel = 'apply';

  return (
    <ApplyComponent
      {...{
        buttonLabel,
        onClickSubmitButton,
        applied,
        roomId,
        userId,
      }}
    />
  );
};

export default RoomCreator;
