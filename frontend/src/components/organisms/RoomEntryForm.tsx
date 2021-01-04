import {
  Button,
  Container,
  FormControl,
  FormErrorMessage,
  Input,
} from '@chakra-ui/react';
import React, { FC } from 'react';
import { UseFormMethods } from 'react-hook-form/dist/types/form';

type Prop = {
  userName: string;
  onUserNameChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  password: string;
  onPasswordChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  buttonLabel: string;
  onClickSubmitButton: () => void;
  formMethods: UseFormMethods;
};

const RoomEntryForm: FC<Prop> = ({
  userName,
  onUserNameChange,
  password,
  onPasswordChange,
  buttonLabel,
  onClickSubmitButton,
  formMethods,
}) => {
  // reference: https://github.com/react-hook-form/react-hook-form/issues/2887
  /* eslint-disable  @typescript-eslint/unbound-method */
  const { handleSubmit, errors, register } = formMethods;

  return (
    <>
      <Container mt={4} pt={4} pb={4} borderWidth="1px" borderRadius="lg">
        <FormControl isInvalid={errors.userName as boolean}>
          <Input
            name="userName"
            mt={3}
            value={userName}
            placeholder="your name"
            onChange={onUserNameChange}
            ref={register({ required: true })}
          />
          <FormErrorMessage>
            {errors.userName && <span>This field is required</span>}
          </FormErrorMessage>
        </FormControl>

        <FormControl isInvalid={errors.password as boolean}>
          <Input
            name="password"
            type="password"
            mt={3}
            value={password}
            placeholder="password (6 digits)"
            onChange={onPasswordChange}
            ref={register({ required: true })}
          />
          <FormErrorMessage>
            {errors.password && <span>This field is required</span>}
          </FormErrorMessage>
        </FormControl>

        <Button
          mt={3}
          colorScheme="teal"
          onClick={handleSubmit(onClickSubmitButton)}
        >
          {buttonLabel}
        </Button>
      </Container>
    </>
  );
};

export default RoomEntryForm;
