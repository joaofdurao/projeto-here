import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, map, Observable, of } from "rxjs";
import { ResponseWrapper } from "../core/components/response/response-wrapper";
import { ParticipationResource } from "../domain/resource/participation.resource";

@Injectable({providedIn: 'root'})
export class ParticipationRepository {

  constructor(private http: HttpClient) {}

  public subscribeEvent(id: string): Observable<ResponseWrapper<ParticipationResource>> {
    return this.http.post<any>('participacoes', id)
    .pipe(
      map(data => ({ data, error: false } as ResponseWrapper<ParticipationResource>)),
      catchError((error) => {
        if (error.message) {
          alert(error.message)
        } else {
          alert('Ocorreu um erro. Tente novamente mais tarde.')
        }
        return of({ data: null, error: true } as ResponseWrapper<ParticipationResource>)
      })
    );
  }

  public unSubscribeEvent(id: string): Observable<ResponseWrapper<string>> {
    return this.http.put<any>(`participacoes/disable/${id}`, null, { responseType: 'text' as 'json'  })
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

  getParticipacaoQRCode(eventoId: string): Observable<ResponseWrapper<Blob>> {
    return this.http.get(`participacoes/qrcode/usuario/${eventoId}`, { responseType: 'blob' }).pipe(
      map((data) => ({ data, error: false } as ResponseWrapper<Blob>)),
      catchError(() => of({ data: null, error: true } as ResponseWrapper<Blob>))
    );
  }
}
