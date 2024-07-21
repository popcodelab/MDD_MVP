import { CanActivateFn } from '@angular/router';

export const unauthGuard: CanActivateFn = (route, state) => {
  return true;
};
