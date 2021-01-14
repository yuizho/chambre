import {
  Button,
  Container,
  FormControl,
  FormErrorMessage,
  Input,
} from '@chakra-ui/react';
import React, { FC } from 'react';
import { UseFormMethods } from 'react-hook-form/dist/types/form';

export type RoomEntryFormData = {
  userName: string;
  password: string;
};

type Prop = {
  buttonLabel: string;
  onSubmit: (values: RoomEntryFormData) => void;
  formMethods: UseFormMethods<RoomEntryFormData>;
};

const RoomEntryForm: FC<Prop> = ({ buttonLabel, onSubmit, formMethods }) => {
  // reference: https://github.com/react-hook-form/react-hook-form/issues/2887
  /* eslint-disable  @typescript-eslint/unbound-method */
  const { handleSubmit, errors, register } = formMethods;

  return (
    <>
      <Container mt={4} pt={4} pb={4} borderWidth="1px" borderRadius="lg">
        <FormControl isInvalid={!!errors.userName}>
          <Input
            name="userName"
            mt={3}
            placeholder="あなたのユーザ名"
            ref={register({
              required: 'ユーザ名を入力してください',
              pattern: {
                value: /[a-zA-Z0-9]{1,20}/,
                message: 'ユーザ名は半角英数字20文字以下で入力してください',
              },
            })}
          />
          <FormErrorMessage>{errors?.userName?.message}</FormErrorMessage>
        </FormControl>

        <FormControl isInvalid={!!errors.password}>
          <Input
            name="password"
            type="password"
            mt={3}
            placeholder="roomのパスワード"
            ref={register({
              required: 'roomのパスワードを入力してください',
              pattern: {
                value: /[a-zA-Z0-9]{6}/,
                message: 'roomのパスワードは半角英数字6文字で入力してください',
              },
            })}
          />
          <FormErrorMessage>{errors?.password?.message}</FormErrorMessage>
        </FormControl>

        <Button mt={3} colorScheme="teal" onClick={handleSubmit(onSubmit)}>
          {buttonLabel}
        </Button>
      </Container>
    </>
  );
};

export default RoomEntryForm;
