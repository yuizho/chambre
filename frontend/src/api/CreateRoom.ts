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
  if (!userName || !password) {
    return {
      roomId: '',
      roomUrl: '',
      authToken: '',
    };
  }

  const result = await httpClient<CreateRoomResult>('/api/room/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;',
      // TODO: how to handle csrf [csrfHeaderName]: csrfToken,
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
