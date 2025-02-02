import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { Event } from '../../../domain/event';
import { UserRepository } from '../../../repository/user.repository';

@Component({
  selector: 'app-my-events',
  standalone: true,
  imports: [TableModule, CommonModule, ButtonModule],
  templateUrl: './my-events.component.html',
  styleUrl: './my-events.component.css',
  providers: [DatePipe]
})
export class MyEventsComponent implements OnInit {
  private userRepository = inject(UserRepository)
  events: Event[]

  constructor(private datePipe: DatePipe, private route: Router) {}

  ngOnInit(): void {
    this.userRepository.retrieveMyEvents().subscribe(
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
