import {
  useEventSourceListener,
  EventSourceEvent,
} from 'react-use-event-source-ts';
import { EventState } from '../states/EventState';

type Props = {
  eventSource: EventSource | null;
  listener: (e: EventSourceEvent) => void;
  events: EventState[];
};

export const useApprovedEvent = ({ eventSource, listener, events }: Props) => {
  useEventSourceListener(eventSource, ['APPLIED'], listener, [events]);
};

export const useJoinedEvent = ({ eventSource, listener, events }: Props) => {
  useEventSourceListener(eventSource, ['JOINED'], listener, [events]);
};
