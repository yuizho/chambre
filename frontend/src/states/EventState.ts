import { atom } from 'recoil';

type EventType = 'approved' | 'joined';

export type EventState = {
  roomId: string;
  eventId: string;
  userId: string;
  name: string;
  type: EventType;
  isHandled: boolean;
};

export const eventState = atom<EventState[]>({
  key: 'eventState',
  default: [],
});
