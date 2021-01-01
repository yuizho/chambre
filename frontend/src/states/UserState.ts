import { atom } from 'recoil';

export type UserState = {
  id: string;
  name: string;
  role: 0 | 1;
};

export const userState = atom<UserState | undefined>({
  key: 'userState',
  default: undefined,
});
