import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private $isUserLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(this.isLoggedIn);


  /**
   * Checks if the user is currently logged in getting the user token.
   *
   * @return {boolean} - Returns true if the user is logged in, false otherwise.
   */
  public get isLoggedIn(): boolean {

    const result: boolean = localStorage.getItem('token') != null;
    console.log("result : {}", result);
    return result;
  }

  public $isLoggedIn(): Observable<boolean> {
    return this.$isUserLoggedIn.asObservable();
  }

  public login(token : string): void {
    localStorage.setItem('token', token);
    this.$isUserLoggedIn.next(this.isLoggedIn);
  }

  public get getJwtToken() : string | null {
    return localStorage.getItem('token');
  }
  constructor(private router: Router) {
  }
}
