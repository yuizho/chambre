import { httpClient } from '../lib/http/HttpClient';

export type ApplyResult = {
  userId: string;
  userName: string;
  role: 0 | 1;
};

const fetchApply = async (
  roomId: string,
  userName: string,
  password: string,
): Promise<ApplyResult> => {
  const result = await httpClient<ApplyResult>('/api/user/apply', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;',
    },
    body: JSON.stringify({
      roomId,
      userName,
      roomKey: password,
    }),
  });

  console.log(result);

  return result;
};

export default fetchApply;
