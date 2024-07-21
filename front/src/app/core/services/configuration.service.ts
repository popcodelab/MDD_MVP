import {Injectable} from '@angular/core';
import {MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  private readonly errorSnackBarDuration: number = 5000;
  private readonly errorSnackBarHorizontalPosition: MatSnackBarHorizontalPosition = 'center';
  private readonly errorSnackBarVerticalPosition: MatSnackBarVerticalPosition = 'bottom';


  constructor() {
    if (environment.errorSnackBarDuration != null) {
      this.errorSnackBarDuration = environment.errorSnackBarDuration;
    }
    if (environment.errorSnackBarHorizontalPosition != null) {
      this.errorSnackBarHorizontalPosition = environment.errorSnackBarHorizontalPosition;
    }
    if (environment.errorSnackBarVerticalPosition != null) {
      this.errorSnackBarVerticalPosition = environment.errorSnackBarVerticalPosition;
    }
  }

  /**
   * Retrieves the duration of the error snackbar.
   *
   * @returns {number} The duration of the error snackbar in milliseconds.
   */
  getErrorSnackBarDuration(): number {
    return this.errorSnackBarDuration;
  }

  /**
   * Retrieves the horizontal position of the error snackbar.
   *
   * @returns {string} The horizontal position of the error snackbar.
   */
  getErrorSnackBarHorizontalPosition(): MatSnackBarHorizontalPosition {
    return this.errorSnackBarHorizontalPosition;
  }

  /**
   * Get the vertical position of the error snackbar.
   *
   * @returns {string} The vertical position of the error snackbar.
   */
  getErrorSnackBarVerticalPosition(): MatSnackBarVerticalPosition {
    return this.errorSnackBarVerticalPosition;
  }
}
