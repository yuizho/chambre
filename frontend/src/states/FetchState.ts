import { atom } from 'recoil';

export type ErrorState = {
  message: string;
};

export const errorState = atom<ErrorState | undefined>({
  key: 'errorState',
  default: undefined,
});

export const loadingState = atom<boolean>({
  key: 'loadingState',
  default: false,
});
