import { httpClient } from '../lib/http/HttpClient';

export type ApprovedUser = {
  userId: string;
  userName: string;
};

const fetchApprove = async (
  userId: string,
  userName: string,
): Promise<ApprovedUser> =>
  httpClient<ApprovedUser>('/api/gm/approve', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
    },
    body: JSON.stringify({
      userId,
      userName,
    }),
  });

export default fetchApprove;
