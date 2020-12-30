import { httpClient } from '../lib/http/HttpClient';

export type UsersResult = {
  users: User[];
};

export type User = {
  id: string;
  name: string;
  role: 0 | 1;
};

const fetchUsers = async (): Promise<User[]> => {
  const result = await httpClient<UsersResult>('/api/room/users', {
    method: 'GET',
  });

  console.log(result);

  return result.users;
};

export default fetchUsers;
