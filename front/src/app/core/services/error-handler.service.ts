import {Injectable} from '@angular/core';
import {SnackBarService} from "./snack-bar.service";
import {HttpErrorResponse} from "@angular/common/http";
import {APP_CONSTANTS} from "../../shared/constants";

/**
 * ErrorHandlerService class handles error management and displaying error messages.
 *
 * @author Pierre-Olivier Pignon
 * @version 1.0
 */
@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  private readonly AN_ERROR_HAS_OCCURRED: string = "An error has occurred";

  constructor(private snackBarService: SnackBarService) {
  }

  /**
   * Handles the given error and displays an appropriate message to the user.
   *
   * @param {Error|HttpErrorResponse} error - The error object to handle.
   *
   * @return {void}
   */
  handleError(error: Error | HttpErrorResponse): void {
    let message: string;
    if (error instanceof HttpErrorResponse) {
      if (error.status === 401) {
        message = APP_CONSTANTS.ERROR_MESSAGES.MUST_BE_AUTHENTICATED;
        this.logStackTrace(error);
      } else {
        message = this.AN_ERROR_HAS_OCCURRED + ' : ' + error.error.error;
        this.logStackTrace(error);
      }
    } else {
      message = this.AN_ERROR_HAS_OCCURRED + ' : ' + error.message;
      this.logStackTrace(error);
    }
    this.snackBarService.openSnackBar(message);
  }

  /**
   * Logs the stack trace of the given error.
   *
   * @param {any} error - The error object for which to log the stack trace.
   * @private
   * @return {void}
   */
  private logStackTrace(error: any): void {
    console.error('An error occurred:', error.message);
    console.error('Stack trace:', error.stack);
  }
}
