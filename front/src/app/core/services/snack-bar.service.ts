import { Injectable } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {environment} from "../../../environments/environment";
import {ConfigurationService} from "./configuration.service";

/**
 * Service for displaying snackbars.
 *
 * @remarks
 * This service provides methods for opening snackbars with different types of messages.
 *
 * @category Service
 */
@Injectable({
  providedIn: 'root'
})
/**
 * @author Pignon Pierre-Olivier
 *
 * @version 1.0
 */
export class SnackBarService {

  constructor(private snackBar: MatSnackBar,
              private configurationService: ConfigurationService) {
  }

  /**
   * Opens a snackbar with the provided message and action.
   *
   * @param {string} message - The message to be displayed in the snackbar.
   * @param {string} action - The action button text to be displayed in the snackbar. Default is 'Close'.
   * @param {string} cssClass - The CSS class to be applied to the snackbar. Default is 'error-snackbar'.
   * @return {void} - This method does not return anything.
   */
  openSnackBar(message: string, action: string = 'Close', cssClass: string = '\'error-snackbar\''): void {
    this.snackBar.open(message, action, {
      duration: this.configurationService.getErrorSnackBarDuration(),
      horizontalPosition: this.configurationService.getErrorSnackBarHorizontalPosition(),
      verticalPosition: this.configurationService.getErrorSnackBarVerticalPosition(),
      panelClass: [cssClass]
    });
  }
}
