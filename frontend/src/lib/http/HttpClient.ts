import Cookies from 'js-cookie';
import HttpError from '../error/HttpError';

const handleHttpError = (response: Response) => {
  if (response.ok) {
    return response;
  }
  throw new HttpError(response);
};

function getCsrfToken(): Promise<string> {
  const csrfToken = Cookies.get('XSRF-TOKEN');
  if (csrfToken) {
    return Promise.resolve(csrfToken);
  }

  return fetch('/api/csrf', { method: 'GET' }).then(() => {
    const ct = Cookies.get('XSRF-TOKEN');
    if (ct) {
      return ct;
    }
    throw Error('csrf token required');
  });
}

export const chambreFetch = (
  input: RequestInfo,
  init: RequestInit,
): Promise<Response> =>
  getCsrfToken().then((csrfToken) =>
    fetch(input, {
      ...init,
      headers: {
        ...init.headers,
        ...{ 'X-XSRF-TOKEN': csrfToken },
      },
    })
      .catch((e) => {
        throw Error(e);
      })
      .then(handleHttpError),
  );

export const httpClient = <T>(
  input: RequestInfo,
  init: RequestInit,
  responseHandler: (body: Body) => Promise<any> = (resp) => resp.json(),
): Promise<T> =>
  chambreFetch(input, init)
    .then(responseHandler)
    .then((data: T) => data);
