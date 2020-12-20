const fetchAuth = async (authToken: string): Promise<void> => {
  await fetch('/auth', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;',
      // TODO: [csrfHeaderName]: csrfToken
    },
    body: `authToken=${authToken}`,
  }).then((data) => {
    console.log(data);
  });
};

export default fetchAuth;
