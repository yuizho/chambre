import React, { FC } from 'react';
import { useRecoilValue } from 'recoil';
import { User } from '../../api/Users';
import UserItemComponent from '../../components/molecules/UserItem';
import { userState } from '../../states/UserState';

type Prop = {
  user: User;
  key: string;
  margin: number;
};

const UserItem: FC<Prop> = ({ user, key, margin }) => {
  const currentUser = useRecoilValue(userState);
  return (
    <UserItemComponent
      {...{ user, key, margin, isYou: user.id === currentUser?.id }}
    />
  );
};

export default UserItem;
