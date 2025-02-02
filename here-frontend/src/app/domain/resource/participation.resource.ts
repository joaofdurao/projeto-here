import { EventResource } from "./event.resource"
import { UserResource } from "./user.resource"

export interface ParticipationResource {
  id: string
  presenca: boolean
  status: boolean
  eventoId: string
  usuarioId: string
}
