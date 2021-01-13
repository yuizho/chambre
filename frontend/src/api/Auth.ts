import { chambreFetch } from '../lib/http/HttpClient';

const fetchAuth = async (authToken: string): Promise<void> => {
  await chambreFetch('/auth', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;',
    },
    body: `authToken=${authToken}`,
  }).then((data) => {
    console.log(data);
  });
};

export default fetchAuth;
