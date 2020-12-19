export type RoomCreateResult = {
  roomId: string;
  roomUrl: string;
  authToken: string;
};

const fetchRoomCreate = async (
  userName: string,
  password: string,
): Promise<RoomCreateResult> => {
  if (!userName || !password) {
    return {
      roomId: '',
      roomUrl: '',
      authToken: '',
    };
  }

  const result = await fetch('http://localhost:8080/api/room/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;',
      // TODO: how to handle csrf [csrfHeaderName]: csrfToken,
    },
    body: JSON.stringify({
      userName,
      password,
    }),
  })
    .then((response) => response.json())
    .then((data: RoomCreateResult) => data);

  console.log(result);

  return result;
};

export default fetchRoomCreate;
