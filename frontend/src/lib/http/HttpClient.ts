const handleHttpError = (response: Response) => {
  if (response.ok) {
    return response;
  }
  throw Error(`Http status error: ${response.status}`);
};

export const chambreFetch = (
  input: RequestInfo,
  init?: RequestInit,
): Promise<Response> =>
  fetch(input, init)
    .catch((e) => {
      throw Error(e);
    })
    .then(handleHttpError);

export const httpClient = <T>(
  input: RequestInfo,
  init?: RequestInit,
  responseHandler: (body: Body) => Promise<any> = (resp) => resp.json(),
): Promise<T> =>
  chambreFetch(input, init)
    .then(responseHandler)
    .then((data: T) => data);
