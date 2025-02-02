export interface ResponseWrapper<T> {
  data: T | null;
  error: boolean;
}

export class ResponseWrapper<T> {
  constructor(
    public data: T | null,
    public error: boolean
  ) {}
}
