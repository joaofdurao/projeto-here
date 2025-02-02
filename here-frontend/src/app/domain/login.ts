import { LoginResource } from "./resource/login.resource"

export class Login {
  email?: string
  password?: string

  constructor(resource?: LoginResource) {
    if (resource) {
      this.email = resource.email
      this.password = resource.senha
    }
  }
}
