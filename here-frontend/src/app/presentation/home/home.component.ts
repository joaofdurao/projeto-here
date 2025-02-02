import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { EventsRepository } from '../../repository/events.repository';
import { Event } from '../../domain/event';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [TableModule, CommonModule, ButtonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  providers: [DatePipe]
})
export class HomeComponent implements OnInit {
  private eventsRepository = inject(EventsRepository)
  events: Event[]

  constructor(private datePipe: DatePipe, private route: Router) {}

  ngOnInit(): void {
    this.eventsRepository.retrieveEvents().subscribe(
      data => {
        this.events = data;
      }
    );
  }

  formatDate(date: string): string {
    const dateObject = new Date(date)
    const dayMonth = this.datePipe.transform(dateObject, 'dd/MM')
    const weekday = this.datePipe.transform(dateObject, 'EEEE')
    return `${dayMonth} - ${weekday}`
  }

  goToEventDetail(id: string) {
    this.route.navigate(["/event-detail", id])
  }
}
