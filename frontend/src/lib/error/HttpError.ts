class HttpError extends Error {
  response: Response;
  constructor(response: Response) {
    // Set the message to the status text, such as Unauthorized,
    // with some fallbacks. This message should never be undefined.
    super(
      response.statusText ||
        String(
          response.status === 0 || response.status
            ? response.status
            : 'unknown http  status',
        ),
    );
    this.name = 'HttpError';
    this.response = response;
  }
}

export default HttpError;
