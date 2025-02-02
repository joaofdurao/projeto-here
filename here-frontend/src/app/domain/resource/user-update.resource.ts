import { UserResource } from "./user.resource";

export interface UserUpdateResource {
  userResource: UserResource
  token: string
}
