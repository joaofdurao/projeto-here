import { ParticipationRepository } from '../../../repository/participation.repository';
import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { EventsRepository } from '../../../repository/events.repository';
import { ActivatedRoute } from '@angular/router';
import { Event } from '../../../domain/event';
import { QrCodeModalComponent } from '../../../core/components/qr-code-modal/qr-code-modal.component';

@Component({
  selector: 'app-event-detail',
  standalone: true,
  imports: [ButtonModule, CommonModule, DividerModule, QrCodeModalComponent],
  templateUrl: './event-detail.component.html',
  styleUrl: './event-detail.component.css',
  providers: [DatePipe]
})
export class EventDetailComponent implements OnInit{
  event: Event
  eventId: string
  qrCodeUrl: string
  showQrCodeModal = false
  private eventRepository = inject(EventsRepository)
  private participationRepository = inject(ParticipationRepository)

  constructor(private datePipe: DatePipe, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.eventId = params.get('id')
    });
    this.eventRepository.retrieveEventDetail(this.eventId).subscribe(
      data => {
        this.event = data;
      }
    )
  }

  formatarDiaSemanaHora(date: string): string {
    const data = new Date(date)
    const diaSemana = this.datePipe.transform(data, 'EEEE')
    const hora = this.datePipe.transform(data, 'H')
    const minuto = this.datePipe.transform(data, 'H:mm')?.replace(':', 'h')
    return `${diaSemana} às ${hora}h`
  }

  formatarDataMes(date: string): string {
    const data = new Date(date)
    return this.datePipe.transform(data, 'd \'de\' MMMM')
  }

  public subscribeEvent(): void {
    this.participationRepository.subscribeEvent(this.eventId).subscribe(value => {
      if (!value.error) {
        this.event.participationId = value.data.id
      }
    })
  }

  public unSubscribeEvent(): void {
    this.participationRepository.unSubscribeEvent(this.event.participationId).subscribe(value => {
      if (!value.error) {
        this.event.participationId = ""
      }
    })
  }

  public generateQRCode() {
    this.participationRepository.getParticipacaoQRCode(this.event.id).subscribe((response) => {
      if (!response.error && response.data) {
        const url = URL.createObjectURL(response.data);
        this.openQrCodeModal(url);
      } else {
        console.error('Erro ao obter QR Code.');
      }
    });
  }

  get isUserSubscribed() {
    return this.event.participationId
  }

  public openQrCodeModal(url: string) {
    this.qrCodeUrl = url;
    this.showQrCodeModal = true;
  }

  public closeQrCodeModal() {
    this.showQrCodeModal = false;
  }
}
