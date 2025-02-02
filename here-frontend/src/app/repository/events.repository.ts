import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { EventResource } from '../domain/resource/event.resource';
import { Event } from '../domain/event';

@Injectable({providedIn: 'root'})
export class EventsRepository {
  constructor(private http: HttpClient) {}

  public retrieveEvents(): Observable<Event[]> {
    return this.http.get<EventResource[]>('eventos').pipe(
      map((resources: EventResource[]) => resources.map((resource) => new Event(resource)))
    );
  }

  public retrieveEventDetail(id: string): Observable<Event> {
    return this.http.get<EventResource>(`eventos/${id}`).pipe(
      map((resource: EventResource) => new Event(resource))
    );
  }
}
