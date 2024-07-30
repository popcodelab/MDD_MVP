import {Router, UrlTree} from '@angular/router';
import {Injectable} from "@angular/core";
import {SessionService} from "../../features/auth/services/session.service";
import {map, Observable} from "rxjs";

@Injectable({
  providedIn: 'root' }
)
export class unAuthGuardService {
  constructor(
    private router: Router,
    private sessionService: SessionService,
  ) { }

  public canActivate(): Observable<boolean | UrlTree> {
    return this.sessionService.isUserLoggedIn$.pipe(
      map(isLoggedIn => this.getUrlTreeOrBoolean(isLoggedIn))
    );
  }

  private getUrlTreeOrBoolean(isLoggedIn: boolean): boolean | UrlTree {
    if (isLoggedIn) {
      return this.router.parseUrl('/posts');
    }
    return !isLoggedIn;
  }
}
