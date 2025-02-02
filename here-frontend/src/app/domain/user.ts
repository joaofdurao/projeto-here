import { UserResource } from "./resource/user.resource";

export class User {
  id: string
  name: string
  email: string
  password: string
  course: string
  registration: string
  status: boolean

  constructor(resource?: UserResource) {
      if (resource) {
        this.id = resource.id
        this.name = resource.name
        this.email = resource.email
        this.password = resource.password
        this.course = resource.curso
        this.registration = resource.matricula
        this.status = resource.status
      }
  }
}
