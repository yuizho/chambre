import { httpClient } from '../lib/http/HttpClient';

export type RoomStatus = {
  id: string;
  name: string;
  status: 0 | 1 | 2;
};

const fetchRoomStatus = async (roomId: string): Promise<RoomStatus> => {
  const result = await httpClient<RoomStatus>(`/api/room/status/${roomId}`, {
    method: 'GET',
  });

  console.log(result);

  return result;
};

export default fetchRoomStatus;
