import {
  useEventSourceListener,
  EventSourceEvent,
} from 'react-use-event-source-ts';

export type PushedMessage = {
  eventId: string;
  userId: string;
  name: string;
  type: 'approved' | 'joined';
};

type Props = {
  eventSource: EventSource | null;
  listener: (e: EventSourceEvent) => void;
  events: PushedMessage[];
};

export const useApprovedEvent = ({ eventSource, listener, events }: Props) => {
  useEventSourceListener(eventSource, ['APPLIED'], listener, [events]);
};

export const useJoinedEvent = ({ eventSource, listener, events }: Props) => {
  useEventSourceListener(eventSource, ['JOINED'], listener, [events]);
};
