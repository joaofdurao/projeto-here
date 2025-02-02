import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Login } from '../domain/login';
import { ResponseWrapper } from '../core/components/response/response-wrapper';

@Injectable({providedIn: 'root'})
export class LoginRepository {

  constructor(private http: HttpClient) {}

  login(login: Login): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<any>('usuarios/login', login, { headers, responseType: 'text' as 'json'  })
      .pipe(
        map(data => ({ data, error: false } as ResponseWrapper<string>)),
        catchError((error) => {
          const errorMessage = JSON.parse(error.error)
          if (errorMessage.message) {
            alert(errorMessage.message)
          } else {
            alert('Ocorreu um erro. Tente novamente mais tarde.')
          }
          return of({ data: null, error: true } as ResponseWrapper<string>)
        })
      );
  }
}
