import { httpClient } from '../lib/http/HttpClient';

const fetchReject = async (userId: string): Promise<void> =>
  httpClient<void>('/api/gm/reject', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
    body: JSON.stringify({
      userId,
    }),
  });

export default fetchReject;
