import {HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {throwError} from "rxjs";
import {Injectable} from "@angular/core";

/**
 * Intercepts HTTP requests and adds authorization token to the request headers.
 */
@Injectable({ providedIn: 'root' })
export class JwtInterceptor implements HttpInterceptor {


  /**
   * Intercepts an HTTP request and adds authorization header if a token exists in local storage.
   *
   * @param {HttpRequest<any>} request - The HTTP request to intercept.
   * @param {HttpHandler} next - The handler for the intercepted request.
   * @return {Observable<HttpEvent<any>>} - An Observable that represents the next event in the HTTP stream.
   */
  public intercept(request: HttpRequest<any>, next: HttpHandler){
    console.log("JwtInterceptor - intercept method called");
    if (this.isExcludedRequest(request)) {
      return next.handle(request);
    }
    const token: string | null = localStorage.getItem('token');
    if (token) {
      request = this.addAuthorizationHeader(request, token);
      return next.handle(request);
    } else {
      return throwError(() => new HttpErrorResponse({ status: 401, statusText: 'You must be authenticated' }));
    }
  }

  /**
   * Checks if the given request is excluded based on the URL.
   *
   * @param {HttpRequest<any>} request - The request to be checked.
   * @return {boolean} - Returns true if the request is excluded, false otherwise.
   * @private
   */
  private isExcludedRequest(request: HttpRequest<any>): boolean {
    const excludedUrls: string[] = ['/login', '/register'];
    return excludedUrls.some((url: string) => request.url.includes(url));
  }

  /**
   * Adds authorization header to the request.
   *
   * @param {HttpRequest<any>} request - The request object to add the header to.
   * @param {string} token - The token to be added as the Authorization value.
   * @returns {HttpRequest<any>} - The request object with the added authorization header.
   * @private
   */
  private addAuthorizationHeader(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
}
