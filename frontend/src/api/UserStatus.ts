import { httpClient } from '../lib/http/HttpClient';

export type UserStatus = {
  id: string;
  name: string;
  role: 0 | 1;
};

const fetchUserStatus = async (): Promise<UserStatus> => {
  const result = await httpClient<UserStatus>('/api/user/status', {
    method: 'GET',
  });

  console.log(result);

  return result;
};

export default fetchUserStatus;
