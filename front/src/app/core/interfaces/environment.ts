import {MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

/**
 * Interface representing the environment configuration.
 *
 * @author Pignon Pierre-Olivier
 *
 * @version 1.0
 */
export interface Environment {
  production: boolean;
  apiUrl: string;
  errorSnackBarDuration: number;
  errorSnackBarHorizontalPosition: MatSnackBarHorizontalPosition;
  errorSnackBarVerticalPosition: MatSnackBarVerticalPosition;
}
