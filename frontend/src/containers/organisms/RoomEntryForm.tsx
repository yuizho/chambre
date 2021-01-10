import React, { FC } from 'react';
import { useForm } from 'react-hook-form';
import RoomTntryFormComponent, {
  RoomEntryFormData,
} from '../../components/organisms/RoomEntryForm';

type Prop = {
  buttonLabel: string;
  onClickSubmitButton: (values: RoomEntryFormData) => void;
};

const RoomEntryForm: FC<Prop> = ({ buttonLabel, onClickSubmitButton }) => {
  // reference: https://github.com/react-hook-form/react-hook-form/issues/2887
  /* eslint-disable  @typescript-eslint/unbound-method */
  const formMethods = useForm<RoomEntryFormData>({ mode: 'onBlur' });

  return (
    <RoomTntryFormComponent
      {...{
        buttonLabel,
        onClickSubmitButton,
        formMethods,
      }}
    />
  );
};

export default RoomEntryForm;
