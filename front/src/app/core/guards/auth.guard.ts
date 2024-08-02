import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from "@angular/core";
import {SessionService} from "../../features/auth/services/session.service";
import {firstValueFrom, map, take} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class authGuardService {
  constructor(
    private sessionService: SessionService,
    private router: Router
  ) {
  }

  /**
   * Determines if the user is authorized to access a specific route.
   *
   * @param {ActivatedRouteSnapshot} route - The route being navigated to.
   * @param {RouterStateSnapshot} state - The state of the router.
   *
   * @returns {Promise<boolean>} - A promise that resolves to a boolean value indicating if the user is authorized to access the route.
   */
  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean> {
    return await this.checkUserLoginStatus();
  }

  /**
   * Checks the login status of the user.
   *
   * @private
   * @returns {Promise<boolean>} A Promise that resolves to a boolean indicating the login status of the user.
   */
  private async checkUserLoginStatus(): Promise<boolean> {
    await this.sessionService.setUpUserSession();
    return await firstValueFrom(
      this.sessionService.isUserLoggedIn$.pipe(
        take(1),
        map(this.handleNotAuthenticated.bind(this))
      )
    );
  }

  private handleNotAuthenticated(isAuthenticated: boolean): boolean {
    if (isAuthenticated) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }

}
