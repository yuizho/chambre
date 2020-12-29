export type ApprovedUser = {
  userId: string;
  userName: string;
};

const fetchApprove = async (
  userId: string,
  userName: string,
): Promise<ApprovedUser> =>
  fetch('/api/gm/approve', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
      // TODO: [csrfHeaderName]: csrfToken
    },
    body: JSON.stringify({
      userId,
      userName,
    }),
  })
    .then((response) => response.json())
    .then((data: ApprovedUser) => data);

export default fetchApprove;
