import { httpClient } from '../lib/http/HttpClient';

export type CreateRoomResult = {
  roomId: string;
  roomUrl: string;
  authToken: string;
};

const fetchCreateRoom = async (
  userName: string,
  password: string,
): Promise<CreateRoomResult> => {
  const result = await httpClient<CreateRoomResult>('/api/room/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;',
    },
    body: JSON.stringify({
      userName,
      password,
    }),
  });

  console.log(result);

  return result;
};

export default fetchCreateRoom;
