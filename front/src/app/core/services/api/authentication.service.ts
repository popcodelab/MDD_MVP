import { Injectable } from '@angular/core';
import {environment} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {LoginRequest} from "../../interfaces/auth/login.request";
import {Observable} from "rxjs";
import {LoginResponse} from "../../interfaces/auth/login.response";
import {RegisterRequest} from "../../interfaces/auth/register.request";

/**
 * Represents an authentication service that provides methods for user login and registration.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  /**
   * The serviceUrl variable is a string that represents the API url for authentication.
   * It is derived by concatenating the environment.serviceUrl with the '/auth' path.
   *
   * @type {string}
   * @example
   * // Usage
   * console.log(serviceUrl); // "http://example.com/api/auth"
   */
  private readonly serviceUrl: string = environment.apiUrl + '/auth';

  /**
   * Constructs a new instance of the class.
   * @param {HttpClient} http - The HTTP client instance to use for making HTTP requests.
   */
  constructor(private http: HttpClient) { }

  /**
   * Logs in a user using the provided login request.
   *
   * @param {LoginRequest} loginRequest - The login request containing the user's credentials.
   *
   * @return {Observable<LoginResponse>} - An Observable that resolves to a LoginResponse object representing the login result.
   */
  public login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.serviceUrl}/login`, loginRequest);
  }

  /**
   * Registers a user with the server.
   *
   * @param {RegisterRequest} registerRequest - The registration details of the user.
   * @return {Observable<void>} Observable that emits void.
   */
  public register(registerRequest: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.serviceUrl}/register`, registerRequest);
  }
}
