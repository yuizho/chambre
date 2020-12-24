const fetchApprove = async (
  userId: string,
  userName: string,
): Promise<void> => {
  await fetch('/api/gm/approve', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
      // TODO: [csrfHeaderName]: csrfToken
    },
    body: JSON.stringify({
      userId,
      userName,
    }),
  }).then((data) => {
    console.log(data);
  });
};

export default fetchApprove;
