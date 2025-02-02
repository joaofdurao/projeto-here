import { EventResource } from "./resource/event.resource"

export class Event {
  id: string
  title: string
  description: string
  date: string
  hourClass: number
  course: string
  place: string
  speaker: string
  speakerJob: string
  status: boolean
  participationId: string

  constructor(resource?: EventResource) {
    if (resource) {
      this.id = resource.id
      this.title = resource.titulo
      this.description = resource.descricao
      this.date = resource.dataHora
      this.hourClass = resource.horaAula
      this.course = resource.curso
      this.place = resource.local
      this.speaker = resource.palestrante
      this.speakerJob = resource.profissaoPalestrante
      this.status = resource.status
      this.participationId = resource.participacaoId
    }
  }
}
