export type UsersResult = {
  users: User[];
};

export type User = {
  id: string;
  name: string;
  role: 0 | 1;
};

const fetchUsers = async (): Promise<User[]> => {
  const result = await fetch('/api/room/users', {
    method: 'GET',
  })
    .then((response) => response.json())
    .then((data: UsersResult) => data);

  console.log(result);

  return result.users;
};

export default fetchUsers;
