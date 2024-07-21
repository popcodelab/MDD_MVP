import { Injectable } from '@angular/core';
import {SnackBarService} from "./snack-bar.service";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  private readonly AN_ERROR_HAS_OCCURRED: string = "An error has occurred";

  constructor(private snackBarService: SnackBarService) { }

  handleError(error: Error | HttpErrorResponse) {
    let message: string;

    if (error instanceof HttpErrorResponse) {
      if (error.status === 401) {
        message = 'You must be authenticated';
      } else {
        message = this.AN_ERROR_HAS_OCCURRED + ' : ' + error.message;
      }
    } else {
      message = this.AN_ERROR_HAS_OCCURRED + ' : '  + error.message;
    }

    this.snackBarService.openSnackBar(message);
  }
}
