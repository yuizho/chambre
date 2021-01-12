import React, { FC } from 'react';
import { useRecoilValue } from 'recoil';
import { User } from '../../api/Users';
import UserItemComponent from '../../components/molecules/UserItem';
import { userState } from '../../states/UserState';

type Prop = {
  user: User;
  margin: number;
};

const UserItem: FC<Prop> = ({ user, margin }) => {
  const currentUser = useRecoilValue(userState);

  return (
    <UserItemComponent
      {...{ user, margin, isYou: user.id === currentUser?.id }}
    />
  );
};

export default UserItem;
