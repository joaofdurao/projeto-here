import { UserDto } from './../domain/dto/user.dto';
import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../core/components/infra/auth.service';
import { User } from '../domain/user';
import { catchError, map, Observable, of, pipe } from 'rxjs';
import { ResponseWrapper } from '../core/components/response/response-wrapper';
import { UserResource } from '../domain/resource/user.resource';
import { UserUpdateResource } from '../domain/resource/user-update.resource';
import { Event } from '../domain/event';
import { EventResource } from '../domain/resource/event.resource';

@Injectable({providedIn: 'root'})
export class UserRepository {
  private authService = inject(AuthService)

  constructor(private http: HttpClient) {}

  createUser(user: User): Observable<ResponseWrapper<User>> {
    const dto: UserDto = this.loginToDto(user)

    return this.http.post<any>('usuarios', dto)
    .pipe(
      map((data: UserResource) => ({ data: new User(data), error: false } as ResponseWrapper<User>)),
      catchError((error) => {
        if (error.message) {
          alert(error.message)
        } else {
          alert('Ocorreu um erro. Tente novamente mais tarde.')
        }
        return of({ data: null, error: true } as ResponseWrapper<User>)
      })
    );
  }
  loginToDto(user: User): UserDto {
    const dto: UserDto = {
      name: user.name,
      email: user.email,
      password: user.password ,
      curso: user.course,
      matricula: user.registration,
      status: user.status,
    }
    return dto;
  }
  getUserDetail(): Observable<ResponseWrapper<User>> {
    return this.http.get<any>(`usuarios/detail`)
    .pipe(
      map((data: UserResource) => ({ data: new User(data), error: false } as ResponseWrapper<User>)),
      catchError((error) => {
        if (error.message) {
          alert(error.message)
        } else {
          alert('Ocorreu um erro. Tente novamente mais tarde.')
        }
        return of({ data: null, error: true } as ResponseWrapper<User>)
      })
    );
  }

    public updateUser(userDetail: User): Observable<ResponseWrapper<User>> {
      const dto: UserDto = this.loginToDto(userDetail)

    return this.http.put<any>(`usuarios/detail`, dto)
    .pipe(
      map((data: UserUpdateResource) => {
        this.authService.login(data.token)
        return ({ data: new User(data.userResource), error: false } as ResponseWrapper<User>)
      }),
      catchError((error) => {
        if (error.message) {
          alert(error.message)
        } else {
          alert('Ocorreu um erro. Tente novamente mais tarde.')
        }
        return of({ data: null, error: true } as ResponseWrapper<User>)
      })
    );
  }

  public retrieveMyEvents(): Observable<Event[]> {
    return this.http.get<EventResource[]>(`usuarios/detail/eventos`).pipe(
      map((resources: EventResource[]) => resources.map((resource) => new Event(resource)))
    );
  }
}
