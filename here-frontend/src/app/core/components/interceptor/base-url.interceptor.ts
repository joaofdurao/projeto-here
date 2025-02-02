import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../../../environment";

@Injectable()
export class BaseUrlInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!request.url.startsWith('http')) {
      const apiReq = request.clone({
        url: `${environment.apiUrl}/${request.url}`
      });
      return next.handle(apiReq);
    }
    return next.handle(request);
  }
}
