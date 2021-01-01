import {
  useEventSourceListener,
  EventSourceEvent,
} from 'react-use-event-source-ts';
import { EventState } from '../states/EventState';

type ApprovedProps = {
  eventSource: EventSource | null;
  listener: (e: EventSourceEvent) => void;
  events: EventState[];
};

export const useAppliedEvent = ({
  eventSource,
  listener,
  events,
}: ApprovedProps) => {
  useEventSourceListener(eventSource, ['APPLIED'], listener, [events]);
};

export const useJoinedEvent = ({
  eventSource,
  listener,
  events,
}: ApprovedProps) => {
  useEventSourceListener(eventSource, ['JOINED'], listener, [events]);
};

type UnApprovedProps = {
  eventSource: EventSource | null;
  listener: (e: EventSourceEvent) => void;
};

export const useApprovedEvent = ({
  eventSource,
  listener,
}: UnApprovedProps) => {
  useEventSourceListener(eventSource, ['APPROVED'], listener);
};
